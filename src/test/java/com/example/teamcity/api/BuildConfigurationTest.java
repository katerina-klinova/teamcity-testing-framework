package com.example.teamcity.api;

import com.example.teamcity.api.requests.checked.CheckedProjectRequest;
import com.example.teamcity.api.requests.checked.CheckedUserRequest;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest{
    @Test
    public void buildConfigurationTest(){
        var testData = testDataStorage.addTestData();

        new CheckedUserRequest(Specifications.getSpec().getSuperUserSpec())
                .create(testData.getUser());

        var project = new CheckedProjectRequest(Specifications.getSpec().getAuthenticatedSpec(testData.getUser()))
                .create(testData.getProject());

        softly.assertThat(project.getId())
                .isEqualTo(testData.getProject().getId());
    }
}
