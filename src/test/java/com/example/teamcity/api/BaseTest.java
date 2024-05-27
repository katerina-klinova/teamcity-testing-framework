package com.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataCleanUp;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.requests.checked.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.ITestContext;

import java.util.ArrayList;

public class BaseTest {

    protected SoftAssertions softly;

    public TestDataStorage testDataStorage;
    public ArrayList<String> testDataCleanUp;

    public RequestSpecification superUserSpec = Specifications.getSpec().getSuperUserSpec();
    public RequestSpecification unauthUserSpec = Specifications.getSpec().getUnauthenticatedSpec();

    public CheckedRequests checkedWithSuperUser
            = new CheckedRequests(Specifications.getSpec().getSuperUserSpec());

    public UncheckedRequests uncheckedWithSuperUser
            = new UncheckedRequests(Specifications.getSpec().getSuperUserSpec());

    @BeforeSuite
    public void beforeSuite(ITestContext context){
        String[] includedGroups = context.getIncludedGroups();
        for (String group : includedGroups) {
            if (!group.equals("setup")) {
                new SetupSuite().setServerAuthSettings();
            }
        }
    }

    @BeforeMethod
    public void beforeTest(){
        softly = new SoftAssertions();
        testDataStorage = TestDataStorage.getStorage();
        testDataCleanUp = TestDataCleanUp.getTestDataUsed();
    }

    @AfterMethod
    public void afterTest(){
        testDataStorage.delete();
        softly.assertAll();
    }
}
