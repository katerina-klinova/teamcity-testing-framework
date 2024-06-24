package com.teamcity.api;

import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.Project;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.PROJECTS;
import static com.teamcity.api.enums.Endpoint.USERS;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Project")
public class ProjectTest extends BaseApiTest {

    private static final int PROJECT_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create project", groups = {"Regression"})
    public void userCreatesProjectTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        var checkedProjectRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        var project = (Project) checkedProjectRequest.create(testData.get(PROJECTS));

        softly.assertThat(project.getId()).as("projectId").isEqualTo(((NewProjectDescription) testData.get(PROJECTS)).getId());
    }

    @Test(description = "User should not be able to create two projects with the same id", groups = {"Regression"})
    public void userCreatesTwoProjectsWithSameIdTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        var checkedProjectRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        checkedProjectRequest.create(testData.get(PROJECTS));

        var secondTestData = generate();
        var projectTestData = (NewProjectDescription) testData.get(PROJECTS);
        var secondProjectTestData = (NewProjectDescription) secondTestData.get(PROJECTS);
        secondProjectTestData.setId(projectTestData.getId());

        var uncheckedProjectRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        uncheckedProjectRequest.create(secondTestData.get(PROJECTS))
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create project with id exceeding the limit", groups = {"Regression"})
    public void userCreatesProjectWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        var projectTestData = (NewProjectDescription) testData.get(PROJECTS);
        projectTestData.setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT + 1));

        var uncheckedProjectRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        uncheckedProjectRequest.create(testData.get(PROJECTS))
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        projectTestData.setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT));

        var checkedProjectRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        checkedProjectRequest.create(testData.get(PROJECTS));
    }

    @Test(description = "Unauthorized user should not be able to create project", groups = {"Regression"})
    public void unauthorizedUserCreatesProjectTest() {
        var uncheckedProjectRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), PROJECTS);
        uncheckedProjectRequest.create(testData.get(PROJECTS))
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(PROJECTS)
                .read(((NewProjectDescription) testData.get(PROJECTS)).getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete project", groups = {"Regression"})
    public void userDeletesProjectTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        var checkedProjectRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        checkedProjectRequest.create(testData.get(PROJECTS));
        checkedProjectRequest.delete(((NewProjectDescription) testData.get(PROJECTS)).getId());

        var uncheckedProjectRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        uncheckedProjectRequest.read(((NewProjectDescription) testData.get(PROJECTS)).getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

}
