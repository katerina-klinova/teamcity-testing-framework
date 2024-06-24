package com.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomData {

    private static final int LENGTH = 10;
    private static final String TEST_PREFIX = "Test";

    private RandomData() {
    }

    public static String getString() {
        return TEST_PREFIX + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    public static String getString(int length) {
        // Generates String of custom length considering the number of characters allocated for the prefix
        return TEST_PREFIX + RandomStringUtils.randomAlphabetic(Math.max(length - TEST_PREFIX.length(), LENGTH));
    }

}
