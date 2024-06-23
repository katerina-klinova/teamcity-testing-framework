package com.teamcity;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.EnumMap;

import static com.teamcity.api.generators.TestDataGenerator.generate;

public class BaseTest {

    protected final CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected final UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
    protected EnumMap<Endpoint, BaseModel> testData;
    protected SoftAssertions softly;

    @BeforeMethod(alwaysRun = true)
    public void generateBaseTestData() {
        softly = new SoftAssertions();
        // Generates one testData set before each test runs without adding it to any storage
        testData = generate();
    }

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedEntities() {
        TestDataStorage.getStorage().deleteCreatedEntities();
        softly.assertAll();
    }

}
