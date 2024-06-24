package com.teamcity.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
// Fields with this annotation will be generated automatically when their parent or ancestor classes are generated
// These fields need to be set manually (see Steps in api.StartBuildTest.userStartsBuildTest)
public @interface Optional {
}
