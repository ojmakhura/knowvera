package bw.co.roguesystems.ai.schema;

public class SchemaNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SchemaNotFoundException(String code) {
        super("No schema with code '" + code + "'");
    }

    public SchemaNotFoundException(String code, int version) {
        super("No version " + version + " of schema '" + code + "'");
    }
}
