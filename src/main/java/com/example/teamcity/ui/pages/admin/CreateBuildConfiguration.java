package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.element;

public class CreateBuildConfiguration extends Page{
    public SelenideElement fromRepoLink = element(Selectors.byHref("#createFromUrl"));
    public SelenideElement manuallyLink = element(Selectors.byHref("#createManually"));
    private SelenideElement urlInput = element(Selectors.byId("url"));
    private SelenideElement buildConfigNameInput = element(Selectors.byId("buildTypeName"));
    private SelenideElement nameInput = element(Selectors.byId("buildTypeName"));
    private SelenideElement buildConfigIdInput = element(Selectors.byId("buildTypeExternalId"));
    @Getter
    private SelenideElement urlErrorMessage = element(Selectors.byId("error_url"));
    @Getter
    private SelenideElement buildConfigNameErrorMessage = element(Selectors.byId("error_buildTypeName"));

    public CreateBuildConfiguration open(String parentProjectId){
        waitUntilPageIsLoaded();
        Selenide.open("/admin/createObjectMenu.html?projectId=" + parentProjectId + "&showMode=createBuildTypeMenu");
        return this;
    }

    public CreateBuildConfiguration createBuildFromRepo(String url){
        waitUntilElementIsVisible(fromRepoLink);
        fromRepoLink.click();
        waitUntilElementIsVisible(urlInput);
        urlInput.sendKeys(url);
        submit();
        waitUntilDataIsSaved();
        return this;
    }

    public void setupBuildConfigFromRepo(String buildTypeName){
        waitUntilElementIsVisible(buildConfigNameInput);
        buildConfigNameInput.clear();
        buildConfigNameInput.sendKeys(buildTypeName);
        submit();
    }

    public void setupBuildConfigManually(String buildConfigName, String buildConfigId){
        waitUntilElementIsVisible(manuallyLink);
        manuallyLink.click();
        waitUntilElementIsVisible(nameInput);
        nameInput.sendKeys(buildConfigName);
        buildConfigIdInput.clear();
        buildConfigIdInput.sendKeys(buildConfigId);
        submit();
    }
}
