package dev.lucavassos.recruiter.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHA_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-+=";

    public static String generateRandomPassword() {
        return generateRandomString(64);
    }

    private static String generateRandomString(int length) {
        char[] possibleCharacters = (
                ALPHA_LOWERCASE + ALPHA_UPPERCASE + NUMERIC + SYMBOLS
        ).toCharArray();

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .selectFrom(possibleCharacters)
                .usingRandom(RANDOM::nextInt)
                .get();
        return generator.generate(length);
    }
}
