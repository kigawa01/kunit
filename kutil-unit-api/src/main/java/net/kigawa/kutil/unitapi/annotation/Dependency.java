package net.kigawa.kutil.unitapi.annotation;

import java.lang.annotation.*;

@Repeatable(value = Dependencies.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency
{
    Class<?> value();

    String name() default "";
}
