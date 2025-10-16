// --- PostMapping.java ---
package com.pluralsight.web;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostMapping {
    String value() default "/";
}
