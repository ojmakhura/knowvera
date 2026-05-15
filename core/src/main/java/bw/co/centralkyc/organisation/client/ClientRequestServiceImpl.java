// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::organisation::client::ClientRequestService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.organisation.client;

import bw.co.centralkyc.PhoneNumber;
import bw.co.centralkyc.PhoneType;
import bw.co.centralkyc.PropertySearchOrder;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.SortOrderFactory;
import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.Document;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentMapper;
import bw.co.centralkyc.document.DocumentRepository;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualIdentityType;
import bw.co.centralkyc.individual.IndividualMapper;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.individual.Sex;
import bw.co.centralkyc.kyc.KycComplianceStatus;
import bw.co.centralkyc.messaging.ClientRequestNotification;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import bw.co.centralkyc.sequence.SequenceGenerator;
import bw.co.centralkyc.sequence.SequenceGeneratorRepository;
import bw.co.centralkyc.sequence.SequenceGeneratorService;
import bw.co.centralkyc.sequence.SequencePart;
import bw.co.centralkyc.sequence.SequencePartType;
import bw.co.centralkyc.settings.SettingsMapper;
import bw.co.centralkyc.settings.SettingsRepository;
import bw.co.roguesystems.comm.ContentType;
import bw.co.roguesystems.comm.MessagingPlatform;
import bw.co.roguesystems.comm.message.CommMessageDTO;
import io.micrometer.common.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.centralkyc.organisation.client.ClientRequestService
 */
