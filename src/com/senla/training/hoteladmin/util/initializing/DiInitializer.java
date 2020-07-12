package com.senla.training.hoteladmin.util.initializing;

import com.senla.training.hoteladmin.annotation.ConfigProperty;
import com.senla.training.hoteladmin.annotation.NeedDiClass;
import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.repository.*;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.PackageScanner;
import com.senla.training.hoteladmin.view.Builder;
import com.senla.training.hoteladmin.view.MenuController;
import com.senla.training.hoteladmin.view.Navigator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class DiInitializer {
    private static final String PACKAGE_NAME = "com/senla/training/hoteladmin";
    private static final String BASE_PROPERTIES_FILE = "resources/app.properties";
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.roomsRepository")
    private static RoomsRepository roomsRepository;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.clientsRepository")
    private static ClientsRepository clientsRepository;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.hotelServiceRepository")
    private static HotelServiceRepository hotelServiceRepository;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.roomsService")
    private static RoomService roomService;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.clientsService")
    private static ClientService clientService;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.hotelServiceService")
    private static HotelServiceService hotelServiceService;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.roomsController")
    private static RoomController roomController;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.clientsController")
    private static ClientController clientController;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.hotelServiceController")
    private static HotelServiceController hotelServiceController;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.menuController")
    private static MenuController menuController;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.builder")
    private static Builder builder;
    @ConfigProperty(configName = BASE_PROPERTIES_FILE, propertyName = "diInitializer.navigator")
    private static Navigator navigator;

    public static void init() {
        List<Class<?>> classes = PackageScanner.find(PACKAGE_NAME);
        classes = classes.stream()
                .filter(element -> element.isAnnotationPresent(NeedDiClass.class))
                .collect(Collectors.toList());
        for (Class<?> element : classes) {
            Object instance = getInstance(element);
            initFields(instance);
        }
    }

    public static MenuController getMenuController() {
        return menuController;
    }

    private static Object getInstance(Class<?> element) {
        Object instance;
        instance = createRepositoryInstance(element);
        if (instance == null) {
            instance = createServiceInstance(element);
        }
        if (instance == null) {
            instance = createControllerInstance(element);
        }
        if (instance == null) {
            instance = createViewInstance(element);
        }
        if (instance == null) {
            throw new IncorrectWorkException("Could not init classes: error at creating instance");
        }
        return instance;
    }

    private static void initFields(Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                field.setAccessible(true);
                Object instance = getInstance(field.getType());
                initFields(instance);
                boolean isInited = initFieldRepository(field, element);
                if (!isInited) {
                    isInited = initFieldService(field, element);
                }
                if (!isInited) {
                    isInited = initFieldController(field, element);
                }
                if (!isInited) {
                    initFieldView(field, element);
                }
            }
        }
    }

    private static boolean initFieldRepository(Field field, Object element) {
        if (ClientsRepository.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, clientsRepository);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (RoomsRepository.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, roomsRepository);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (HotelServiceRepository.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, hotelServiceRepository);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        return false;
    }

    private static boolean initFieldService(Field field, Object element) {
        if (ClientService.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, clientService);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (RoomService.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, roomService);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (HotelServiceService.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, hotelServiceService);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        return false;
    }

    private static boolean initFieldController(Field field, Object element) {
        if (ClientController.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, clientController);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (RoomController.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, roomController);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (HotelServiceController.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, hotelServiceController);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        return false;
    }

    private static boolean initFieldView(Field field, Object element) {
        if (MenuController.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, menuController);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (Navigator.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, navigator);
            } catch (Exception ex) {
                throw new IncorrectWorkException("Could not init classes: error at setting field");
            }
            return true;
        }
        if (Builder.class.isAssignableFrom(field.getType())) {
            try {
                field.set(element, builder);
            } catch (Exception ex) {
                throw new BusinessException("Could not init classes: error at setting field");
            }
            return true;
        }
        return false;
    }

    private static Object createRepositoryInstance(Class<?> element) {
        if (ClientsRepository.class.isAssignableFrom(element)) {
            return clientsRepository;
        }
        if (RoomsRepository.class.isAssignableFrom(element)) {
            return roomsRepository;
        }
        if (HotelServiceRepository.class.isAssignableFrom(element)) {
            return hotelServiceRepository;
        }
        return null;
    }

    private static Object createServiceInstance(Class<?> element) {
        if (ClientService.class.isAssignableFrom(element)) {
            return clientService;
        }
        if (RoomService.class.isAssignableFrom(element)) {
            return roomService;
        }
        if (HotelServiceService.class.isAssignableFrom(element)) {
            return hotelServiceService;
        }
        return null;
    }

    private static Object createControllerInstance(Class<?> element) {
        if (ClientController.class.isAssignableFrom(element)) {
            return clientController;
        }
        if (RoomController.class.isAssignableFrom(element)) {
            return roomController;
        }
        if (HotelServiceController.class.isAssignableFrom(element)) {
            return hotelServiceController;
        }
        return null;
    }

    private static Object createViewInstance(Class<?> element) {
        if (MenuController.class.isAssignableFrom(element)) {
            return menuController;
        }
        if (Builder.class.isAssignableFrom(element)) {
            return builder;
        }
        if (Navigator.class.isAssignableFrom(element)) {
            return navigator;
        }
        return null;
    }
}

