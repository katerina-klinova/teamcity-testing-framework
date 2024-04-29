package com.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.requests.checked.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseApiTest extends BaseTest{

    public TestDataStorage testDataStorage;

    public CheckedRequests checkedWithSuperUser
            = new CheckedRequests(Specifications.getSpec().getSuperUserSpec());

    public UncheckedRequests uncheckedWithSuperUser
            = new UncheckedRequests(Specifications.getSpec().getSuperUserSpec());

    @BeforeSuite
    public void beforeSuite(){
        new SetupSuite().setServerAuthSettings();
    }

    @BeforeMethod
    public void setupTest(){
        testDataStorage = TestDataStorage.getStorage();
    }

    @AfterTest
    public void cleanTest(){
        testDataStorage.delete();
    }
}
