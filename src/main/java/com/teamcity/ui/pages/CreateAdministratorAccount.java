package com.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;

import static com.codeborne.selenide.Selenide.element;

public class CreateAdministratorAccount extends Page {
    private SelenideElement logInAsSuperUserLink = element(Selectors.byHref("/login.html?super=1"));

    public CreateAdministratorAccount open() {
        Selenide.open("/setupAdmin.html");
        waitUntilPageIsLoaded();
        return this;
    }

    public void followLogInAsSuperUserLink(){
        waitUntilElementIsVisible(logInAsSuperUserLink);
        logInAsSuperUserLink.click();
    }
}