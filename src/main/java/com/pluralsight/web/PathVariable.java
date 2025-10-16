// --- PathVariable.java ---
package com.pluralsight.web;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathVariable {
    String value() default "";
}