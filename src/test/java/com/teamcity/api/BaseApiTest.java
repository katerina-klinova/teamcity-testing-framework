package com.teamcity.api;

import com.teamcity.BaseTest;
import com.teamcity.api.models.AuthModules;
import com.teamcity.api.models.ServerAuthSettings;
import com.teamcity.api.requests.checked.CheckedServerAuthSettings;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.teamcity.api.generators.TestDataGenerator.generate;


public class BaseApiTest extends BaseTest {

    private final CheckedServerAuthSettings checkedServerAuthSettings = new CheckedServerAuthSettings(
            Specifications.getSpec().superUserSpec());
    private AuthModules authModules;
    private boolean perProjectPermissions;

    @BeforeSuite(alwaysRun = true)
    public void setUpServerAuthSettings() {
        // Getting current value of "perProjectPermissions"
        perProjectPermissions = checkedServerAuthSettings.read(null)
                .getPerProjectPermissions();

        authModules = (AuthModules) generate(AuthModules.class);
        // Setting "perProjectPermissions" to true (for testing roles)
        checkedServerAuthSettings.update(null, ServerAuthSettings.builder()
                .perProjectPermissions(true)
                .modules(authModules)
                .build());
    }

    @AfterSuite(alwaysRun = true)
    public void cleanUpServerAuthSettings() {
        // Setting the "perProjectPermissions" value to the intial pre-test-run value
        checkedServerAuthSettings.update(null, ServerAuthSettings.builder()
                .perProjectPermissions(perProjectPermissions)
                .modules(authModules)
                .build());
    }

}
