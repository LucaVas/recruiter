package dev.lucavassos.recruiter.testUtils;

import java.util.Random;

public class RandomUtils {

    public static final String ALPHABEET_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String ALPHABET_UPPER_CASE = ALPHABEET_LOWER_CASE.toUpperCase();
    public static final String NUMERIC = "0123456789";
    public static final String LOWER_CASE_ALPHA_NUMERIC = ALPHABEET_LOWER_CASE + NUMERIC;
    public static final String ALPHA_NUMERIC = ALPHABEET_LOWER_CASE + ALPHABET_UPPER_CASE + NUMERIC;
    private static final Random RANDOM = new Random();

    public static String randomPhoneNumber() {
        return randomNumericString(5, 10);
    }

    private static String randomNumericString(int minLength, int maxLength) {
        return randomString(minLength, maxLength, NUMERIC);
    }

    public static String randomEmail() {
        return randomLowerCaseString(1, 5)
                + "@"
                + randomLowerCaseString(1, 3)
                + "."
                + randomLowerCaseString(2, 3);
    }

    public static String randomLowerCaseString(int minLength, int maxLength) {
        return randomString(minLength, maxLength, LOWER_CASE_ALPHA_NUMERIC);
    }

    public static String randomString(int minLength, int maxLength) {
        return randomString(minLength, maxLength, ALPHA_NUMERIC);
    }

    private static String randomString(int minLength, int maxLength, String symbols) {
        int actualLength = minLength;
        if (minLength < maxLength) {
            actualLength += RANDOM.nextInt(maxLength - minLength + 1);
        }
        char[] chars = symbols.toCharArray();
        char[] value = new char[actualLength];
        for (int i = 0; i < actualLength; i++) {
            value[i] = chars[RANDOM.nextInt(chars.length)];
        }
        return new String(value);
    }

    public static Boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    public static Integer randomNumber(int min, int max) { return RANDOM.nextInt(min, max); }

}
