package com.teamcity.api.spec;

import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import com.teamcity.api.config.Config;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.models.User;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public final class Specifications {

    private static Specifications spec;

    private Specifications() {
    }

    public static Specifications getSpec() {
        if (spec == null) {
            spec = new Specifications();
        }
        return spec;
    }

    public RequestSpecification unauthSpec() {
        return reqBuilder()
                .setBaseUri("http://" + Config.getProperty("host"))
                .build();
    }

    public RequestSpecification authSpec(BaseModel model) {
        var user = (User) model;
        return reqBuilder()
                .setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")))
                .build();
    }

    public RequestSpecification superUserSpec() {
        return reqBuilder()
                .setBaseUri("http://:%s@%s".formatted(Config.getProperty("superUserToken"), Config.getProperty("host")))
                .build();
    }

    private RequestSpecBuilder reqBuilder() {
        return new RequestSpecBuilder()
        // Filters for requests and responses used to generate the Swagger Coverage report. Will be shown in Allure report
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured(),
                        new SwaggerCoverageRestAssured()))
                                //new SwaggerCoverageRestAssured(new FileSystemOutputWriter(Paths.get("target/" + OUTPUT_DIRECTORY)))))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);
    }

}
