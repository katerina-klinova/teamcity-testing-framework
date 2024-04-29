package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class ServerAuthSettingsRequest extends Request implements CrudInterface {

    private final static String SERVER_AUTH_SETTINGS_ENDPOINT = "/app/rest/server/authSettings";

    public ServerAuthSettingsRequest(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
        return given().spec(spec).body(obj)
                .put(SERVER_AUTH_SETTINGS_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public Object update(String id, Object obj) {
        return null;
    }

    @Override
    public Object delete(String id) {
        return null;
    }
}
