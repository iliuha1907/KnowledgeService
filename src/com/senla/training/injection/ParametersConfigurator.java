package com.senla.training.injection;

import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.exception.IncorrectInitializationException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ParametersConfigurator {
    private static final String DEFAULT_FILE_NAME = "app.properties";

    public static void init(Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                String name = annotation.propertyName();
                if (name.isEmpty()) {
                    name = element.getClass().getSimpleName() + "." + field.getName();
                }
                String configName = annotation.configName();
                if (configName.isEmpty()) {
                    configName = DEFAULT_FILE_NAME;
                }
                Type type = annotation.type();
                if (type.equals(Object.class)) {
                    type = field.getType();
                }
                Object value = getObjectByType(type, name, configName);
                setFieldWithValidation(field, element, value);
            }
        }
    }

    private static Object getObjectByType(Type type, String name, String configName) {
        if (type.equals(Integer.class)) {
            return PropertyDataProvider.getInt(name, configName);
        } else if (type.equals(Boolean.class)) {
            return PropertyDataProvider.getBoolean(name, configName);
        } else if (type.equals(String.class)) {
            return PropertyDataProvider.getString(name, configName);
        } else {
            throw new IncorrectInitializationException("Could not init parameters: unknown type");
        }
    }

    private static void setFieldWithValidation(Field field, Object object, Object value) {
        if (!field.getType().isInstance(value)) {
            throw new IncorrectInitializationException("Could not initialize classes: incompatible types");
        }
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException ex) {
            throw new IncorrectInitializationException("Could not initialize classes: incompatible types");
        }
    }
}

