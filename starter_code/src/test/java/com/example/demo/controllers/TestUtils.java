package com.example.demo.controllers;

import lombok.SneakyThrows;

public class TestUtils {
    @SneakyThrows
    public static void injectValue(Object target, String fieldName, Object value) {
        boolean wasPrivate = false;
        var declaredField = target.getClass().getDeclaredField(fieldName);
        if (!declaredField.isAccessible()) {
            declaredField.setAccessible(true);
            wasPrivate = true;
        }
        declaredField.set(target, value);
        if (wasPrivate) {
            declaredField.setAccessible(false);
        }
    }
}
