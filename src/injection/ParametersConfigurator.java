package injection;

import injection.annotation.ConfigProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ParametersConfigurator {
    private static final String DEFAULT_FILE_NAME = "app.properties";

    public static void init(Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                field.setAccessible(true);
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
                if (type.equals(Integer.class)) {
                    Object value = PropertyDataProvider.getInt(name, configName);
                    setFieldWithValidation(field, element, value);
                } else if (type.equals(Boolean.class)) {
                    Object value = PropertyDataProvider.getBoolean(name, configName);
                    setFieldWithValidation(field, element, value);
                } else if (type.equals(String.class)) {
                    Object value = PropertyDataProvider.getString(name, configName);
                    setFieldWithValidation(field, element, value);
                } else {
                    throw new IncorrectInitializationException("Could not init parameters: unknown type");
                }
                field.setAccessible(false);
            }
        }
    }

    private static void setFieldWithValidation(Field field, Object object, Object value) {
        if (!field.getType().isInstance(value)) {
            throw new IncorrectInitializationException("Could not initialize classes: incompatible types");
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException ex) {
            throw new IncorrectInitializationException("Could not initialize classes: incompatible types");
        }
    }
}

