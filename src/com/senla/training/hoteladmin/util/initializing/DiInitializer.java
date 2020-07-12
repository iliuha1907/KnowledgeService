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
    private final String packageName = "com/senla/training/hoteladmin";
    private final String basePropertiesFile = "resources/app.properties";
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.roomsRepository")
    private RoomsRepository roomsRepository;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.clientsRepository")
    private ClientsRepository clientsRepository;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.hotelServiceRepository")
    private HotelServiceRepository hotelServiceRepository;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.roomsService")
    private RoomService roomService;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.clientsService")
    private ClientService clientService;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.hotelServiceService")
    private HotelServiceService hotelServiceService;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.roomsController")
    private RoomController roomController;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.clientsController")
    private ClientController clientController;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.hotelServiceController")
    private HotelServiceController hotelServiceController;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.menuController")
    private MenuController menuController;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.builder")
    private Builder builder;
    @ConfigProperty(configName = basePropertiesFile, propertyName = "diInitializer.navigator")
    private Navigator navigator;

    public void init() {
        List<Class<?>> classes = PackageScanner.find(packageName);
        classes = classes.stream()
                .filter(element -> element.isAnnotationPresent(NeedDiClass.class))
                .collect(Collectors.toList());
        for (Class<?> element : classes) {
            Object instance = getInstance(element);
            initFields(instance);
        }
    }

    public MenuController getMenuController() {
        return menuController;
    }

    private Object getInstance(Class<?> element) {
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

    private void initFields(Object element) {
        for (Field field : element.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                field.setAccessible(true);
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

    private boolean initFieldRepository(Field field, Object element) {
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

    private boolean initFieldService(Field field, Object element) {
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

    private boolean initFieldController(Field field, Object element) {
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

    private boolean initFieldView(Field field, Object element) {
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

    private Object createRepositoryInstance(Class<?> element) {
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

    private Object createServiceInstance(Class<?> element) {
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

    private Object createControllerInstance(Class<?> element) {
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

    private Object createViewInstance(Class<?> element) {
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

