package com.example.teamcity.api.generators;

import com.example.teamcity.api.models.AuthModule;
import com.example.teamcity.api.models.AuthModules;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Properties;
import com.example.teamcity.api.models.Property;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.models.ServerAuthSettings;
import com.example.teamcity.api.models.User;

import java.util.Arrays;

public class TestDataGenerator {

    public static ServerAuthSettings generateServerAuthSettings() {
        return new ServerAuthSettings().builder()
                .perProjectPermissions(true)
                .modules(AuthModules.builder()
                        .module(Arrays.asList(
                                AuthModule.builder()
                                        .name("HTTP-Basic")
                                        .build(),
                                AuthModule.builder()
                                        .name("Default")
                                        .properties(Properties.builder()
                                                //.count(RandomData.getInt(0, 10))
                                                // .href(RandomData.getString())
                                                .property(Arrays.asList(
                                                        Property.builder()
                                                                .name("usersCanResetOwnPasswords")
                                                                .value("true")
                                                                // .inherited(RandomData.getBoolean())
                                                                //.type(Type.builder()
                                                                //         .rawValue(RandomData.getString())
                                                                //         .build())
                                                                .build(),
                                                        Property.builder()
                                                                .name("usersCanChangeOwnPasswords")
                                                                .value("true")
                                                                .build(),
                                                        Property.builder()
                                                                .name("freeRegistrationAllowed")
                                                                .value("false")
                                                                .build()))
                                                .build())
                                        .build(),
                                AuthModule.builder()
                                        .name("Token-Auth")
                                        .build(),
                                AuthModule.builder()
                                        .name("LDAP")
                                        .properties(Properties.builder()
                                                .property(Arrays.asList(
                                                        Property.builder()
                                                                .name("allowCreatingNewUsersByLogin")
                                                                .value("true")
                                                                .build()))
                                                .build())
                                        .build()))
                        .build())
                .build();
    }

    public static TestData generate(){
        var user = User.builder()
                .username("user_" + RandomData.getString())
                .password(RandomData.getString())
                .email(RandomData.getString() + "@gmail.com")
                .roles(Roles.builder()
                        .role(Arrays.asList(Role.builder()
                                .roleId("SYSTEM_ADMIN")
                                .scope("g")
                                .build()))
                        .build())
                .build();

        var project = new NewProjectDescription()
                .builder()
                .parentProject(Project.builder()
                        .locator("_Root")
                        .build())
                .name(RandomData.getString())
                .id("project_" + RandomData.getString())
                .copyAllAssociatedSettings(true)
                .build();

        var buildType = new BuildType().builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(project)
                .build();

        return TestData.builder()
                .user(user)
                .project(project)
                .buildType(buildType)
                .build();
    }

    public static Roles generateRole(com.example.teamcity.api.enums.Role role, String scope){
        return Roles.builder()
                .role(Arrays.asList(Role.builder()
                                .roleId(role.getText())
                        .scope(scope)
                        .build()))
                .build();
    }
}
