package com.teamcity.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
// Fields with this annotation will be filled out with random data (implemented only for Strings)
public @interface Random {
}
