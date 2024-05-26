package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.Agent;
import com.example.teamcity.ui.pages.AgentsUnauthorized;
import com.example.teamcity.ui.pages.LogInAsSuperUser;
import com.example.teamcity.ui.pages.StartUpPage;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.webdriver;

public class SetupTest extends BaseUiTest{

    @Test(groups = {"setup"})
    public void startUpTest(){
        new StartUpPage().open()
                .setupTeamCityServer()
                .getHeader().shouldHave(Condition.text("Create Administrator Account"), Duration.ofMinutes(2));
    }

    @Test(groups = {"setup"})
    public void setupTeamCityAgentTest(){
//        new CreateAdministratorAccount().open()
//                .followLogInAsSuperUserLink();

        var superUserLogin = new LogInAsSuperUser().open();
        superUserLogin
                .getHeader().shouldHave(Condition.text("Log in as Super user"));
        superUserLogin
                .logInWithAuthToken()
                .getWelcomeText().shouldHave(Condition.text("Welcome to TeamCity"), Duration.ofMinutes(2));
        System.out.println("URL = " + webdriver().driver().getCurrentFrameUrl());

        new AgentsUnauthorized().open()
                .openUnauthorizedAgent();

        new Agent().open().authorizeAgent();
    }
}
