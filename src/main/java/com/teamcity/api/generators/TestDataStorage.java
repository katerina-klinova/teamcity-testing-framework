package com.teamcity.api.generators;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public final class TestDataStorage {

    private static TestDataStorage testDataStorage;
    /* EnumMap holds the created entitied mapping them to endpoints (several entities per 1 endpoint).
    When calling the elements in EnumMap with e.g. .forEach(), the order is guaranteed to match the order in Enum file.
    For the Endpoint class it will always be BUILD_QUEUE > BUILDS > BUILD_TYPES > USERS > PROJECTS,
    event if elements are added to Map in some different order. This valuable to guarantee deletion in the correct order
    to e.g. not try deleting build type after project when it doesn't exist already, etc.*/
    private final EnumMap<Endpoint, Set<String>> createdEntitiesMap;

    private TestDataStorage() {
        createdEntitiesMap = new EnumMap<>(Endpoint.class);
    }

    public static TestDataStorage getStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    /* Add the created entity id into Map for further clean up.
    .computeIfAbsent() creates an empty HashSet if this endpoint doesn't have a Map yet.
    After this the new id is added to the new or the existing Map.*/
    public void addCreatedEntity(Endpoint endpoint, String id) {
        createdEntitiesMap.computeIfAbsent(endpoint, key -> new HashSet<>()).add(id);
    }

    public void addCreatedEntity(Endpoint endpoint, BaseModel model) {
        addCreatedEntity(endpoint, getEntityId(model));
    }

    public void deleteCreatedEntities() {
        createdEntitiesMap.forEach((endpoint, ids) -> ids.forEach(id ->
                new UncheckedBase(Specifications.getSpec().superUserSpec(), endpoint).delete(id)));
        // It's necessary to clear Map as it will keep trying to delete the deleted entities if > 1 test runs
        createdEntitiesMap.clear();
    }

    // Reflection is used to ge the id field for the classes that extend BaseModel and don't have id field initially
    private String getEntityId(BaseModel model) {
        try {
            var idField = model.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            var idFieldValue = String.valueOf(idField.get(model));
            idField.setAccessible(false);
            return idFieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot get entity id", e);
        }
    }

}
