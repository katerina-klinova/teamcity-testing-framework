package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.Agent;
import com.example.teamcity.ui.pages.AgentsUnauthorized;
import com.example.teamcity.ui.pages.LogInAsSuperUser;
import com.example.teamcity.ui.pages.StartUpPage;
import org.testng.annotations.Test;

import java.time.Duration;

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
                .getWelcomeText().shouldHave(Condition.text("Welcome to TeamCity"), Duration.ofSeconds(30));

        new AgentsUnauthorized().open()
                .openUnauthorizedAgent();

        new Agent().open().authorizeAgent();
    }
}
