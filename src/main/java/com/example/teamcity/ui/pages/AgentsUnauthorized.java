package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;

import static com.codeborne.selenide.Selenide.element;

public class AgentsUnauthorized extends Page{
    private SelenideElement agentLink = element(Selectors.byHref("/agent/1"));

    public AgentsUnauthorized open(){
        Selenide.open("/agents/unauthorized");
        waitUntilPageIsLoaded();
        return this;
    }

    public void openUnauthorizedAgent(){
        waitUntilElementIsVisible(agentLink);
        agentLink.click();
    }

}
