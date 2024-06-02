package com.example.teamcity.api;

import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

public class BaseApiTest extends BaseTest{
    @BeforeSuite
    public void beforeSuite(ITestContext context){
                new SetupSuite().setServerAuthSettings();
    }
}
