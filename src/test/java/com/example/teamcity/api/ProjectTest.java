package com.example.teamcity.api;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectTest extends BaseApiTest{

    //1. create project with all required fields under root
    @Test
    public void projShouldGetCreatedUnderRootWithRequiredFieldsProvided(){
        var testData = testDataStorage.addTestData();

        var project = checkedWithSuperUser.getProjectRequest()
                .create(testData.getProject());

        softly.assertThat(project.getId())
                .isEqualTo(testData.getProject().getId());
    }

    //2. create project with required fields under another project
    @Test
    public void projShouldGetCreatedUnderAnotherProjWithRequiredFieldsProvided(){
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        var projectOne = checkedWithSuperUser.getProjectRequest()
                .create(firstTestData.getProject());

        testDataCleanUp.add(projectOne.getId());

        secondTestData.getProject().setParentProject(projectOne);

        var projectTwo = checkedWithSuperUser.getProjectRequest()
                .create(secondTestData.getProject());

        softly.assertThat(projectTwo.getId())
                .isEqualTo(secondTestData.getProject().getId());

        softly.assertThat(projectTwo.getParentProjectId())
                .isEqualTo(projectOne.getId());

    }
    //3. create project with all fields provided
    // TODO

    //4. create project with copySettings = false
    @Test
    public void projShouldGetCreatedUnderRootHavingCopyAllAssociatedSettingsSetToFalse(){
        var testData = testDataStorage.addTestData();

        testData.getProject().setCopyAllAssociatedSettings(false);

        var project = checkedWithSuperUser.getProjectRequest()
                .create(testData.getProject());

        testDataCleanUp.add(project.getId());

        softly.assertThat(project.getId())
                .isEqualTo(testData.getProject().getId());
    }

    //5. create project with missing name
    @Test
    public void projShouldNotGetCreatedHavingNameEmpty(){
        var testData = testDataStorage.addTestData();

        testData.getProject().setName("");

        uncheckedWithSuperUser.getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty."));
    }

    //6. create project with missing id
    @Test
    public void projShouldNotGetCreatedHavingIdEmpty(){
        var testData = testDataStorage.addTestData();

        testData.getProject().setId("");

        uncheckedWithSuperUser.getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID must not be empty."));
    }

    //7. create project with non existent parent
    @Test
    public void projShouldNotGetCreatedHavingNonExistentParent(){
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        firstTestData.getProject().setParentProject(secondTestData.getProject().getParentProject());

        uncheckedWithSuperUser.getProjectRequest()
                .create(firstTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //8. create proj with existing name
    @Test
    public void projShouldNotGetCreatedHavingExistingName(){
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        var projectOne = checkedWithSuperUser.getProjectRequest()
                .create(firstTestData.getProject());
        testDataCleanUp.add(projectOne.getId());

        secondTestData.getProject().setName(projectOne.getName());

        uncheckedWithSuperUser.getProjectRequest()
                .create(secondTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project with this name already exists: " + projectOne.getName()));
    }

    //9. create proj with existing id
    @Test
    public void projShouldNotGetCreatedHavingExistingId(){
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        var projectOne = checkedWithSuperUser.getProjectRequest()
                .create(firstTestData.getProject());
        testDataCleanUp.add(projectOne.getId());

        secondTestData.getProject().setId(projectOne.getId());

        uncheckedWithSuperUser.getProjectRequest()
                .create(secondTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"" + projectOne.getId() + "\" is already used by another project"));
    }

    //10. create proj with name.length = 1 symbol
    @Test
    public void projShouldGetCreatedHaving1AlphaSymbolInName(){
        var testData = testDataStorage.addTestData();

        testData.getProject().setName(RandomStringUtils.randomAlphabetic(1));

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testDataCleanUp.add(project.getId());

        softly.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
    }

    //11. create proj with name.length = 255 alphanumeric symbols
    @Test
    public void projShouldGetCreatedHaving255AlphaNumSymbolsInName(){
        var testData = testDataStorage.addTestData();

        testData.getProject().setName(RandomStringUtils.randomAlphanumeric(255));

        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testDataCleanUp.add(project.getId());

        softly.assertThat(project.getName()).isEqualTo(testData.getProject().getName());
    }

    //12. create proj with name.length = 256 alphanumeric symbols
    @Test
    public void projShouldNotGetCreatedHaving256AlphaNumSymbolsInName(){
        var testData = testDataStorage.addTestData();

        testData.getProject().setName(RandomStringUtils.randomAlphanumeric(256));

        uncheckedWithSuperUser.getProjectRequest().create(testData.getProject())
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}
