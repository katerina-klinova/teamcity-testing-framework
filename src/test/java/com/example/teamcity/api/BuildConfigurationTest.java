package com.example.teamcity.api;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedProjectRequest;
import com.example.teamcity.api.requests.checked.CheckedRequestGenerator;
import com.example.teamcity.api.requests.checked.CheckedUserRequest;
import com.example.teamcity.api.requests.unchecked.UncheckedRequestGenerator;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
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

    //1. create a build with required values provided
    @Test
    public void buildConfigShouldGetCreatedWithRequiredValuesProvided(){
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest()
                .create(testData.getProject());
        testDataCleanUp.add(testData.getProject().getId());

        var buildConfig = checkedWithSuperUser.getBuildConfigRequest()
                .create(testData.getBuildType());

        softly.assertThat(buildConfig.getId())
                .isEqualTo(testData.getBuildType().getId());
    }

    //2. try creating a build with Id missing
    //Example of generic unchecked and checked requests generator
    @Test
    public void buildConfigShouldNotGetCreatedHavingBuildIdEmpty(){
        var testData = testDataStorage.addTestData();

        new CheckedRequestGenerator<Project>(superUserSpec, NewProjectDescription.class, Project.class)
                .create(testData.getProject());

        testData.getBuildType().setId("");

        new UncheckedRequestGenerator<BuildType>(superUserSpec, BuildConfigurationTest.class)
                .create(testData.getBuildType())
                .then().assertThat()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString("Build configuration or template ID must not be empty."));
    }

    //3. try creating a build with Name missing
    @Test
    public void buildConfigShouldNotGetCreatedHavingNameEmpty(){
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest()
                .create(testData.getProject());
        testDataCleanUp.add(testData.getProject().getId());

        testData.getBuildType().setName("");

        uncheckedWithSuperUser.getBuildConfigRequest()
                .create(testData.getBuildType())
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("When creating a build type, non empty name should be provided."));
    }

    //4. try creating a build reusing an existing Id
    @Test
    public void buildConfigShouldNotGetCreatedHavingExistingId(){
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest()
                .create(firstTestData.getProject());
        testDataCleanUp.add(firstTestData.getProject().getId());

        checkedWithSuperUser.getBuildConfigRequest()
                .create(firstTestData.getBuildType());

        secondTestData.getBuildType().setId(firstTestData.getBuildType().getId());
        secondTestData.getBuildType().setProject(firstTestData.getProject());

        uncheckedWithSuperUser.getBuildConfigRequest()
                .create(secondTestData.getBuildType())
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(
                        "The build configuration / template ID \"" + firstTestData.getBuildType().getId()
                                + "\" is already used by another configuration or template"));
    }

    //5. try creating a build reusing an existing name
    @Test
    public void buildConfigShouldNotGetCreatedHavingExistingName() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest()
                .create(firstTestData.getProject());
        testDataCleanUp.add(firstTestData.getProject().getId());

        checkedWithSuperUser.getBuildConfigRequest()
                .create(firstTestData.getBuildType());

        secondTestData.getBuildType().setName(firstTestData.getBuildType().getName());
        secondTestData.getBuildType().setProject(firstTestData.getProject());

        uncheckedWithSuperUser.getBuildConfigRequest()
                .create(secondTestData.getBuildType())
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString((String.format(
                        "buildServer.serverSide.DuplicateBuildTypeNameException: Build configuration with " +
                                "name \"%s\" already exists in project: \"%s\"",
                        firstTestData.getBuildType().getName(), firstTestData.getProject().getName()
                ))));
    }

    //6. try creating a build using a not existing project Id
    @Test
    public void buildConfigShouldNotGetCreatedHavingProjectNonExistent(){
        var testData = testDataStorage.addTestData();

        uncheckedWithSuperUser.getBuildConfigRequest()
                .create(testData.getBuildType())
                .then().assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString(
                        "Project cannot be found by external id '"
                                + testData.getBuildType().getProject().getId()));
    }
}
