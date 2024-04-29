package com.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.ServerAuthSettingsRequest;
import com.example.teamcity.api.spec.Specifications;

public class SetupSuite {

    public void setServerAuthSettings(){
        new ServerAuthSettingsRequest(Specifications.getSpec().getSuperUserSpec())
                .create(TestDataGenerator.generateServerAuthSettings());
    }
}
