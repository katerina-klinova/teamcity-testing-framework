package com.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.teamcity.BaseTest;
import com.teamcity.api.config.Config;
import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.User;
import com.teamcity.ui.pages.LoginPage;
import org.testng.annotations.BeforeSuite;

public class BaseUiTest extends BaseTest {
    protected static final String PUBLIC_REPO = "https://github.com/AlexPshe/spring-core-for-qa";
    protected static final String PRIVATE_REPO = "https://github.com/katerina-klinova/teamcity-testing-framework1";

    @BeforeSuite
    public void setupUiTests(){
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");

        Configuration.reportsFolder = "target/surefire-reports";
        Configuration.downloadsFolder ="target/downloads";

        BrowserSettings.setup(Config.getProperty("browser"));
    }

    public void logInAsUser(User user){
        checkedSuperUser.getRequest(Endpoint.USERS).create(testData.get(Endpoint.USERS));
//        new CheckedRequestGenerator<User>(superUserSpec, User.class, User.class)
//                .create(user);
        new LoginPage().open().login(user);
    }
}
