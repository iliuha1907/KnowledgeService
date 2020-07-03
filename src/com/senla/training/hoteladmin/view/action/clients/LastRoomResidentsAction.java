package com.senla.training.hotelAdmin.view.action.clients;

import com.senla.training.hotelAdmin.controller.ClientController;
import com.senla.training.hotelAdmin.view.IAction;
import com.senla.training.hotelAdmin.util.UserInteraction;

public class LastRoomResidentsAction implements IAction {
    private ClientController clientController = ClientController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter id of the room");
        Integer roomId;

        roomId = userInteraction.getInt();
        if (roomId == null || roomId < 1) {
            System.out.println("Wrong id");
            return;
        }
        System.out.println(clientController.getLastThreeResidents(roomId));
    }
}

