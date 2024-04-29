package com.example.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.random.RandomGenerator;

public class RandomData {

    private static final int LENGTH = 10;

    public static String getString(){
        return "test_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    public static int getInt(int origin, int bound){
        return RandomGenerator.getDefault().nextInt(origin, bound);
    }

    public static Boolean getBoolean(){
        return RandomGenerator.getDefault().nextBoolean();
    }
}
