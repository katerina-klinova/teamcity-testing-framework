package com.teamcity.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
// Fields with this annotation will be parametrized when generated only if parameters were passed
// See api.BuildTypeTest.projectAdminCreatesBuildTypeForAnotherUserProjectTest
public @interface Parameterizable {
}
