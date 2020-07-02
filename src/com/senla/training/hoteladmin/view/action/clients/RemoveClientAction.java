package com.senla.training.hotelAdmin.view.action.clients;

import com.senla.training.hotelAdmin.controller.ClientController;
import com.senla.training.hotelAdmin.util.UserInteraction;
import com.senla.training.hotelAdmin.view.IAction;

public class RemoveClientAction implements IAction {
    private ClientController clientController = ClientController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();
        System.out.println("Enter id of the client");
        Integer id;

        id = userInteraction.getInt();
        if (id == null || id < 0) {
            System.out.println("Wrong id");
            return;
        }
        System.out.println(clientController.removeResident(id));
    }
}

