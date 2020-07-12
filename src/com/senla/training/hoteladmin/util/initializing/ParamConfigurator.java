package com.senla.training.hoteladmin.util.initializing;

import com.senla.training.hoteladmin.annotation.ConfigProperty;
import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.repository.*;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.fileproperties.PropertyDataProvider;
import com.senla.training.hoteladmin.util.fileproperties.PropertyNamesProvider;
import com.senla.training.hoteladmin.view.Builder;
import com.senla.training.hoteladmin.view.MenuController;
import com.senla.training.hoteladmin.view.Navigator;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ParamConfigurator {
    private static RoomsRepository roomsRepository;
    private static ClientsRepository clientsRepository;
    private static HotelServiceRepository hotelServiceRepository;
    private static RoomService roomService;
    private static ClientService clientService;
    private static HotelServiceService hotelServiceService;
    private static RoomController roomController;
    private static ClientController clientController;
    private static HotelServiceController hotelServiceController;
    private static MenuController menuController;
    private static Builder builder;
    private static Navigator navigator;

    public static void init(Object element) {
        Arrays.stream(element.getClass().getDeclaredFields()).forEach(field -> {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
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
                } else if (type.equals(String.class)) {
                    Object value = PropertyDataProvider.getString(name, configName);
                    setFieldWithValidation(field, element, value);
                } else {
                    String className = PropertyDataProvider.getString(name, configName);
                    field.setAccessible(true);
                    if (className == null) {
                        className = field.getType().getSimpleName();
                    }
                    //здесь boolean, чтобы не было лишних проверок, если поле уже было проставлено
                    boolean isInited = initRepository(className, field, element);
                    if (!isInited) {
                        isInited = initService(className, field, element);
                    }
                    if (!isInited) {
                        isInited = initController(className, field, element);
                    }
                    if (!isInited) {
                        initView(className, field, element);
                    }
                }
            }
        });
    }

    private static boolean initRepository(String className, Field field, Object element) {
        if (className.equals("ClientsRepositoryImpl") || className.equals("ClientsRepository")) {
            if (clientsRepository == null) {
                clientsRepository = new ClientsRepositoryImpl();
            }
            setFieldWithValidation(field, element, clientsRepository);
            return true;
        }
        if (className.equals("RoomsRepositoryImpl") || className.equals("RoomsRepository")) {
            if (roomsRepository == null) {
                roomsRepository = new RoomsRepositoryImpl();
            }
            setFieldWithValidation(field, element, roomsRepository);
            return true;
        }
        if (className.equals("HotelServiceRepositoryImpl") || className.equals("HotelServiceRepository")) {
            if (hotelServiceRepository == null) {
                hotelServiceRepository = new HotelServiceRepositoryImpl();
            }
            setFieldWithValidation(field, element, hotelServiceRepository);
            return true;
        }
        return false;
    }

    private static boolean initService(String className, Field field, Object element) {
        if (className.equals("ClientServiceImpl") || className.equals("ClientService")) {
            if (clientService == null) {
                clientService = new ClientServiceImpl();
                init(clientService);
            }
            setFieldWithValidation(field, element, clientService);
            return true;
        }
        if (className.equals("RoomServiceImpl") || className.equals("RoomService")) {
            if (roomService == null) {
                roomService = new RoomServiceImpl();
                init(roomService);
            }
            setFieldWithValidation(field, element, roomService);
            return true;
        }
        if (className.equals("HotelServiceServiceImpl") || className.equals("HotelServiceService")) {
            if (hotelServiceService == null) {
                hotelServiceService = new HotelServiceServiceImpl();
                init(roomService);
            }
            setFieldWithValidation(field, element, hotelServiceService);
            return true;
        }
        return false;
    }

    private static boolean initController(String className, Field field, Object element) {
        if (className.equals("ClientController")) {
            if (clientController == null) {
                clientController = new ClientController();
                init(clientController);
            }
            setFieldWithValidation(field, element, clientController);
            return true;
        }
        if (className.equals("RoomController")) {
            if (roomController == null) {
                roomController = new RoomController();
                init(roomController);
            }
            setFieldWithValidation(field, element, roomController);
            return true;
        }
        if (className.equals("HotelServiceController")) {
            if (hotelServiceController == null) {
                hotelServiceController = new HotelServiceController();
                init(roomController);
            }
            setFieldWithValidation(field, element, hotelServiceController);
            return true;
        }
        return false;
    }

    private static boolean initView(String className, Field field, Object element) {
        if (className.equals("Builder")) {
            if (builder == null) {
                builder = new Builder();
                init(builder);
            }
            setFieldWithValidation(field, element, builder);
            return true;
        }
        if (className.equals("Navigator")) {
            if (navigator == null) {
                navigator = new Navigator();
                init(navigator);
            }
            setFieldWithValidation(field, element, navigator);
            return true;
        }
        if (className.equals("MenuController")) {
            if (menuController == null) {
                menuController = new MenuController();
                init(menuController);
            }
            setFieldWithValidation(field, element, menuController);
            return true;
        }
        return false;
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

