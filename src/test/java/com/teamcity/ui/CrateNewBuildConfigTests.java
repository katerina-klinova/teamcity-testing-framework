package com.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.User;
import com.teamcity.ui.pages.admin.CreateBuildConfiguration;
import com.teamcity.ui.pages.admin.CreateNewProject;
import com.teamcity.ui.pages.admin.EditProject;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;

@Feature("Build type")
public class CrateNewBuildConfigTests extends BaseUiTest {
    @Test(description = "User should be able to create a build config from public repository ", groups = {"Regression"})
    public void authorizedUserShouldBeAbleToCreateNewBuildConfigFromRepository() {
        var projectData = (NewProjectDescription) testData.get(PROJECTS);
        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);
        var editProject = new EditProject(buildTypeData.getName());

        logInAsUser((User) testData.get(USERS));

        new CreateNewProject().open(projectData.getParentProject().getLocator())
                .createProjectByUrl(PUBLIC_REPO)
                .setupProjectFromRepo(projectData.getId(), buildTypeData.getName());

        editProject.open(projectData.getId()).clickCreateBuildConfig();

        new CreateBuildConfiguration().createBuildFromRepo(PUBLIC_REPO)
                .setupBuildConfigFromRepo(buildTypeData.getName());

        //UI check that build configuration exists
        editProject.open(projectData.getId()).getBuildConfigNameTextInTable()
                .shouldHave(Condition.text(buildTypeData.getName()));

        //API check that build configuration exists
        checkedSuperUser.getRequest(BUILD_TYPES).read(projectData.getId() + "_" + buildTypeData.getName());
    }

    @Test(description = "User should be able to create a build config manually", groups = {"Regression"})
    public void authorizedUserShouldBeAbleToCreateNewBuildConfigManually() {
        var projectData = (NewProjectDescription) testData.get(PROJECTS);
        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);
        var editProject = new EditProject(buildTypeData.getName());

        logInAsUser((User) testData.get(USERS));

        new CreateNewProject()
                .open(projectData.getParentProject().getLocator())
                .setupProjectManually(projectData.getName(), projectData.getId());

        editProject.open(projectData.getId()).clickCreateBuildConfig();

        new CreateBuildConfiguration()
                .setupBuildConfigManually(buildTypeData.getName(), buildTypeData.getId());

        //UI check that build configuration exists
        editProject.open(projectData.getName()).getBuildConfigNameTextInTable()
                .shouldHave(Condition.text(buildTypeData.getName()));

        //API check that build configuration exists
        checkedSuperUser.getRequest(BUILD_TYPES).read(buildTypeData.getId());
    }

    @Test(description = "User should not be able to create a build config from repo with no URL provided", groups = {"Regression"})
    public void authorizedUserShouldNotBeAbleToCreateNewBuildConfigFromRepositoryWithNoUrl() {
        var projectData = (NewProjectDescription) testData.get(PROJECTS);

        logInAsUser((User) testData.get(USERS));

        new CreateNewProject().open(projectData.getParentProject().getLocator())
                .createProjectByUrl(PUBLIC_REPO)
                .setupProjectFromRepo(projectData.getId(), ((BuildType) testData.get(BUILD_TYPES)).getName());

        new CreateBuildConfiguration().open(projectData.getId())
                .createBuildFromRepo("")
                .getUrlErrorMessage()
                .shouldHave(Condition.text("URL must not be empty"));
    }

    @Test(description = "User should not be able to create a build config manually with existing name ", groups = {"Regression"})
    public void authorizedUserShouldNotBeAbleToCreateNewBuildConfigManuallyForExistingName() {
        var projectData = (NewProjectDescription) testData.get(PROJECTS);
        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);
        var createBuildConfig = new CreateBuildConfiguration();

        logInAsUser((User) testData.get(USERS));

        new CreateNewProject()
                .open(projectData.getParentProject().getLocator())
                .setupProjectManually(projectData.getName(), projectData.getId());

        createBuildConfig.open(projectData.getId())
                .setupBuildConfigManually(buildTypeData.getName(), buildTypeData.getId());

        createBuildConfig.open(projectData.getId())
                .setupBuildConfigManually(buildTypeData.getName(), RandomData.getString())
                .getBuildConfigNameErrorMessage()
                .shouldHave(Condition.text(String.format(
                        "Build configuration with name \"%s\" already exists in project: \"%s\"",
                        buildTypeData.getName(),
                        projectData.getName()
                )));
    }
}
