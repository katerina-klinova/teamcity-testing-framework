package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.element;

public class Agent extends Page {
    private SelenideElement authorizeInitButton = element(Selectors.byAttribute("data-test-authorize-agent","true"));
    private SelenideElement authorizeConfirmButton = element(Selectors.byClassName("CommonForm__button--Nb"));

    public void authorizeAgent(){
        waitUntilElementIsEnabled(authorizeInitButton);
        authorizeInitButton.click();
        waitUntilElementIsEnabled(authorizeConfirmButton);
        authorizeConfirmButton.click();
    }
}
