package dev.rage.argon.util.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    String[] names();

    String permission() default "";

    boolean async() default false;

    boolean hidden() default false;

    String description() default "No description!";

}
