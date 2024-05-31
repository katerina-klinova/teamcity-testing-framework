package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedRequestGenerator;
import com.example.teamcity.api.requests.unchecked.UncheckedRequestGenerator;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.time.Duration;

public class CreateNewProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleToCreateNewProjectFromRepository() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/AlexPshe/spring-core-for-qa";
        var projectId = testData.getProject().getId();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProjectFromRepo(testData.getProject().getId(), testData.getBuildType().getName());

        //UI check that project exists
        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(projectId));

        //API check that project exists
        new CheckedRequestGenerator<Project>(superUserSpec, NewProjectDescription.class, Project.class)
                .get(projectId);

        testDataCleanUp.add(projectId);
    }

    @Test
    public void authorizedUserShouldBeAbleToCreateNewProjectManually() {
        var testData = testDataStorage.addTestData();
        var project = testData.getProject();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .setupProjectManually(project.getName(), project.getId());

        //UI check that project exists
        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(project.getName()));

        //API check that project exists
        new CheckedRequestGenerator<Project>(superUserSpec, NewProjectDescription.class, Project.class)
                .get(project.getId());

        testDataCleanUp.add(project.getId());
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateNewProjectFromPrivateRepository() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/katerina-klinova/teamcity-testing-framework1";
        var createNewProjPage = new CreateNewProject();
        var project = testData.getProject();

        loginAsUser(testData.getUser());

        //UI action and check that project is not created
        createNewProjPage.open(project.getParentProject().getLocator())
                .createProjectByUrl(url)
                .getUrlErrorMessage().shouldHave(
                        Condition.text("Anonymous authentication has failed. The repository is either private or does not exist")
                        , Duration.ofMinutes(1));

        //API check that project does not exist
        new UncheckedRequestGenerator<Project>(superUserSpec, NewProjectDescription.class)
                .get(project.getId()).then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateNewProjectManuallyWithExistingName() {
        var testData = testDataStorage.addTestData();
        var createNewProjPage = new CreateNewProject();
        var project = testData.getProject();

        loginAsUser(testData.getUser());

        //UI action and check that project is not created
        createNewProjPage.open(project.getParentProject().getLocator())
                .setupProjectManually(project.getName(), project.getId());
        createNewProjPage.open(project.getParentProject().getLocator())
                .setupProjectManually(project.getName(), RandomData.getString())
                .getNameErrorMessage().shouldHave(
                Condition.text("Project with this name already exists:"));

        //API check doesn't make much sense here because project exists

        testDataCleanUp.add(project.getId());
    }
}
