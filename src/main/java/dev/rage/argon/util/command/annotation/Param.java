package dev.rage.argon.util.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {

    String name();

    String defaultValue() default "";

    boolean wildcard() default false;

    String[] completionFlags() default {};

}
