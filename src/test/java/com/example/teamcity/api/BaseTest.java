package com.example.teamcity.api;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected SoftAssertions softly;

    @BeforeMethod
    public void beforeTest(){
        softly = new SoftAssertions();
    }

    @AfterMethod
    public void afterTest(){
        softly.assertAll();
    }
}
