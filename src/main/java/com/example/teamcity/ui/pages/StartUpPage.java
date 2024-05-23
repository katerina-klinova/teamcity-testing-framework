package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

@Getter
public class StartUpPage extends Page{

    private SelenideElement header;
    private SelenideElement proceedButton;
    private SelenideElement acceptLicence;

    public StartUpPage open(){
        Selenide.open("/");
        return this;
    }

    public StartUpPage setupTeamCityServer(){
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        waitUntilElementIsEnabled(acceptLicence);
        acceptLicence.scrollTo();
        acceptLicence.click();
        submit();
        return this;
    }
}
