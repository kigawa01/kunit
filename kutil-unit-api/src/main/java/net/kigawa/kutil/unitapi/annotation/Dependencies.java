package net.kigawa.kutil.unitapi.annotation;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies
{
    Dependency[] value() default {} ;
}