@Service("clientRequestService")
public class ClientRequestServiceImpl
        extends ClientRequestServiceBase {

    @Value("${app.request-token-length}")
    private int requestTokenLength;

     private static final String SEQUENCE_NAME = "CLIENT_REQUEST_REF";

    private final PasswordEncoder passwordEncoder;
    private final ClientRequestNotification clientRequestNotification;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final OrganisationRepository organisationRepository;

    public ClientRequestServiceImpl(ClientRequestRepository clientRequestRepository,
            ClientRequestMapper clientRequestMapper,PasswordEncoder passwordEncoder,
            ClientRequestNotification clientRequestNotification, IndividualRepository individualRepository,
            IndividualMapper individualMapper, OrganisationRepository organisationRepository,
            SequenceGeneratorRepository sequenceGeneratorRepository, SequenceGeneratorService sequenceGeneratorService,
            DocumentRepository documentRepository, DocumentMapper documentMapper,
            SettingsRepository settingsRepository, SettingsMapper settingsMapper, MessageSource messageSource) {
        super(clientRequestRepository, clientRequestMapper, individualRepository,
                individualMapper, documentRepository, documentMapper, settingsRepository,
                settingsMapper, messageSource);
        // TODO Auto-generated constructor stub
        this.passwordEncoder = passwordEncoder;
        this.clientRequestNotification = clientRequestNotification;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.organisationRepository = organisationRepository;
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#findById(String)
     */
    @Override
    protected ClientRequestDTO handleFindById(String id)
            throws Exception {

        ClientRequest clientRequest = clientRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        return clientRequestMapper.toClientRequestDTO(clientRequest);
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#save(ClientRequestDTO)
     */
    @Override
    protected ClientRequestDTO handleSave(ClientRequestDTO clientRequest)
            throws Exception {

        ClientRequest clientRequestEntity = clientRequestMapper.clientRequestDTOToEntity(clientRequest);

        if (StringUtils.isBlank(clientRequestEntity.getRef())) {

            StringBuilder sb = new StringBuilder(clientRequestEntity.getOrganisation().getCode());
            sb.append('_').append(SEQUENCE_NAME);

            SequenceGenerator sequenceGenerator = sequenceGeneratorRepository.findByName(sb.toString()).orElse(null);

            if (sequenceGenerator == null) {

                sequenceGenerator = new SequenceGenerator();
                sequenceGenerator.setName(sb.toString());
                sequenceGenerator.setTargetEntity(TargetEntity.CLIENT_REQUEST);

                List<SequencePart> sequenceParts = new ArrayList<>();

                SequencePart counterPart = new SequencePart();
                counterPart.setPosition(0);
                counterPart.setType(SequencePartType.STATIC);
                counterPart.setInitialValue(clientRequestEntity.getOrganisation().getCode() + '_' + "KR-");
                counterPart.setName(sb.toString() + "_PREFIX");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                counterPart = new SequencePart();
                counterPart.setPosition(1);
                counterPart.setType(SequencePartType.COUNTER);
                counterPart.setName(sb.toString() + "_COUNTER");
                counterPart.setInitialValue("00000000");
                counterPart.setSequenceGenerator(sequenceGenerator);
                sequenceParts.add(counterPart);

                sequenceGenerator.setSequenceParts(sequenceParts);
                sequenceGenerator = sequenceGeneratorRepository.save(sequenceGenerator);
            }

            String nextRef = sequenceGeneratorService.generateNextSequenceValue(sb.toString(), true);
            clientRequestEntity.setRef(nextRef);
        }

        boolean isNew = StringUtils.isBlank(clientRequest.getId());
        String token = null;

        if (isNew) {
            // Generate a random token with letters, digits and special characters
            token = RandomStringUtils
                    .secure()
                    .next(requestTokenLength, true, true);

            // Encode the token
            String encodedToken = passwordEncoder.encode(token);
            clientRequestEntity.setAccountRequestToken(encodedToken);
        }

        clientRequestEntity = clientRequestRepository.save(clientRequestEntity);

        if (isNew) {

            // For new requests, we might want to send the token via email or other means
            // For this example, we'll just print it to the console (not recommended for
            // production)

            clientRequestNotification.queueEmailNotificationsForRequests(
                    Arrays.asList(clientRequestEntity),
                    Map.of(clientRequestEntity.getTargetId(), token),
                    clientRequest.getOrganisation());

        }

        return clientRequestMapper.toClientRequestDTO(clientRequestEntity);
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        if (clientRequestRepository.existsById(UUID.fromString(id))) {
            clientRequestRepository.deleteById(UUID.fromString(id));
            return true;
        }

        throw new ClientRequestServiceException("ClientRequest not found");
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#getAll()
     */
    @Override
    protected List<ClientRequestDTO> handleGetAll()
            throws Exception {

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(clientRequestRepository.findAll());
        this.updateClientName(dtos);

        return dtos;
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#search(ClientRequestSearchCriteria,
     *      Set<PropertySearchOrder>)
     */
    @Override
    protected List<ClientRequestDTO> handleSearch(ClientRequestSearchCriteria criteria,
            Set<PropertySearchOrder> sortProperties)
            throws Exception {

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);

        Sort sort = SortOrderFactory.createSortOrder(sortProperties);

        Collection<ClientRequest> requests = (sort != null)
                ? clientRequestRepository.findAll(spec, sort)
                : clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;

    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<ClientRequestDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ClientRequest> requests = clientRequestRepository.findAll(pageRequest);

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#search(SearchObject<ClientRequestSearchCriteria>)
     */
    @Override
    protected Page<ClientRequestDTO> handleSearch(SearchObject<ClientRequestSearchCriteria> criteria)
            throws Exception {

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria.getCriteria());
        Sort sort = SortOrderFactory.createSortOrder(criteria.getSortings());

        Integer pageNumber = criteria.getPageNumber() != null ? criteria.getPageNumber() : 0;
        Integer pageSize = criteria.getPageSize() != null ? criteria.getPageSize() : 10;

        PageRequest pageRequest = (sort != null)
                ? PageRequest.of(pageNumber, pageSize, sort)
                : PageRequest.of(pageNumber, pageSize);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, pageRequest);

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    private Specification<ClientRequest> buildSpecificationFromCriteria(ClientRequestSearchCriteria criteria) {

        Specification<ClientRequest> spec = ((root, query, builder) -> builder.conjunction());

        if (criteria == null) {
            return spec;
        }

        if (StringUtils.isNotBlank(criteria.getOrganisationId())) {

            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("organisation").get("id"),
                            UUID.fromString(criteria.getOrganisationId())));
        }

        if (criteria.getTarget() != null) {

            spec = spec.and((root, query, cb) -> cb.equal(root.get("target"), criteria.getTarget()));
        }

        if (StringUtils.isNotBlank(criteria.getTargetId())) {

            spec = spec.and((root, query, cb) -> cb.equal(root.get("targetId"), criteria.getTargetId()));
        }

        if (CollectionUtils.isNotEmpty(criteria.getStatuses())) {

            spec = spec.and((root, query, cb) -> root.get("status").in(criteria.getStatuses()));

        }

        return spec;
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#findByOrganisation(String)
     */
    @Override
    protected List<ClientRequestDTO> handleFindByOrganisation(String organisationId)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setOrganisationId(organisationId);
        // criteria.setStatus(ClientRequestStatus.);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        List<ClientRequest> requests = clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#findByOrganisation(String,
     *      Integer, Integer)
     */
    @Override
    protected Page<ClientRequestDTO> handleFindByOrganisation(String organisationId, Integer pageNumber,
            Integer pageSize)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setOrganisationId(organisationId);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#findByStatus(ClientRequestStatus)
     */
    @Override
    protected List<ClientRequestDTO> handleFindByStatus(ClientRequestStatus status)
            throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.equal(root.get("status"), status);

        List<ClientRequest> requests = clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#uploadRequests(InputStream)
     */
    @Override
    protected Page<ClientRequestDTO> handleUploadRequests(InputStream inputStream, String user,
            String organisationId, DocumentDTO document, TargetEntity target, String organisation)
            throws Exception {

        if (target != null && target != TargetEntity.INDIVIDUAL && target != TargetEntity.ORGANISATION) {

            throw new ClientRequestServiceException("Only 'null', 'ORGANISATION' and 'INDIVIDUAL' are allowed.");
        }

        Map<String, String> tokenMap = new HashMap<>();

        List<ClientRequest> clientRequests = new ArrayList<>();

        try {
            // Try to detect if it's Excel or CSV by attempting to read as Excel first
            inputStream.mark(Integer.MAX_VALUE); // Mark the stream so we can reset if needed

            Document d = documentMapper.documentDTOToEntity(document);

            try {
                // Try reading as Excel (.xlsx)
                Workbook workbook = new XSSFWorkbook(inputStream);
                clientRequests = processIndividualExcelFile(workbook, user, organisationId, d, target);

                workbook.close();
            } catch (Exception excelException) {
                // Reset the stream and try as .xls
                try {
                    inputStream.reset();
                    Workbook workbook = new HSSFWorkbook(inputStream);
                    clientRequests = processIndividualExcelFile(workbook, user, organisationId, d, target);
                    workbook.close();
                } catch (Exception xlsException) {
                    // Reset and try as CSV
                    inputStream.reset();
                    clientRequests = processIndividualCsvFile(inputStream, user, organisationId, d, target);
                }
            }
        } catch (IOException e) {
            throw new Exception("Error reading file: " + e.getMessage(), e);
        }

        clientRequests.forEach(c -> {
            boolean isNew = StringUtils.isBlank(c.getId().toString());

            if (isNew) {

                // Generate a random token with letters, digits and special characters
                String token = RandomStringUtils
                        .secure()
                        .next(requestTokenLength, true, true);

                // Encode the token
                String encodedToken = passwordEncoder.encode(token);

                c.setAccountRequestToken(encodedToken);

                tokenMap.put(c.getTargetId(), token);
            }
        });

        clientRequests = clientRequestRepository.saveAll(clientRequests);

        if (target == TargetEntity.INDIVIDUAL) {

        } else if (target == TargetEntity.ORGANISATION) {

        }

        clientRequestNotification.queueEmailNotificationsForRequests(clientRequests, tokenMap, organisation);

        return findByTargetAndOrganisation(target, null, organisationId, 0, 10);
    }

    /**
     * Save individual entity and create client request
     */
    private ClientRequest saveIndividualAndRequest(Individual individual, String user, String organisationId,
            Document document, TargetEntity target) {
        // Save individual entity

        Individual savedIndividual = individualRepository.findByIdentityNoAndIdentityType(
                individual.getIdentityNo(), individual.getIdentityType())
                .orElseThrow(() -> new ClientRequestServiceException("The individual could not be found."));

        if (savedIndividual == null) {
            savedIndividual = individual;

            savedIndividual.setCreatedAt(LocalDateTime.now());
            savedIndividual.setCreatedBy(user);
            individual.setKycStatus(KycComplianceStatus.ABSENT);
        } else {
            savedIndividual.setModifiedAt(LocalDateTime.now());
            savedIndividual.setModifiedBy(user);
        }

        savedIndividual = individualRepository.save(savedIndividual);

        // Create client request
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setTarget(target);
        clientRequest.setTargetId(savedIndividual.getId().toString());

        // Set organisation
        // clientRequest.setOrganisationId(organisationId);

        // Set status and audit fields
        clientRequest.setStatus(ClientRequestStatus.PENDING);
        clientRequest.setCreatedBy(user);
        clientRequest.setCreatedAt(java.time.LocalDateTime.now());
        clientRequest.setDocument(document);

        return clientRequest;
    }

    /**
     * Process Excel file and extract Individual entities
     * Expected columns: First Name, Middle Name, Surname, Identity Type, Identity
     * No,
     * Email Address, Phone Numbers, Physical Address, Postal Address
     */
    private List<ClientRequest> processIndividualExcelFile(Workbook workbook, String user, String organisationId,
            Document document, TargetEntity target) {
        List<ClientRequest> clientRequests = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0); // Read first sheet
        Iterator<Row> rowIterator = sheet.iterator();

        // Skip header row if exists
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Individual individual = extractIndividualFromRow(row);
            if (individual != null) {
                ClientRequest request = saveIndividualAndRequest(individual, user, organisationId, document, target);
                clientRequests.add(request);
            }
        }

        return clientRequests;
    }

    /**
     * Process CSV file and extract Individual entities
     * Expected columns: First Name, Middle Name, Surname, Identity Type, Identity
     * No,
     * Email Address, Phone Numbers, Physical Address, Postal Address
     */
    private List<ClientRequest> processIndividualCsvFile(InputStream inputStream, String user, String organisationId,
            Document document, TargetEntity target)
            throws IOException {
        List<ClientRequest> clientRequests = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setIgnoreHeaderCase(true)
                        .setTrim(true)
                        .build())) {

            for (CSVRecord csvRecord : csvParser) {
                Individual individual = extractIndividualFromCsvRecord(csvRecord);
                if (individual != null) {
                    ClientRequest request = saveIndividualAndRequest(individual, user, organisationId, document,
                            target);

                    clientRequests.add(request);
                }
            }
        }

        return clientRequests;
    }

    /**
     * Extract Individual entity from Excel row
     * Expected columns: First Name, Middle Name, Surname, Identity Type, Identity
     * No,
     * Email Address, Phone Numbers, Physical Address, Postal Address
     */
    private Individual extractIndividualFromRow(Row row) {
        try {
            Individual individual = new Individual();

            // Convenience helper for reading string values
            Function<Integer, String> readString = col -> {
                Cell cell = row.getCell(col);
                return (cell != null && cell.getCellType() != CellType.BLANK)
                        ? getCellValueAsString(cell)
                        : null;
            };

            // Read simple fields
            individual.setFirstName(readString.apply(0)); // Mandatory
            individual.setMiddleName(readString.apply(1)); // Optional
            individual.setSurname(readString.apply(2)); // Mandatory

            // Identity Type (Mandatory)
            String identityTypeStr = readString.apply(3);
            if (identityTypeStr != null) {
                individual.setIdentityType(parseIndividualIdentityType(identityTypeStr));
            }

            // Identity No (Mandatory)
            individual.setIdentityNo(readString.apply(4));

            // Email (Optional)
            individual.setEmailAddress(readString.apply(5));

            // Phone numbers (Optional, comma-separated)
            String phoneNumbersStr = readString.apply(6);
            if (phoneNumbersStr != null && !phoneNumbersStr.trim().isEmpty()) {

                List<PhoneNumber> phoneList = Arrays.stream(phoneNumbersStr.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(phone -> {
                            PhoneNumber map = new PhoneNumber();
                            map.setType(PhoneType.OTHER);
                            map.setPhoneNumber(phone);
                            return map;
                        })
                        .collect(Collectors.toList());

                individual.setPhoneNumbers(phoneList);
            }

            // Physical & postal address
            individual.setPhysicalAddress(readString.apply(7)); // Optional
            individual.setPostalAddress(readString.apply(8)); // Optional

            // Validate mandatory fields
            if (individual.getFirstName() != null &&
                    individual.getSurname() != null &&
                    individual.getIdentityType() != null &&
                    individual.getIdentityNo() != null) {

                return individual;
            }

        } catch (Exception e) {
            System.err.println("Error processing row: " + e.getMessage());
        }

        return null; // Row skipped
    }

    /**
     * Extract Individual entity from CSV record
     * Expected columns: First Name, Middle Name, Surname, Identity Type, Identity
     * No,
     * Email Address, Phone Numbers, Physical Address, Postal Address
     */
    private Individual extractIndividualFromCsvRecord(CSVRecord csvRecord) {
        try {
            Individual individual = new Individual();

            // Column 0: First Name (Mandatory)
            String firstName = csvRecord.isMapped("First Name") ? csvRecord.get("First Name")
                    : (csvRecord.size() > 0 ? csvRecord.get(0) : null);
            if (firstName != null && !firstName.trim().isEmpty()) {
                individual.setFirstName(firstName.trim());
            }

            // Column 1: Middle Name (Optional)
            String middleName = csvRecord.isMapped("Middle Name") ? csvRecord.get("Middle Name")
                    : (csvRecord.size() > 1 ? csvRecord.get(1) : null);
            if (middleName != null && !middleName.trim().isEmpty()) {
                individual.setMiddleName(middleName.trim());
            }

            // Column 2: Surname (Mandatory)
            String surname = csvRecord.isMapped("Surname") ? csvRecord.get("Surname")
                    : (csvRecord.size() > 2 ? csvRecord.get(2) : null);
            if (surname != null && !surname.trim().isEmpty()) {
                individual.setSurname(surname.trim());
            }

            // Column 3: Identity Type (Mandatory)
            String identityTypeStr = csvRecord.isMapped("Identity Type") ? csvRecord.get("Identity Type")
                    : (csvRecord.size() > 3 ? csvRecord.get(3) : null);
            if (identityTypeStr != null && !identityTypeStr.trim().isEmpty()) {
                individual.setIdentityType(parseIndividualIdentityType(identityTypeStr));
            }

            // Column 4: Identity No (Mandatory)
            String identityNo = csvRecord.isMapped("Identity No") ? csvRecord.get("Identity No")
                    : (csvRecord.size() > 4 ? csvRecord.get(4) : null);
            if (identityNo != null && !identityNo.trim().isEmpty()) {
                individual.setIdentityNo(identityNo.trim());
            }

            // Column 5: Email Address (Optional)
            String email = csvRecord.isMapped("Email Address") ? csvRecord.get("Email Address")
                    : (csvRecord.size() > 5 ? csvRecord.get(5) : null);
            if (email != null && !email.trim().isEmpty()) {
                individual.setEmailAddress(email.trim());
            }

            // Column 6: Phone Numbers (Optional - comma-separated)
            String phoneNumbers = csvRecord.isMapped("Phone Numbers") ? csvRecord.get("Phone Numbers")
                    : (csvRecord.size() > 6 ? csvRecord.get(6) : null);
            if (phoneNumbers != null && !phoneNumbers.trim().isEmpty()) {
                @SuppressWarnings({ "rawtypes", "unchecked" })
                List phoneList = new ArrayList<>();
                String[] phones = phoneNumbers.split(",");
                for (String phone : phones) {
                    java.util.Map<String, String> phoneMap = new java.util.HashMap<>();
                    phoneMap.put("number", phone.trim());
                    phoneList.add(phoneMap);
                }
                individual.setPhoneNumbers(phoneList);
            }

            // Column 7: Physical Address (Optional)
            String physicalAddress = csvRecord.isMapped("Physical Address") ? csvRecord.get("Physical Address")
                    : (csvRecord.size() > 7 ? csvRecord.get(7) : null);
            if (physicalAddress != null && !physicalAddress.trim().isEmpty()) {
                individual.setPhysicalAddress(physicalAddress.trim());
            }

            // Column 8: Postal Address (Optional)
            String postalAddress = csvRecord.isMapped("Postal Address") ? csvRecord.get("Postal Address")
                    : (csvRecord.size() > 8 ? csvRecord.get(8) : null);
            if (postalAddress != null && !postalAddress.trim().isEmpty()) {
                individual.setPostalAddress(postalAddress.trim());
            }

            // Column 9: Sex (Mandatory)
            String sex = csvRecord.isMapped("Sex") ? csvRecord.get("Sex")
                    : (csvRecord.size() > 9 ? csvRecord.get(9) : null);
            if (sex != null && !sex.trim().isEmpty()) {
                individual.setSex(parseSex(sex));
            }

            // Validate mandatory fields
            if (individual.getFirstName() != null && individual.getSurname() != null
                    && individual.getIdentityNo() != null && individual.getIdentityType() != null) {
                return individual;
            }

        } catch (Exception e) {
            // Log and skip invalid records
            System.err.println("Error processing CSV record: " + e.getMessage());
        }

        return null;
    }

    /**
     * Get cell value as string regardless of cell type
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Check if it's a date or number
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * Parse identity type string to IndividualIdentityType enum
     */
    private IndividualIdentityType parseIndividualIdentityType(String identityTypeStr) {
        if (identityTypeStr == null || identityTypeStr.trim().isEmpty()) {
            return IndividualIdentityType.OMANG; // Default
        }

        try {
            return IndividualIdentityType.fromString(identityTypeStr.trim().toUpperCase());
        } catch (Exception e) {
            return IndividualIdentityType.OMANG; // Default fallback
        }
    }

    private Sex parseSex(String sexStr) {
        if (sexStr == null || sexStr.trim().isEmpty()) {
            throw new ClientRequestServiceException("Sex must be specified");
        }

        try {

            if ("M".equalsIgnoreCase(sexStr.trim())) {

                return Sex.MALE;
            } else if ("F".equalsIgnoreCase(sexStr.trim())) {

                return Sex.FEMALE;
            }

            return Sex.fromString(sexStr.trim().toUpperCase());
        } catch (Exception e) {
            throw new ClientRequestServiceException("Invalid sex value: " + sexStr);
        }
    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#findByIndividual(String)
     */
    @Override
    protected List<ClientRequestDTO> handleFindByIndividual(String individualId)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTargetId(individualId);
        criteria.setTarget(TargetEntity.INDIVIDUAL);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        List<ClientRequest> requests = clientRequestRepository.findAll(spec);

        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    private void updateClientName(List<ClientRequestDTO> reuests) {

        Map<String, String> individualNameMap = new HashMap<>();
        Map<String, String> individualIdentityNo = new HashMap<>();
        Map<String, String> organisationNameMap = new HashMap<>();
        Map<String, String> organisationRegistration = new HashMap<>();

        reuests.forEach(r -> {
            if (r.getTarget() == TargetEntity.INDIVIDUAL) {

                String name = individualNameMap.get(r.getTargetId());
                String identityNo = individualIdentityNo.get(r.getTargetId());

                if(!individualNameMap.containsKey(r.getTargetId())) {

                    Individual individual = individualRepository.findById(UUID.fromString(r.getTargetId()))
                            .orElse(null);

                    if (individual != null) {
                        name = individual.getFirstName() + " " + individual.getSurname();
                        identityNo = individual.getIdentityNo();
                    } else {
                        name = "Unknown Individual";
                        identityNo = "Unknown Identity No";
                    }

                    individualNameMap.put(r.getTargetId(), name);
                    individualIdentityNo.put(r.getTargetId(), identityNo);
                }

                r.setName(name);
                r.setRegistration(identityNo);

            } else if (r.getTarget() == TargetEntity.ORGANISATION) {
                
                String name = organisationNameMap.get(r.getTargetId());
                String registrationNo = organisationRegistration.get(r.getTargetId());

                if(!organisationNameMap.containsKey(r.getTargetId())) {

                    Organisation organisation = organisationRepository.findById(UUID.fromString(r.getTargetId()))
                            .orElse(null);

                    if (organisation != null) {
                        name = organisation.getName();
                        registrationNo = organisation.getRegistrationNo();
                    } else {
                        name = "Unknown Organisation";
                        registrationNo = "Unknown Registration No";
                    }

                    organisationNameMap.put(r.getTargetId(), name);
                    organisationRegistration.put(r.getTargetId(), registrationNo);
                }

                r.setName(name);
                r.setRegistration(registrationNo);
            }
        });

    }

    /**
     * @see bw.co.centralkyc.organisation.client.ClientRequestService#findByIndividual(String,
     *      Integer, Integer)
     */
    @Override
    protected Page<ClientRequestDTO> handleFindByIndividual(String individualId, Integer pageNumber, Integer pageSize)
            throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTargetId(individualId);
        criteria.setTarget(TargetEntity.INDIVIDUAL);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);

        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected List<ClientRequestDTO> handleFindByDocument(String documentId) throws Exception {

        return null; // clientRequestRepository.findByDocumentId(documentId);
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByDocument(String documentId, Integer pageNumber, Integer pageSize)
            throws Exception {
        return null; // clientRequestRepository.findByDocumentId(documentId,
                     // PageRequest.of(pageNumber, pageSize));
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByStatus(ClientRequestStatus status, Integer pageNumber,
            Integer pageSize) throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.equal(root.get("status"), status);

        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected List<ClientRequestDTO> handleFindByTarget(TargetEntity target, String targetId) throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("target"), target),
                cb.equal(root.get("targetId"), targetId));

        List<ClientRequest> requests = clientRequestRepository.findAll(spec);
        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByTarget(TargetEntity target, String targetId, Integer pageNumber,
            Integer pageSize) throws Exception {

        Specification<ClientRequest> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("target"), target),
                cb.equal(root.get("targetId"), targetId));

        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected List<ClientRequestDTO> handleFindByTargetAndOrganisation(TargetEntity target, String targetId,
            String organisationId) throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTarget(target);
        criteria.setTargetId(targetId);
        criteria.setOrganisationId(organisationId);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        List<ClientRequest> requests = clientRequestRepository.findAll(spec);
        List<ClientRequestDTO> dtos = clientRequestMapper.toClientRequestDTOCollection(requests);
        this.updateClientName(dtos);

        return dtos;
    }

    @Override
    protected Page<ClientRequestDTO> handleFindByTargetAndOrganisation(TargetEntity target, String targetId,
            String organisationId, Integer pageNumber, Integer pageSize) throws Exception {

        ClientRequestSearchCriteria criteria = new ClientRequestSearchCriteria();
        criteria.setTarget(target);
        criteria.setTargetId(targetId);
        criteria.setOrganisationId(organisationId);

        Specification<ClientRequest> spec = this.buildSpecificationFromCriteria(criteria);
        Page<ClientRequest> requests = clientRequestRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        Page<ClientRequestDTO> dtoPage = requests.map(clientRequestMapper::toClientRequestDTO);
        this.updateClientName(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    protected ClientRequestDTO handleUpdateStatus(String id, ClientRequestStatus status) throws Exception {

        ClientRequest clientRequest = clientRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        clientRequest.setStatus(status);
        clientRequest = clientRequestRepository.save(clientRequest);

        if (status == ClientRequestStatus.ACCEPTED) {
            // Additional actions on approval can be handled here
        }

        return clientRequestMapper.toClientRequestDTO(clientRequest);
    }

    @Override
    protected String handleConfirmToken(String requestId, String token) throws Exception {

        ClientRequest clientRequest = clientRequestRepository.findById(UUID.fromString(requestId))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        boolean matches = passwordEncoder.matches(token, clientRequest.getAccountRequestToken());

        if (!matches) {
            throw new ClientRequestServiceException("Invalid confirmation token");
        }

        String confirmationToken = RandomStringUtils
                .secure()
                .next(requestTokenLength, true, true);

        // Encode the token
        String encodedToken = passwordEncoder.encode(confirmationToken);

        clientRequest.setIdentityConfirmationToken(encodedToken);

        String registrationToken = RandomStringUtils
                .secure()
                .next(requestTokenLength, true, true);

        // Encode the token
        String encodedRegistrationToken = passwordEncoder.encode(registrationToken);

        clientRequest.setRegistrationToken(encodedRegistrationToken);

        clientRequestRepository.save(clientRequest);

        return String.format("%s|%s", confirmationToken, registrationToken);
    }

    @Override
    protected Long handleCountByStatus(ClientRequestStatus status) throws Exception {

        return clientRequestRepository.countByStatus(status).orElse(0L);
    }

    @Override
    protected Long handleCountByStatusAndOrganisationId(ClientRequestStatus status, String organisationId)
            throws Exception {

        return clientRequestRepository.countByStatusAndOrganisationId(status, organisationId).orElse(0L);
    }

    @Override
    protected Long handleCount() throws Exception {

        return clientRequestRepository.count();
    }

    @Override
    protected boolean handleConfirmRegistration(String id, Boolean confirm, String registrationToken) throws Exception {

        ClientRequest request = clientRequestRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ClientRequestServiceException("ClientRequest not found"));

        boolean matches = passwordEncoder.matches(registrationToken, request.getRegistrationToken());

        if (!matches) {
            throw new ClientRequestServiceException("Invalid registration token");
        }

        if (request.getStatus() == ClientRequestStatus.ACCEPTED) {

            throw new ClientRequestServiceException("ClientRequest already confirmed");
        }

        if (confirm) {
            request.setStatus(ClientRequestStatus.ACCEPTED);
        } else {
            request.setStatus(ClientRequestStatus.REJECTED);
        }

        request = clientRequestRepository.save(request);

        return request.getStatus() == ClientRequestStatus.ACCEPTED;
    }

    @Override
    protected ClientRequestDTO handleFindUserReadyRequests() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindUserReadyRequests'");
    }

    @Override
    protected ClientRequestDTO handleFindUserReadyRequests(Integer pageNumber, Integer pageSize) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleFindUserReadyRequests'");
    }

}