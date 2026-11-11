package bw.co.roguesystems.ai.profile;

public class ProfileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProfileNotFoundException(String code) {
        super("No processing profile with code '" + code + "'");
    }

    public ProfileNotFoundException(String code, int version) {
        super("No version " + version + " of processing profile '" + code + "'");
    }
}
