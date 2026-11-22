package bw.co.knowvera.organisation.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Reads a client request spreadsheet (.xlsx, .xls or .csv; told apart by content, not by name)
 * into rows of text keyed by column. The first row holds the column names; columns are found by
 * name (case, spaces and punctuation ignored), so their order does not matter and extra columns
 * are ignored. Blank rows are skipped.
 */
final class ClientRequestSpreadsheet {

    static final String FIRST_NAME = "firstname";
    static final String MIDDLE_NAME = "middlename";
    static final String SURNAME = "surname";
    static final String IDENTITY_TYPE = "identitytype";
    static final String IDENTITY_NO = "identityno";
    static final String EMAIL = "emailaddress";
    static final String PHONES = "phonenumbers";
    static final String PHYSICAL_ADDRESS = "physicaladdress";
    static final String POSTAL_ADDRESS = "postaladdress";
    static final String SEX = "sex";
    static final String DATE_OF_BIRTH = "dateofbirth";
    static final String NATIONALITY = "nationality";
    static final String MARITAL_STATUS = "maritalstatus";
    static final String EMPLOYMENT_STATUS = "employmentstatus";

    /** Other names a column may go by. */
    private static final Map<String, String> ALIASES = Map.ofEntries(
            Map.entry("lastname", SURNAME),
            Map.entry("identitynumber", IDENTITY_NO),
            Map.entry("idnumber", IDENTITY_NO),
            Map.entry("idno", IDENTITY_NO),
            Map.entry("idtype", IDENTITY_TYPE),
            Map.entry("email", EMAIL),
            Map.entry("phonenumber", PHONES),
            Map.entry("phone", PHONES),
            Map.entry("gender", SEX),
            Map.entry("dob", DATE_OF_BIRTH));

    /** A data row: its number as a spreadsheet shows it (the header is row 1) and its non-blank values. */
    record Row(int number, Map<String, String> values) {

        /** The trimmed value of a column, or null when the cell is blank or the column is absent. */
        String get(String column) {
            return values.get(column);
        }
    }

    private static final byte[] ZIP = { 'P', 'K' };
    private static final byte[] OLE2 = { (byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0 };

    private ClientRequestSpreadsheet() {
    }

    static List<Row> read(byte[] content) {

        try {
            return startsWith(content, ZIP) || startsWith(content, OLE2) ? readWorkbook(content) : readCsv(content);
        } catch (ClientRequestImportException e) {
            throw e;
        } catch (IOException | RuntimeException e) {
            throw new ClientRequestImportException(
                    "The file could not be read as an Excel or CSV spreadsheet: " + e.getMessage());
        }
    }

    private static List<Row> readWorkbook(byte[] content) throws IOException {

        List<Row> rows = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {

            Sheet sheet = workbook.getNumberOfSheets() == 0 ? null : workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter(Locale.ROOT);

            List<String> header = null;
            if (sheet != null) {
                for (org.apache.poi.ss.usermodel.Row sheetRow : sheet) {

                    List<String> cells = new ArrayList<>();
                    for (int i = 0; i < Math.max(sheetRow.getLastCellNum(), 0); i++) {
                        cells.add(text(sheetRow.getCell(i), formatter, evaluator));
                    }
                    if (cells.stream().allMatch(String::isBlank)) {
                        continue;
                    }
                    if (header == null) {
                        header = columns(cells);
                    } else {
                        rows.add(row(sheetRow.getRowNum() + 1, header, cells));
                    }
                }
            }
        }
        return rows;
    }

    /** A cell as text: dates as ISO dates, whole numbers without a decimal point or exponent. */
    private static String text(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {

        if (cell == null) {
            return "";
        }
        CellType type = cell.getCellType() == CellType.FORMULA ? evaluator.evaluateFormulaCell(cell)
                : cell.getCellType();
        if (type == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            }
            return BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();
        }
        return formatter.formatCellValue(cell, evaluator).trim();
    }

    private static List<Row> readCsv(byte[] content) throws IOException {

        String text = new String(content, StandardCharsets.UTF_8);
        if (text.startsWith("﻿")) {
            text = text.substring(1);
        }

        // Excel writes ';' between values in locales where ',' is the decimal separator
        String firstLine = text.lines().filter(line -> !line.isBlank()).findFirst().orElse("");
        char delimiter = count(firstLine, ';') > count(firstLine, ',') ? ';' : ',';

        List<Row> rows = new ArrayList<>();
        try (CSVParser parser = CSVParser.parse(new StringReader(text), CSVFormat.DEFAULT.builder()
                .setDelimiter(delimiter)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build())) {

            List<String> header = null;
            for (CSVRecord record : parser) {

                List<String> cells = record.toList();
                if (cells.stream().allMatch(String::isBlank)) {
                    continue;
                }
                if (header == null) {
                    header = columns(cells);
                } else {
                    // Quoted values can span lines, so the record number is not the line the row starts on
                    rows.add(row((int) parser.getCurrentLineNumber() - lines(record) + 1, header, cells));
                }
            }
        }
        return rows;
    }

    /** Lines a record takes up: one, plus one for each line break inside its values. */
    private static int lines(CSVRecord record) {
        int lines = 1;
        for (String value : record) {
            lines += count(value, '\n');
        }
        return lines;
    }

    private static List<String> columns(List<String> headerCells) {
        List<String> columns = new ArrayList<>();
        for (String cell : headerCells) {
            String name = cell.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
            columns.add(ALIASES.getOrDefault(name, name));
        }
        return columns;
    }

    private static Row row(int number, List<String> header, List<String> cells) {
        Map<String, String> values = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(header.size(), cells.size()); i++) {
            String value = cells.get(i) == null ? "" : cells.get(i).trim();
            if (!header.get(i).isEmpty() && !value.isEmpty()) {
                values.putIfAbsent(header.get(i), value);
            }
        }
        return new Row(number, values);
    }

    private static int count(String text, char c) {
        return (int) text.chars().filter(ch -> ch == c).count();
    }

    private static boolean startsWith(byte[] data, byte[] prefix) {
        if (data.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
}
