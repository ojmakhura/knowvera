package bw.co.centralkyc.utils;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KycUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "@#$%!";

    @Value("${app.security.password.min-length}")
    private int minPasswordLength;

    /**
     * 
     * Generate random password
     * 
     * @return
     */
    public String generatePassword() {
        if (minPasswordLength < 8) {
            throw new IllegalArgumentException("Password length must be at least 8");
        }

        List<String> groups = List.of(UPPER, LOWER, DIGITS, SYMBOLS);
        String all = UPPER + LOWER + DIGITS + SYMBOLS;

        StringBuilder password = new StringBuilder();

        // Ensure at least one char from each group
        for (String group : groups) {
            password.append(group.charAt(RANDOM.nextInt(group.length())));
        }

        // Fill remaining chars
        for (int i = password.length(); i < minPasswordLength; i++) {
            password.append(all.charAt(RANDOM.nextInt(all.length())));
        }

        return password.toString();
    }
}
