package com.senla.training.injection;

import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.injection.exception.IncorrectInitializationException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DependencyInjector {
    private static boolean isInited = false;
    private static Map<Class<?>, Object> instances;

    public static void init(String startPackage) {
        if (isInited) {
            throw new IncorrectInitializationException("Could not init classes: already initialized");
        }

        instances = new HashMap<>();
        List<Class<?>> classes = PackageScanner.findClasses(startPackage.replace(".", "/")).stream()
                .filter(element -> element.isAnnotationPresent(NeedInjectionClass.class))
                .collect(Collectors.toList());
        for (Class<?> element : classes) {
            Class<?>[] elementInterfaces = element.getInterfaces();
            try {
                Object instance = element.getDeclaredConstructor().newInstance();
                if (elementInterfaces.length > 0) {
                    instances.putIfAbsent(elementInterfaces[0], instance);
                }
                instances.put(element, instance);
            } catch (Exception ex) {
                throw new IncorrectInitializationException("Could not init classes");
            }
        }
        instances.forEach((key, value) -> initFields(value));
        isInited = true;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getClassInstance(Class<T> source) {
        T instance = (T) instances.get(source);
        if (instance == null) {
            throw new IncorrectInitializationException("Could not find class");
        }
        return instance;
    }

    private static void initFields(Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NeedInjectionField.class)) {
                Object instance = instances.get(field.getType());
                if (instance == null) {
                    throw new IncorrectInitializationException("Could not init classes: unknown type");
                }
                try {
                    field.setAccessible(true);
                    field.set(element, instance);
                } catch (IllegalAccessException ex) {
                    throw new IncorrectInitializationException("Could not init classes");
                }
            }
        }
        ParametersConfigurator.init(element);
    }
}

