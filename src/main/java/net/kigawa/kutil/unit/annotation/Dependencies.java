package net.kigawa.kutil.unit.annotation;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies
{
    Dependency[] value() default {} ;
}
