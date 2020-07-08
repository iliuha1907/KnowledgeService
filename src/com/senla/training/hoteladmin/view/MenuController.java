package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.util.UserInteraction;

public class MenuController {
    private static MenuController instance;
    private Builder builder;
    private Navigator navigator;
    private RoomController roomController;
    private ClientController clientController;
    private HotelServiceController hotelServiceController;

    private MenuController(Builder builder) {
        this.builder = builder;
        roomController = RoomController.getInstance();
        clientController = ClientController.getInstance();
        hotelServiceController = HotelServiceController.getInstance();
    }

    public static MenuController getInstance(Builder builder) {
        if (instance == null) {
            instance = new MenuController(builder);
        }
        return instance;
    }

    public void run() {
        System.out.println(roomController.deserializeRoomsClients());
        System.out.println(hotelServiceController.deserializeHotelServices());
        System.out.println(clientController.deserializeLastResidents());
        System.out.println(roomController.deserializeRoomsId());
        System.out.println(hotelServiceController.deserializeServicesId());
        System.out.println(clientController.deserializeClientsId());

        builder.buildMenu();
        navigator = Navigator.getInstance(builder.getRootMenu());
        UserInteraction userInteraction = UserInteraction.getInstance();
        boolean stop = false;
        while (!stop) {
            navigator.printMenu();
            Integer choice = userInteraction.getInt();
            if (choice == null) {
                System.out.println("Wrong input");
                continue;
            }
            choice -= 1;
            if (choice.equals(navigator.getCurrentMenu().getMenuItems().size())) {
                stop = true;
            } else {
                navigator.navigate(choice);
            }
        }
        userInteraction.stopWorking();

        System.out.println(roomController.serializeRoomsClients());
        System.out.println(clientController.serializeLastResidents());
        System.out.println(hotelServiceController.serializeHotelServices());
        System.out.println(roomController.serializeRoomsId());
        System.out.println(clientController.serializeClientId());
        System.out.println(hotelServiceController.serializeServicesId());
    }
}

