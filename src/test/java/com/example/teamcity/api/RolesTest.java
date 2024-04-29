package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.CheckedBuildConfigRequest;
import com.example.teamcity.api.requests.checked.CheckedProjectRequest;
import com.example.teamcity.api.requests.checked.CheckedUserRequest;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfigRequest;
import com.example.teamcity.api.requests.unchecked.UncheckedProjectRequest;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class RolesTest extends BaseApiTest{

    @Test
    public void unauthorizedUserShouldNotHaveRightToCreateProject(){
        var testData = testDataStorage.addTestData();

        new UncheckedProjectRequest(Specifications.getSpec().getUnauthenticatedSpec())
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
//                .body(Matchers.containsString("Authentication required"));

        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:"
                        + testData.getProject().getId() + "'."));
    }

    @Test
    public void systemAdminShouldHaveRightsToCreateProject(){
        var testData = testDataStorage.addTestData();

        testData.getUser().setRoles(TestDataGenerator.generateRole(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var project = new CheckedProjectRequest(Specifications.getSpec().getAuthenticatedSpec(testData.getUser()))
                .create(testData.getProject());

        softly.assertThat(project.getId())
                .isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectAdminShouldHaveRightsToCreateBuildConfigToHisProject(){
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest()
                .create(testData.getProject());

        testData.getUser().setRoles(
                TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());

        var buildConfig = new CheckedBuildConfigRequest(Specifications.getSpec().getAuthenticatedSpec(testData.getUser()))
                .create(testData.getBuildType());

        softly.assertThat(buildConfig.getId())
                .isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void projectAdminShouldNotHaveRightsToCreateBuildConfigToAnotherProject() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        var firstUserRequest = new CheckedUserRequest(
                Specifications.getSpec().getAuthenticatedSpec(firstTestData.getUser()));
        var secondUserRequest = new CheckedUserRequest(
                Specifications.getSpec().getAuthenticatedSpec(secondTestData.getUser()));

        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());

        firstTestData.getUser().setRoles(TestDataGenerator.
                generateRole(Role.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());

        secondTestData.getUser().setRoles(TestDataGenerator.
                generateRole(Role.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest()
                .create(secondTestData.getUser());

        new UncheckedBuildConfigRequest(Specifications.getSpec().getAuthenticatedSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);

//        softly.assertThat(buildConfig.getId())
//                .isEqualTo(firstTestData.getBuildType().getId());
    }
}
