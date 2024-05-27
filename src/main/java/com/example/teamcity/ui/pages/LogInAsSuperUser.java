package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class LogInAsSuperUser extends Page{
    private SelenideElement authTokenInput = element(Selectors.byId("password"));
    @Getter
    private SelenideElement welcomeText = element(Selectors.byText("Welcome to TeamCity"));

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
        welcomeText.shouldHave(Condition.text("Welcome to TeamCity"), Duration.ofMinutes(1));
        return this;
    }
}
