package com.teamcity.api.generators;

import com.teamcity.api.annotations.Optional;
import com.teamcity.api.annotations.Parameterizable;
import com.teamcity.api.annotations.Random;
import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public final class TestDataGenerator {

    private TestDataGenerator() {
    }

    /* The main test data generator method. If the field is annotated with Optional, it is skipped,
    otherwise there is a choice:
    1) if the field is annotated with Parameterizable and parameters were passed to the method
    then in the order of generating the Parameterizable fields the passed parameters are set. This means that
    if there are 4 Parameterizable fields, and it is only passed 3 parameters then values will be set only for
    the first 3 of the fields in the order the method works through the fields. Thus, it is important
    to pay attention to the order of fields in the @Data class;
    2) otherwise if the filed is annotated with Random и is a String it is populated with random data;
    3) otherwise if the fields extends BaseModel, it is generated and recursively passed to the new generate() method;
    4) otherwise if the field is of List type and has a generic type that extends BaseModel, it's value
    is set to 1 element that is generated and recursively passed to the new generate() method.
    Parameter generatedModels is passed when several entities are generated in a single cycle and contains the entities,
     generated at previous steps. It allows to reuse a generated entity that may be a field to another complex entity
     instead of generating a new one. This logic applies only to bullets 3 и 4.
    E.g., when NewProjectDescription is generated, it is passed to generatedModels as a parameter when generating a BuildType,
    it will be reused when setting value for NewProjectDescription project instead of generating a new one */
    public static BaseModel generate(Collection<BaseModel> generatedModels, Class<? extends BaseModel> generatorClass, Object... parameters) {
        try {
            var instance = generatorClass.getDeclaredConstructor().newInstance();
            for (var field : generatorClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Optional.class)) {
                    var generatedClass = generatedModels.stream().filter(m
                            -> m.getClass().equals(field.getType())).findFirst();
                    if (field.isAnnotationPresent(Parameterizable.class) && parameters.length > 0) {
                        field.set(instance, parameters[0]);
                        parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
                    } else if (field.isAnnotationPresent(Random.class) && String.class.equals(field.getType())) {
                        field.set(instance, RandomData.getString());
                    } else if (BaseModel.class.isAssignableFrom(field.getType())) {
                        var finalParameters = parameters;
                        field.set(instance, generatedClass.orElseGet(() -> generate(
                                generatedModels, field.getType().asSubclass(BaseModel.class), finalParameters)));
                    } else if (List.class.isAssignableFrom(field.getType())) {
                        if (field.getGenericType() instanceof ParameterizedType pt) {
                            var typeClass = (Class<?>) pt.getActualTypeArguments()[0];
                            if (BaseModel.class.isAssignableFrom(typeClass)) {
                                var finalParameters = parameters;
                                field.set(instance, generatedClass.map(List::of).orElseGet(() -> List.of(generate(
                                        generatedModels, typeClass.asSubclass(BaseModel.class), finalParameters))));
                            }
                        }
                    }
                }
                field.setAccessible(false);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new IllegalStateException("Cannot generate test data", e);
        }
    }

    // Method generates a single entity. Passes an empty parameter to generatedModels argument
    public static BaseModel generate(Class<? extends BaseModel> generatorClass, Object... parameters) {
        return generate(Collections.emptyList(), generatorClass, parameters);
    }

    /* Generates all entities that have a generatorClass in Endpoint. Makes Endpoint.class the only point of scaling.
     It's enough to only create a new endpoint to start generating a new object in test data. Used in the order
     that is descending in comparison to EnumMap. See details on the EnumMap logic in the comment to TestDataStorage.createdEntitiesMap */
    public static EnumMap<Endpoint, BaseModel> generate() {
        var generatedTestData = new EnumMap<Endpoint, BaseModel>(Endpoint.class);
        Arrays.stream(Endpoint.values()).filter(e -> e.getGeneratorClass() != null).sorted(Comparator.reverseOrder())
                .forEach(endpoint -> generatedTestData.put(endpoint, generate(generatedTestData.values(), endpoint.getGeneratorClass())));
        return generatedTestData;
    }

}
