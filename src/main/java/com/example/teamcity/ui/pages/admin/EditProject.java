package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;
import lombok.Getter;
import lombok.Setter;

import static com.codeborne.selenide.Selenide.element;

public class EditProject extends Page {
    private SelenideElement createBuildConfigLink = element(Selectors.byText("Create build configuration"));
    @Setter
    @Getter private SelenideElement buildConfigNameTextInTable;

    public EditProject(String buildConfigName){
        this.buildConfigNameTextInTable = element(Selectors.byText(buildConfigName));
    }
    public EditProject open(String projectName){
        System.out.println("EditProject URL = " + "/admin/editProject.html?projectId=" + projectName);
        Selenide.open("/admin/editProject.html?projectId=" + projectName);
        waitUntilPageIsLoaded();
        return this;
    }

    public void clickCreateBuildConfig(){
        waitUntilElementIsVisible(createBuildConfigLink);
        createBuildConfigLink.click();
    }

}
