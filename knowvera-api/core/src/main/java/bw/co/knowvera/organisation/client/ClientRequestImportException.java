package bw.co.knowvera.organisation.client;

import java.util.List;

/**
 * A client request spreadsheet that was refused. An import is all-or-nothing: when any row is
 * wrong nothing is stored, and {@link #getRowErrors()} names every wrong row ("Row 4: ...") so the
 * file can be corrected and uploaded again.
 */
public class ClientRequestImportException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final List<String> rowErrors;

    /** The file as a whole cannot be imported (unreadable, no rows, missing columns). */
    public ClientRequestImportException(String message) {
        this(message, List.of());
    }

    public ClientRequestImportException(String message, List<String> rowErrors) {
        super(message);
        this.rowErrors = List.copyOf(rowErrors);
    }

    /** One message per wrong row, in file order; empty when the file as a whole was refused. */
    public List<String> getRowErrors() {
        return rowErrors;
    }
}
