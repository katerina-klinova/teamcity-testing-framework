package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.*;
import org.testng.annotations.Test;

public class SetupTest extends BaseUiTest{

    @Test(groups = {"setup"})
    public void startUpTest(){
        new StartUpPage().open()
                .setupTeamCityServer()
                .getHeader().shouldHave(Condition.text("Create Account Administrator"));
    }

    @Test(groups = {"setup"})
    public void setupTeamCityAgentTest(){
        new CreateAdministratorAccount().open()
                .followLogInAsSuperUserLink();

        new LogInAsSuperUser().logInWithAuthToken();

        new AgentsUnauthorized().open()
                .openUnauthorizedAgent();

        new Agent().authorizeAgent();
    }
}
