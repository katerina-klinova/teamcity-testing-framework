package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {

    private static Specifications spec;

    //Sigleton
    private Specifications(){}

    public static Specifications getSpec(){
        if (spec == null){
            spec = new Specifications();
        }
        return spec;
    }
    private RequestSpecBuilder getRequestBuilder(){
        var requestBuilder = new RequestSpecBuilder();
        requestBuilder.setBaseUri("http://" + Config.getProperty("host"));
        requestBuilder.addFilter(new RequestLoggingFilter());
        requestBuilder.addFilter(new ResponseLoggingFilter());
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        return requestBuilder;
    }
    public RequestSpecification getUnauthenticatedSpec(){
        var requestBuilder = getRequestBuilder();
        return requestBuilder.build();
    }

    public RequestSpecification getAuthenticatedSpec(User user){
        var requestBuilder = getRequestBuilder();
        requestBuilder.setBaseUri("http://" + user.getUsername() + ":" + user.getPassword() + "@" + Config.getProperty("host"));
        return requestBuilder.build();
    }

    public RequestSpecification getSuperUserSpec(){
        var requestBuilder = getRequestBuilder();
        requestBuilder.setBaseUri("http://:" + Config.getProperty("superUserToken") + "@" + Config.getProperty("host"));
        return requestBuilder.build();
    }
}
