package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Request;
import io.restassured.specification.RequestSpecification;
import com.example.teamcity.api.requests.CrudInterface;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UncheckedProjectRequest extends Request implements CrudInterface {

    private static final String PROJECT_ENDPOINT = "/app/rest/projects";

    public UncheckedProjectRequest(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .post(PROJECT_ENDPOINT);
    }

    @Override
    public Response get(String id) {
        return given()
                .spec(spec)
                .get(PROJECT_ENDPOINT + "/id:" + id);
    }

    @Override
    public Object update(String id, Object objj) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given()
                .spec(spec)
                .delete(PROJECT_ENDPOINT + "/id:" + id);
    }
}
