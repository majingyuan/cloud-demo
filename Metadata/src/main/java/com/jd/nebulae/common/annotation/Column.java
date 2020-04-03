package com.jd.nebulae.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String value() default "";

    boolean isTransient() default false;

    boolean isPrimaryKey() default false;

    boolean autoCurrentTimestamp() default false;
}
