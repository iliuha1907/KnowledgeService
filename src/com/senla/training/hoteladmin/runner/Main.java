package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.MenuController;
import injection.DependencyInjector;
import injection.annotation.NeedInjectionClass;
import injection.annotation.NeedInjectionField;

@NeedInjectionClass
public class Main {
    @NeedInjectionField
    private static RoomController roomController;
    @NeedInjectionField
    private static ClientController clientController;
    @NeedInjectionField
    private static HotelServiceController hotelServiceController;

    public static void main(String[] args) {
        UserInteraction.startWorking();
        DependencyInjector dependencyInjector = new DependencyInjector();
        try {
            dependencyInjector.init("com/senla/training/hoteladmin");
            MenuController menuController = dependencyInjector.getClassInstance(MenuController.class);
            if (menuController == null) {
                System.out.println("Could not get MenuController, shutting app down");
            } else {
                deserializeData();
                menuController.run();
                serializeData();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
        }
    }

    private static void deserializeData() {
        System.out.println(roomController.deserializeRoomsClients());
        System.out.println(hotelServiceController.deserializeHotelServices());
        System.out.println(clientController.deserializeLastResidents());
        System.out.println(roomController.deserializeRoomsId());
        System.out.println(hotelServiceController.deserializeServicesId());
        System.out.println(clientController.deserializeClientsId());
    }

    private static void serializeData() {
        System.out.println(roomController.serializeRoomsClients());
        System.out.println(clientController.serializeLastResidents());
        System.out.println(hotelServiceController.serializeHotelServices());
        System.out.println(roomController.serializeRoomsId());
        System.out.println(clientController.serializeClientId());
        System.out.println(hotelServiceController.serializeServicesId());
    }
}

