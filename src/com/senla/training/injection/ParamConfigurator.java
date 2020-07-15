package com.senla.training.injection;

import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.util.fileproperties.PropertyDataProvider;
import com.senla.training.hoteladmin.util.fileproperties.PropertyNamesProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ParamConfigurator {

    public static void init(Object element) {
        Arrays.stream(element.getClass().getDeclaredFields()).forEach(field -> {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                field.setAccessible(true);
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                String name = annotation.propertyName();
                if (name.equals("")) {
                    name = element.getClass().getSimpleName() + "." + field.getName();
                }
                String configName = annotation.configName();
                if (configName.equals("")) {
                    configName = PropertyNamesProvider.FILE_NAME;
                }
                Type type = annotation.type();
                if (type.equals(Object.class)) {
                    type = field.getType();
                }
                if (type.equals(Integer.class)) {
                    Object value = PropertyDataProvider.getInt(name, configName);
                    setFieldWithValidation(field, element, value);
                } else if (type.equals(Boolean.class)) {
                    Object value = PropertyDataProvider.getBoolean(name, configName);
                    setFieldWithValidation(field, element, value);
                }
            }
        });
    }

    private static void setFieldWithValidation(Field field, Object object, Object value) {
        if (!field.getType().isInstance(value)) {
            throw new IncorrectWorkException("Could not initialize classes: incompatible types");
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException ex) {
            throw new IncorrectWorkException("Could not initialize classes: incompatible types");
        }
    }
}

