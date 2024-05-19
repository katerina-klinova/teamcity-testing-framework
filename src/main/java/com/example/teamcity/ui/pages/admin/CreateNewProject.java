package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProject extends Page {

    public SelenideElement fromRepoLink = element(Selectors.byHref("#createFromUrl"));
    public SelenideElement manuallyLink = element(Selectors.byHref("#createManually"));
    private SelenideElement urlInput = element(Selectors.byId("url"));
    private SelenideElement projectNameInput = element(Selectors.byId("projectName"));
    private SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));
    private SelenideElement nameInput = element(Selectors.byId("name"));
    private SelenideElement projectIdInput = element(Selectors.byId("externalId"));
    @Getter
    private SelenideElement urlErrorMessage = element(Selectors.byId("error_url"));
    @Getter
    private SelenideElement nameErrorMessage = element(Selectors.byId("errorName"));


    public CreateNewProject open(String parentProjectId){
        waitUntilPageIsLoaded();
        Selenide.open("/admin/createObjectMenu.html?projectId=" + parentProjectId + "&showMode=createProjectMenu");
        return this;
    }

    public CreateNewProject createProjectByUrl(String url){
        waitUntilElementIsVisible(urlInput);
        urlInput.sendKeys(url);
        submit();
        return this;
    }

    public void setupProjectFromRepo(String projectName, String buildTypeName){
        waitUntilElementIsVisible(projectNameInput);
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildTypeName);
        submit();
    }

    public void setupProjectManually(String projectName, String projectId){
        waitUntilElementIsVisible(manuallyLink);
        manuallyLink.click();
        waitUntilElementIsVisible(nameInput);
        nameInput.sendKeys(projectName);
        projectIdInput.clear();
        projectIdInput.sendKeys(projectId);
        submit();
    }
}
