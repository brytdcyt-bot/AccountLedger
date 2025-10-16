// --- GetMapping.java ---
package com.pluralsight.web;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetMapping {
    String value() default "/";
}