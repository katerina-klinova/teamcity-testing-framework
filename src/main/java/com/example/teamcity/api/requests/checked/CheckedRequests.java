package com.example.teamcity.api.requests.checked;

import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class CheckedRequests {
    private CheckedUserRequest userRequest;
    private CheckedProjectRequest projectRequest;
    private CheckedBuildConfigRequest buildConfigRequest;

    public CheckedRequests(RequestSpecification spec) {
        this.userRequest = new CheckedUserRequest(spec);
        this.buildConfigRequest = new CheckedBuildConfigRequest(spec);
        this.projectRequest  = new CheckedProjectRequest(spec);
    }
}
