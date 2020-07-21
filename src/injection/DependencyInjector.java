package injection;

import injection.annotation.NeedInjectionClass;
import injection.annotation.NeedInjectionField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DependencyInjector {
    private static Map<Class<?>, Object> instances;

    public static void init(String startPackagePath) {
        instances = new HashMap<>();
        List<Class<?>> classes = PackageScanner.findClasses(startPackagePath);
        classes = classes.stream()
                .filter(element -> element.isAnnotationPresent(NeedInjectionClass.class))
                .collect(Collectors.toList());
        for (Class<?> element : classes) {
            Class<?>[] elementInterfaces = element.getInterfaces();
            try {
                if (elementInterfaces.length > 0) {
                    Object instance = element.getDeclaredConstructor().newInstance();
                    instances.putIfAbsent(elementInterfaces[0], instance);
                    instances.put(element, instance);
                } else {
                    instances.put(element, element.getDeclaredConstructor().newInstance());
                }
            } catch (Exception ex) {
                throw new IncorrectInitializationException("Could not init classes");
            }

        }
        instances.forEach((key, value) -> initFields(value));
    }

    public static  <T> T getClassInstance(Class<T> source) {
        return (T) instances.get(source);
    }

    private static void initFields(Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NeedInjectionField.class)) {
                field.setAccessible(true);
                Object instance = instances.get(field.getType());
                if (instance == null) {
                    throw new IncorrectInitializationException("Could not init classes: unknown type");
                }
                try {
                    field.set(element, instance);
                } catch (IllegalAccessException ex) {
                    throw new IncorrectInitializationException("Could not init classes");
                }
                field.setAccessible(false);
            }
        }
        ParametersConfigurator.init(element);
    }
}

