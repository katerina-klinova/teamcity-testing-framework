package com.example.teamcity.api.requests.unchecked;

import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class UncheckedRequests {
    private UncheckedUserRequest userRequest;
    private UncheckedProjectRequest projectRequest;
    private UncheckedBuildConfigRequest buildConfigRequest;

    public UncheckedRequests(RequestSpecification spec) {
        this.userRequest = new UncheckedUserRequest(spec);
        this.buildConfigRequest = new UncheckedBuildConfigRequest(spec);
        this.projectRequest  = new UncheckedProjectRequest(spec);
    }
}
