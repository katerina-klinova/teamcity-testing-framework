package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedRequestGenerator;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

public class CheckedRequestGenerator<Response> extends Request implements CrudInterface {
    private Class<Request> requestClass;
    private final Class<Response> responseClass;

    public CheckedRequestGenerator(RequestSpecification spec, Class requestClass, Class responseClass) {
        super(spec);
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    @Override
    public Response create(Object obj) {
        return new UncheckedRequestGenerator<Type>(spec, requestClass).create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(responseClass);
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
        return new UncheckedRequestGenerator<Type>(spec,requestClass).delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }
}
