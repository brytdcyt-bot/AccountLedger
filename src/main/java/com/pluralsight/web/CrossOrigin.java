// --- CrossOrigin.java ---
package com.pluralsight.web;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CrossOrigin {
    String[] origins() default "*";
}

