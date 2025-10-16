package com.pluralsight.Interfaces;

public @interface AliasFor {
    Class<EnableAutoConfiguration> annotation();

    String attribute();
}
