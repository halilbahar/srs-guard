package at.htl.srsguard.util;

import java.security.SecureRandom;

public final class ApiKeyGenerator {
    private static final char[] VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456879".toCharArray();

    private ApiKeyGenerator() {
    }

    public static String generateKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        char[] buff = new char[length];
        for (int i = 0; i < length; ++i) {
            if (i % 10 == 0) {
                secureRandom.setSeed(secureRandom.nextLong());
            }
            buff[i] = VALID_CHARACTERS[secureRandom.nextInt(VALID_CHARACTERS.length)];
        }
        return new String(buff);
    }

    public static String generateKey() {
        return ApiKeyGenerator.generateKey(32);
    }
}
