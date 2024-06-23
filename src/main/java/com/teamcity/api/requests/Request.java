package com.teamcity.api.requests;

import com.teamcity.api.enums.Endpoint;
import io.restassured.specification.RequestSpecification;

public class Request {

    // Sticking to the pattern of keeping all the variables final if the implementation doesn't require the opposite
    // The same applies to access modifiers: private by default and extending to the minimal possible extent when needed
    protected final RequestSpecification spec;
    protected final Endpoint endpoint;

    public Request(RequestSpecification spec, Endpoint endpoint) {
        this.spec = spec;
        this.endpoint = endpoint;
    }

}
