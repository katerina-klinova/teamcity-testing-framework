package com.teamcity.api;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.models.*;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;
import org.apache.hc.core5.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;
import static com.teamcity.api.generators.TestDataGenerator.generate;

public class RolesTest extends BaseApiTest{

    @Test(description = "Unauthorized user should not be able to create a project"
            , groups = {"Regression"})
    public void unauthorizedUserShouldNotHaveRightToCreateProject(){
        var projectData = (NewProjectDescription) testData.get(PROJECTS);
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        new UncheckedBase(Specifications.getSpec().unauthSpec(), PROJECTS)
                .create(testData.get(PROJECTS))
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(BUILD_TYPES).read(((BuildType) testData.get(BUILD_TYPES)).getId())
                .then().assertThat().statusCode(org.apache.http.HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));

        uncheckedSuperUser.getRequest(PROJECTS)
                .read((projectData).getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString(String.format(
                        "No project found by locator 'count:1,id:%s'."
                        , (projectData).getId()
                )));
    }

    @Test(description = "System Admin should be able to create a project"
            , groups = {"Regression"})
    public void systemAdminShouldHaveRightsToCreateProject(){
        var userData = (User) testData.get(USERS);
        userData.setRoles((Roles) generate(
                Roles.class, UserRole.SYSTEM_ADMIN, "g"
        ));
        var projectData = (NewProjectDescription) testData.get(PROJECTS);

        checkedSuperUser.getRequest(USERS).create(userData);

        var projectRequest = new CheckedBase(Specifications.getSpec().authSpec(userData), PROJECTS);
        var project = (Project) projectRequest.create(projectData);

        softly.assertThat(project.getId()).isEqualTo(projectData.getId());
    }

    @Test(description = "Project Admin should be able to create a build config to a projec they are assigned to"
            , groups = {"Regression"})
    public void projectAdminShouldHaveRightsToCreateBuildConfigToTheirProject(){
        var projectData = (NewProjectDescription) testData.get(PROJECTS);

        checkedSuperUser.getRequest(PROJECTS).create(projectData);

        var userData = (User) testData.get(USERS);
        userData.setRoles((Roles) generate(
                Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectData.getId()
        ));

        checkedSuperUser.getRequest(USERS).create(userData);

        var buildTypeData = (BuildType) testData.get(BUILD_TYPES);
        var buildConfig = (BuildType) new CheckedBase(Specifications.getSpec()
                .authSpec(userData), BUILD_TYPES)
                .create(buildTypeData);
        softly.assertThat(buildConfig.getId()).isEqualTo(buildTypeData.getId());
    }

    @Test(description = "Project Admin should not be able to create a build config to a project they are not assigned to"
            , groups = {"Regression"})
    public void projectAdminShouldNotHaveRightsToCreateBuildConfigToAnotherProject() {
        var projectData1 = (NewProjectDescription) testData.get(PROJECTS);
        var testData2 = generate();
        var projectData2 = (NewProjectDescription) testData2.get(PROJECTS);
        checkedSuperUser.getRequest(PROJECTS).create(projectData1);
        checkedSuperUser.getRequest(PROJECTS).create(projectData2);

        var userData1 = (User) testData.get(USERS);
        userData1.setRoles((Roles) generate(
                Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectData1.getId()
        ));
        checkedSuperUser.getRequest(USERS).create(userData1);

        var userData2 = (User) testData2.get(USERS);
        userData2.setRoles((Roles) generate(
                Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectData2.getId()
        ));
        checkedSuperUser.getRequest(USERS).create(userData2);

        new UncheckedBase(Specifications.getSpec().authSpec(userData2), BUILD_TYPES)
                .create(testData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString(
                        "You do not have enough permissions to access project with internal id: project"));
    }
}
