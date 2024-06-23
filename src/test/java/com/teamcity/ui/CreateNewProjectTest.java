package com.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.User;
import com.teamcity.ui.pages.admin.CreateNewProject;
import com.teamcity.ui.pages.favorites.ProjectsPage;
import io.qameta.allure.Feature;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.teamcity.api.enums.Endpoint.*;

@Feature("Project")
public class CreateNewProjectTest extends BaseUiTest {
    @Test(description = "User should be able to create a project from a public repository", groups = {"Regression"})
    public void authorizedUserShouldBeAbleToCreateNewProjectFromRepository() {
        var projectData = (NewProjectDescription) testData.get(PROJECTS);

        logInAsUser((User) testData.get(USERS));

        new CreateNewProject()
                .open(projectData.getParentProject().getLocator())
                .createProjectByUrl(PUBLIC_REPO)
                .setupProjectFromRepo(projectData.getId(),((BuildType) testData.get(BUILD_TYPES)).getName());

        //UI check that project is created
        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(projectData.getId()));

        //API check that project exists
        checkedSuperUser.getRequest(PROJECTS).read(projectData.getId());
    }

    @Test(description = "User should be able to create a project manually", groups = {"Regression"})
    public void authorizedUserShouldBeAbleToCreateNewProjectManually() {
        var projectData = (NewProjectDescription) testData.get(PROJECTS);

        logInAsUser((User) testData.get(USERS));

        new CreateNewProject()
                .open(projectData.getParentProject().getLocator())
                .setupProjectManually(projectData.getName(), projectData.getId());

        //UI check that project exists
        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(projectData.getName()));

        //API check that project exists
        checkedSuperUser.getRequest(PROJECTS).read(projectData.getId());
    }

    @Test(description = "User should not be able to create a project from private repository", groups = {"Regression"})
    public void authorizedUserShouldNotBeAbleToCreateNewProjectFromPrivateRepository() {
        var createNewProjPage = new CreateNewProject();
        var projectData = (NewProjectDescription) testData.get(PROJECTS);

        logInAsUser((User) testData.get(USERS));

        //UI action and check that project is not created
        createNewProjPage.open(projectData.getParentProject().getLocator())
                .createProjectByUrl(PRIVATE_REPO)
                .getUrlErrorMessage().shouldHave(
                        Condition.text("Anonymous authentication has failed. The repository is either private or does not exist")
                        , Duration.ofMinutes(1));

        //API check that project does not exist
        uncheckedSuperUser.getRequest(PROJECTS).read(projectData.getId())
                .then().assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "User should not be able to create a project with existing name", groups = {"Regression"})
    public void authorizedUserShouldNotBeAbleToCreateNewProjectManuallyWithExistingName() {
        var createNewProjPage = new CreateNewProject();
        var projectData = (NewProjectDescription) testData.get(PROJECTS);

        logInAsUser((User) testData.get(USERS));

        //UI action and check that project is not created
        createNewProjPage.open(projectData.getParentProject().getLocator())
                .setupProjectManually(projectData.getName(), projectData.getId());
        createNewProjPage.open(projectData.getParentProject().getLocator())
                .setupProjectManually(projectData.getName(), RandomData.getString())
                .getNameErrorMessage().shouldHave(
                Condition.text("Project with this name already exists:"));
    }
}
