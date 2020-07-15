package com.senla.training.injection;

import com.senla.training.injection.annotation.InterfaceOfInjection;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyInjector {
    private static final String PACKAGE_NAME = "com/senla/training/hoteladmin";
    private Map<Class<?>, Object> instances;
    private Set<Class<?>> interfaces;

    public DependencyInjector() {
        instances = new HashMap<>();
    }

    public void init() {
        List<Class<?>> classes = PackageScanner.find(PACKAGE_NAME);
        interfaces = classes.stream()
                .filter(element -> element.isAnnotationPresent(InterfaceOfInjection.class))
                .collect(Collectors.toSet());
        classes = classes.stream()
                .filter(element -> element.isAnnotationPresent(NeedInjectionClass.class))
                .collect(Collectors.toList());
        for (Class<?> element : classes) {
            Class<?>[] elementInterfaces = element.getInterfaces();
            if (elementInterfaces.length > 0 && interfaces.contains(elementInterfaces[0])) {
                try {
                    instances.put(elementInterfaces[0], element.getDeclaredConstructor().newInstance());
                } catch (Exception ex) {
                    throw new IncorrectWorkException("Could not init classes");
                }
            } else {
                try {
                    instances.put(element, element.getDeclaredConstructor().newInstance());
                } catch (Exception ex) {
                    throw new IncorrectWorkException("Could not init classes");
                }
            }
        }
        instances.forEach((key, value) -> initFields(value));
    }

    public Object getStartClass(Class<?> source) {
        Class<?>[] sourceInterfaces = source.getInterfaces();
        if (sourceInterfaces.length > 0) {
            return instances.get(sourceInterfaces[0]);
        } else {
            return instances.get(source);
        }
    }

    private void initFields(Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NeedInjectionField.class)) {
                field.setAccessible(true);
                Object instance = instances.get(field.getType());
                try {
                    field.set(element, instance);
                } catch (IllegalAccessException e) {
                    throw new IncorrectWorkException("Could not init classes");
                }
            }
        }
        ParamConfigurator.init(element);
    }
}

