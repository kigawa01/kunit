package net.kigawa.kutil.unit.annotation;

import java.lang.annotation.*;

@Repeatable(value = Dependencies.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency
{
    Class<?> value();

    String name() default "";
}
