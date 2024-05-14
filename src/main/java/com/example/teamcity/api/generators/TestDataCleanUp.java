package com.example.teamcity.api.generators;

import java.util.ArrayList;

public class TestDataCleanUp {
    private static ArrayList<String> testDataUsed = new ArrayList<>();

    public static ArrayList<String> getTestDataUsed(){
        return testDataUsed;
    }

    public boolean contains(String element) {
        return testDataUsed.contains(element);
    }

    public void clear() {
        testDataUsed.clear();
    }
}