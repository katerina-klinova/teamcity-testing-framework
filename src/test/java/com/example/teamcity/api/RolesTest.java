package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.CheckedBuildConfigRequest;
import com.example.teamcity.api.requests.checked.CheckedProjectRequest;
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
        testDataCleanUp.add(testData.getUser().getUsername());

        var project = new CheckedProjectRequest(Specifications.getSpec().getAuthenticatedSpec(testData.getUser()))
                .create(testData.getProject());
        testDataCleanUp.add(project.getId());

        softly.assertThat(project.getId())
                .isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectAdminShouldHaveRightsToCreateBuildConfigToHisProject(){
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest()
                .create(testData.getProject());
        testDataCleanUp.add(testData.getProject().getId());

        testData.getUser().setRoles(
                TestDataGenerator.generateRole(Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedWithSuperUser.getUserRequest()
                .create(testData.getUser());
        testDataCleanUp.add(testData.getUser().getUsername());

        var buildConfig = new CheckedBuildConfigRequest(Specifications.getSpec().getAuthenticatedSpec(testData.getUser()))
                .create(testData.getBuildType());

        softly.assertThat(buildConfig.getId())
                .isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void projectAdminShouldNotHaveRightsToCreateBuildConfigToAnotherProject() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        testDataCleanUp.add(firstTestData.getProject().getId());

        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());
        testDataCleanUp.add(secondTestData.getProject().getId());

        firstTestData.getUser().setRoles(TestDataGenerator.
                generateRole(Role.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());
        testDataCleanUp.add(firstTestData.getUser().getUsername());

        secondTestData.getUser().setRoles(TestDataGenerator.
                generateRole(Role.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest().create(secondTestData.getUser());
        testDataCleanUp.add(secondTestData.getUser().getUsername());

        new UncheckedBuildConfigRequest(Specifications.getSpec().getAuthenticatedSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString(
                        "You do not have enough permissions to access project with internal id: project"));
    }
}
