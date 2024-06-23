package com.teamcity.api;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.Roles;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Build type")
public class BuildConfigurationTest extends BaseApiTest {

    private static final int BUILD_TYPE_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create build type", groups = {"Regression"})
    public void buildConfigShouldGetCreatedWithRequiredValuesProvided() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));

        softly.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(((BuildType) testData.get(BUILD_TYPES)).getId());
    }

    //2. try creating a build with Id missing
    @Test(description = "User should not be able to create a build when Id is empty", groups = {"Regression"})
    public void buildConfigShouldNotGetCreatedHavingBuildIdEmpty(){
        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);
        buildTypeData.setId("");

        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES)
                .create(buildTypeData)
                .then().assertThat()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body(Matchers.containsString(
                            "Build configuration or template ID must not be empty."
                    ));
    }

    //3. try creating a build with Name missing
    @Test(description = "User should not be able to create a build when Name is empty", groups = {"Regression"})
    public void buildConfigShouldNotGetCreatedHavingNameEmpty(){
        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);
        buildTypeData.setName("");


        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES)
                .create(buildTypeData)
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(
                        "When creating a build type, non empty name should be provided."
                ));
    }

    //4. try creating a build reusing an existing Id
    @Test(description = "User should not be able to create two build types with the same Id", groups = {"Regression"})
    public void buildConfigShouldNotGetCreatedHavingExistingId() {
        var userData = (User) testData.get(USERS);
        var buildTypeDataSet1 = (BuildType) testData.get(BUILD_TYPES);

        checkedSuperUser.getRequest(USERS).create(userData);
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(userData), BUILD_TYPES);
        checkedBuildTypeRequest.create(buildTypeDataSet1);

        var secondTestData = generate();
        var buildTypeDataSet2 = (BuildType) secondTestData.get(BUILD_TYPES);
        buildTypeDataSet2.setId(buildTypeDataSet1.getId());
        buildTypeDataSet2.setProject(buildTypeDataSet1.getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(userData), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(String.format(
                        "The build configuration / template ID \"%s\" is already used by another configuration or template"
                        , buildTypeDataSet1.getId()
                )));
    }

    //5. try creating a build reusing an existing name
    @Test(description = "User should not be able to create a build with existing Name", groups = {"Regression"})
    public void buildConfigShouldNotGetCreatedHavingExistingName() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));

        var testData2 = generate();
        var buildTypeData1 = (BuildType) testData.get(BUILD_TYPES);
        var buildTypeData2 = (BuildType) testData2.get(BUILD_TYPES);
        buildTypeData2.setId(buildTypeData1.getId());
        buildTypeData2.setProject(buildTypeData1.getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData2.get(BUILD_TYPES))
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body(Matchers.containsString(String.format(
                        "Details: jetbrains.buildServer.serverSide.identifiers.DuplicateExternalIdException: "
                        + "The build configuration / template ID \"%s\" is already used by another configuration or template"
                        , buildTypeData1.getId()
                )));
    }

//6. try creating a build using a not existing project Id
    @Test(description = "User should not be able to create a build when parent project does not exist"
            , groups = {"Regression"})
    public void buildConfigShouldNotGetCreatedHavingProjectNonExistent(){
        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);

        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES)
                .create(buildTypeData)
                .then().assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString(String.format(
                        "Details: jetbrains.buildServer.server.rest.errors.NotFoundException: " +
                                "No project found by locator 'count:1,id:%s'. " +
                                "Project cannot be found by external id '%s'"
                                , buildTypeData.getProject().getId(), buildTypeData.getProject().getId()
                )));
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit", groups = {"Regression"})
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        var userData = (User) testData.get(USERS);
        checkedSuperUser.getRequest(USERS).create(userData);
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);
        buildTypeData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(userData), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        buildTypeData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(userData), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));
    }

    @Test(description = "Unauthorized user should not be able to create build type", groups = {"Regression"})
    public void unauthorizedUserCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(BUILD_TYPES).read(((BuildType) testData.get(BUILD_TYPES)).getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete build type", groups = {"Regression"})
    public void userDeletesBuildTypeTest() {
        var userData = (User) testData.get(USERS);

        checkedSuperUser.getRequest(USERS).create(userData);
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(userData), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));
        checkedBuildTypeRequest.delete(((BuildType) testData.get(BUILD_TYPES)).getId());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(userData), BUILD_TYPES);
        uncheckedBuildTypeRequest.read(((BuildType) testData.get(BUILD_TYPES)).getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var testData2 = generate();
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));
        checkedSuperUser.getRequest(PROJECTS).create(testData2.get(PROJECTS));

        var user1TestData = (User) testData.get(USERS);
        var user2TestData = (User) testData2.get(USERS);
        var project1Data = (NewProjectDescription) testData.get(PROJECTS);
        var project2TestData = (NewProjectDescription) testData2.get(PROJECTS);
        user1TestData.setRoles((Roles) generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + project1Data.getId()));
        user2TestData.setRoles((Roles) generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + project2TestData.getId()));

        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(USERS).create(testData2.get(USERS));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData2.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }
}
