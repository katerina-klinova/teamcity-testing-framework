package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.ui.pages.admin.CreateBuildConfiguration;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.admin.EditProject;
import org.testng.annotations.Test;

public class CrateNewBuildConfigTests extends BaseUiTest{
    @Test
    public void authorizedUserShouldBeAbleToCreateNewBuildConfigFromRepository(){
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/AlexPshe/spring-core-for-qa";
        var project = testData.getProject();
        var buildConfig = testData.getBuildType();
        var editProject = new EditProject(testData.getBuildType().getName());

        loginAsUser(testData.getUser());

        new CreateNewProject().open(project.getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProjectFromRepo(project.getName(), buildConfig.getName());

        editProject.open(project.getName().replace("_", "")).clickCreateBuildConfig();

        new CreateBuildConfiguration().createBuildFromRepo(url)
                .setupBuildConfigFromRepo(buildConfig.getName());

        editProject.open(project.getName().replace("_", "")).getBuildConfigNameTextInTable()
               .shouldHave(Condition.text(buildConfig.getName()));
    }

    @Test
    public void authorizedUserShouldBeAbleToCreateNewBuildConfigManually(){
        var testData = testDataStorage.addTestData();
        var project = testData.getProject();
        var editProject = new EditProject(testData.getBuildType().getName());

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(project.getParentProject().getLocator())
                .setupProjectManually(project.getName(), project.getId());

        editProject.open(project.getId()).clickCreateBuildConfig();

        new CreateBuildConfiguration()
                .setupBuildConfigManually(testData.getBuildType().getName(), testData.getBuildType().getId());

        editProject.open(project.getName()).getBuildConfigNameTextInTable()
                .shouldHave(Condition.text(testData.getBuildType().getName()));
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateNewBuildConfigFromRepositoryWithNoUrl(){
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/AlexPshe/spring-core-for-qa";
        var project = testData.getProject();
        var createBuildConfig = new CreateBuildConfiguration();

        loginAsUser(testData.getUser());

        new CreateNewProject().open(project.getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProjectFromRepo(project.getName(), testData.getBuildType().getName());

        createBuildConfig.open(project.getName().replace("_", ""))
                .createBuildFromRepo("");
        createBuildConfig.getUrlErrorMessage()
                .shouldHave(Condition.text("URL must not be empty"));
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateNewBuildConfigManuallyForExistingName(){
        var testData = testDataStorage.addTestData();
        var project = testData.getProject();
        var buildConfig = testData.getBuildType();
        var createBuildConfig = new CreateBuildConfiguration();

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(project.getParentProject().getLocator())
                .setupProjectManually(project.getName(), project.getId());

        createBuildConfig.open(project.getId())
                .setupBuildConfigManually(buildConfig.getName(), buildConfig.getId());

        createBuildConfig.open(project.getId())
                .setupBuildConfigManually(buildConfig.getName(), RandomData.getString());
        createBuildConfig.getBuildConfigNameErrorMessage()
                .shouldHave(Condition.text(String.format(
                        "Build configuration with name \"%s\" already exists in project: \"%s\"",
                        buildConfig.getName(),
                        project.getName()
                )));
    }
}
