package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.MenuController;
import com.senla.training.injection.DependencyInjector;

public class Main {
    private static RoomController roomController;
    private static ClientController clientController;
    private static HotelServiceController hotelServiceController;
    private static MenuController menuController;

    public static void main(String[] args) {
        UserInteraction.startWorking();
        try {
            DependencyInjector.init("com/senla/training/hoteladmin");
            initControllers();
            deserializeData();
            menuController.run();
            serializeData();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
        }
    }

    private static void initControllers() {
        roomController = DependencyInjector.getClassInstance(RoomController.class);
        clientController = DependencyInjector.getClassInstance(ClientController.class);
        hotelServiceController = DependencyInjector.getClassInstance(HotelServiceController.class);
        menuController = DependencyInjector.getClassInstance(MenuController.class);
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

