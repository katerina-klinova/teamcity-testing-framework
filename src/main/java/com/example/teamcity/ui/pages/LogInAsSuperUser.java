package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.element;

public class LogInAsSuperUser extends Page{
    private SelenideElement authTokenInput = element(Selectors.byId("password"));
    @Getter
    private SelenideElement welcomeText = element(Selectors.byClass("h1"));

    public LogInAsSuperUser open(){
        Selenide.open("/login.html?super=1");
        waitUntilPageIsLoaded();
        return this;
    }

    public LogInAsSuperUser logInWithAuthToken(){
        waitUntilPageIsLoaded();
        waitUntilElementIsVisible(authTokenInput);
        authTokenInput.sendKeys(Config.getProperty("superUserToken"));
        submit();
        waitUntilDataIsSaved();
        waitUntilElementIsVisible(getWelcomeText());
        return this;
    }
}
