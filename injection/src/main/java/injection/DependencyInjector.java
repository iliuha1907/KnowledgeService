package injection;


import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import injection.exception.IncorrectInitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DependencyInjector {

    private static final Logger LOGGER = LogManager.getLogger(DependencyInjector.class);
    private static boolean isInited = false;
    private static Map<Class<?>, Object> instances;

    public static void init(final List<String> packages) {
        if (isInited) {
            LOGGER.error("Error at dependency injection already initialized");
            throw new IncorrectInitializationException("Could not init classes: already initialized");
        }
        instances = new HashMap<>();
        List<Class<?>> classes = new ArrayList<>();
        for (String folder : packages) {
            classes.addAll(PackageScanner.findClasses(folder.replace("//.", "/")).stream()
                    .filter(element -> element.isAnnotationPresent(NeedInjectionClass.class))
                    .collect(Collectors.toList()));
        }
        for (Class<?> element : classes) {
            Class<?>[] elementInterfaces = element.getInterfaces();
            try {
                Object instance = element.getDeclaredConstructor().newInstance();
                if (elementInterfaces.length > 0) {
                    instances.putIfAbsent(elementInterfaces[0], instance);
                }
                instances.put(element, instance);
            } catch (Exception ex) {
                LOGGER.error("Error at dependency injection " + ex.getMessage());
                throw new IncorrectInitializationException("Could not init classes");
            }
        }
        instances.forEach((key, value) -> initFields(value));
        isInited = true;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getClassInstance(final Class<T> source) {
        T instance = (T) instances.get(source);
        if (instance == null) {
            LOGGER.error("Error at getting class instance: Could not find class");
            throw new IncorrectInitializationException("Could not find class");
        }
        return instance;
    }

    private static void initFields(final Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NeedInjectionField.class)) {
                Object instance = instances.get(field.getType());
                if (instance == null) {
                    LOGGER.error("Error at init fields: Could not init classes: unknown type");
                    throw new IncorrectInitializationException("Could not init classes: unknown type");
                }
                try {
                    field.setAccessible(true);
                    field.set(element, instance);
                } catch (IllegalAccessException ex) {
                    LOGGER.error("Error at init fields: " + ex.getMessage());
                    throw new IncorrectInitializationException("Could not init classes");
                }
            }
        }
        ParametersConfigurator.init(element);
    }
}

