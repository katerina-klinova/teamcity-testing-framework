package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

@Getter
public class StartUpPage extends Page{

    private SelenideElement header = element(Selectors.byId("proceedButton"));
    private SelenideElement continueButton = element(Selectors.byName("Continue"));
    private SelenideElement proceedButton = element(Selectors.byId("proceedButton"));
    private SelenideElement restoreFromBackupButton = element(Selectors.byId("restoreButton"));
    private SelenideElement acceptLicence = element(Selectors.byId("accept"));

    public StartUpPage open(){
        Selenide.open("/");
        waitUntilPageIsLoaded();
        return this;
    }

    public StartUpPage setupTeamCityServer(){
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        acceptLicence.shouldBe(Condition.enabled, Duration.ofMinutes(5));
        acceptLicence.scrollTo();
        acceptLicence.click();
        continueButton.click();
        return this;
    }
}
