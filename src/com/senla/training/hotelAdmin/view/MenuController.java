package com.senla.training.hotelAdmin.view;

import com.senla.training.hotelAdmin.controller.ClientController;
import com.senla.training.hotelAdmin.controller.HotelServiceController;
import com.senla.training.hotelAdmin.controller.RoomController;
import com.senla.training.hotelAdmin.util.UserInteraction;

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

        builder.buildMenu();
        navigator = Navigator.getInstance(builder.getRootMenu());
        UserInteraction userInteraction = UserInteraction.getInstance();
        boolean stop = false;
        while (!stop) {
            navigator.printMenu();
            Integer choice = userInteraction.getInt() - 1;
            if (choice == null) {
                System.out.println("Wrong input");
                continue;
            }
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
    }
}

