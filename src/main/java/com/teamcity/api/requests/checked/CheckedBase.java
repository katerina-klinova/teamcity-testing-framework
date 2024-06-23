package com.teamcity.api.requests.checked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedBase extends Request implements CrudInterface {

    // All the requests with the same CRUD implementation can be created using the generic constructor
    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public BaseModel create(BaseModel model) {
        var createdModel = new UncheckedBase(spec, endpoint)
                .create(model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
        // Once entity is created its id is added to the list of created entities for further deletion
        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public BaseModel read(String id) {
        return new UncheckedBase(spec, endpoint)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public BaseModel update(String id, Object obj) {
        return new UncheckedBase(spec, endpoint)
                .update(id, obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public String delete(String id) {
        return new UncheckedBase(spec, endpoint)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

}
