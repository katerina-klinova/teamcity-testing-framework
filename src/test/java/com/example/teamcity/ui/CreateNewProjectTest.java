package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.testng.annotations.Test;

public class CreateNewProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleToCreateNewProjectFromRepository(){
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/AlexPshe/spring-core-for-qa";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProjectFromRepo(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));
    }

    @Test
    public void authorizedUserShouldBeAbleToCreateNewProjectManually(){
        var testData = testDataStorage.addTestData();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .setupProjectManually(testData.getProject().getName(), testData.getProject().getId());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateNewProjectFromPrivateRepository(){
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/katerina-klinova/teamcity-testing-framework";
        var createNewProjPage = new CreateNewProject();

        loginAsUser(testData.getUser());

        createNewProjPage.open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url);
        createNewProjPage.getUrlErrorMessage().shouldHave(
                Condition.text("Anonymous authentication has failed. The repository is either private or does not exist"));
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateNewProjectManuallyWithExistingName(){
        var testData = testDataStorage.addTestData();
        var createNewProjPage = new CreateNewProject();
        var project = testData.getProject();

        loginAsUser(testData.getUser());

        createNewProjPage.open(project.getParentProject().getLocator())
                .setupProjectManually(project.getName(), project.getId());
        createNewProjPage.open(project.getParentProject().getLocator())
                .setupProjectManually(project.getName(), RandomData.getString());
        createNewProjPage.getNameErrorMessage().shouldHave(
                Condition.text("Project with this name already exists:"));
    }
}
