package com.example.teamcity.ui.pages;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.ui.Selectors;

import static com.codeborne.selenide.Selenide.element;

public class LogInAsSuperUser extends Page{
    private SelenideElement authTokenInput = element(Selectors.byId("password"));

    public void logInWithAuthToken(){
        waitUntilElementIsVisible(authTokenInput);
        authTokenInput.sendKeys(Config.getProperty("superUserToken"));
        submit();
        waitUntilDataIsSaved();
    }
}
