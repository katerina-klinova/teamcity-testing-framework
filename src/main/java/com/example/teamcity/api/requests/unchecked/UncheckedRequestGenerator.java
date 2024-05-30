package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.generators.TestDataCleanUp;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedRequestGenerator<T> extends Request implements CrudInterface {
    private String endpoint;
    private Class<T> requestClass;
    private String identifier = "/id:";

    public UncheckedRequestGenerator(RequestSpecification spec, Class requestClass) {
        super(spec);
        this.requestClass = requestClass;

        switch (requestClass.getSimpleName()) {
            case "BuildConfigurationTest":
                endpoint = "/app/rest/buildTypes";
                break;
            case "NewProjectDescription":
                endpoint = "/app/rest/projects";
                break;
            case "User":
                endpoint = "/app/rest/users";
                identifier = "/username:";
                break;
        }
    }

    @Override
    public Response create(Object obj) {
        var response = given()
  //              .filter(new SwaggerCoverageRestAssured())
                .spec(spec)
                .body(obj)
                .post(endpoint);
        if (response.statusCode() >= 200 && response.statusCode() <= 300){
            TestDataCleanUp.getTestDataUsed().add(obj.toString());
        }
        return response;
    }

    @Override
    public Response get(String id) {
        return given()
    //            .filter(new SwaggerCoverageRestAssured())
                .spec(spec)
                .get(endpoint + "/id:" + id);
    }

    @Override
    public Object update(String id, Object obj) {
        return given()
   //             .filter(new SwaggerCoverageRestAssured())
                .spec(spec)
                .body(obj)
                .put(endpoint + "/id:" + id);
    }

    @Override
    public Response delete(String id) {
        return given()
      //          .filter(new SwaggerCoverageRestAssured())
                .spec(spec)
                .delete(endpoint + identifier + id);
    }
}
