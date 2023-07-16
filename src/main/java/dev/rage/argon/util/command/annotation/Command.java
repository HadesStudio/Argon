package dev.rage.argon.util.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Command {
	
	boolean async() default false;
	
    String[] names();
    
    String description() default "No provided description";
    
    String permission() default "";
    
    boolean hidden() default false;
}