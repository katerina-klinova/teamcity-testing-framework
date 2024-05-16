package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedRequestGenerator;
import com.example.teamcity.ui.pages.LoginPage;
import org.testng.annotations.BeforeSuite;

public class BaseUiTest extends BaseTest {

    @BeforeSuite
    public void setupUiTests(){
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");

        Configuration.reportsFolder = "target/surefire-reports";
        Configuration.downloadsFolder ="target/downloads";

        BrowserSettings.setup(Config.getProperty("browser"));
    }

    public void loginAsUser(User user){
        new CheckedRequestGenerator<User>(superUserSpec, User.class, User.class)
                .create(user);
        new LoginPage().open().login(user);
    }
}
