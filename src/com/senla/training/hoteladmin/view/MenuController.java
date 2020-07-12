package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.annotation.ConfigProperty;
import com.senla.training.hoteladmin.annotation.NeedDiClass;
import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.util.UserInteraction;

@NeedDiClass
public class MenuController {
    @ConfigProperty
    private Builder builder;
    @ConfigProperty
    private Navigator navigator;
    @ConfigProperty
    private RoomController roomController;
    @ConfigProperty
    private ClientController clientController;
    @ConfigProperty
    private HotelServiceController hotelServiceController;

    public MenuController() {
    }

    public void run() {
        System.out.println(roomController.deserializeRoomsClients());
        System.out.println(hotelServiceController.deserializeHotelServices());
        System.out.println(clientController.deserializeLastResidents());
        System.out.println(roomController.deserializeRoomsId());
        System.out.println(hotelServiceController.deserializeServicesId());
        System.out.println(clientController.deserializeClientsId());

        builder.buildMenu();
        navigator.setCurrentMenu(builder.getRootMenu());
        boolean stop = false;
        while (!stop) {
            navigator.printMenu();
            Integer choice = UserInteraction.getInt();
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


        System.out.println(roomController.serializeRoomsClients());
        System.out.println(clientController.serializeLastResidents());
        System.out.println(hotelServiceController.serializeHotelServices());
        System.out.println(roomController.serializeRoomsId());
        System.out.println(clientController.serializeClientId());
        System.out.println(hotelServiceController.serializeServicesId());
    }
}

