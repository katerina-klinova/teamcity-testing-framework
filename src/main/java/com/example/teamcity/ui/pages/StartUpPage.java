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

    private SelenideElement header = element(Selectors.byId("header"));//Create Administrator Account
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
       // Selenide.sleep(30000);
        header.shouldBe(Condition.visible, Duration.ofMinutes(3));
        return this;
    }
}
