package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

import java.util.Date;

public class AddClientAction implements IAction {
    private ClientController clientController = ClientController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        String firstName = userInteraction.getStringWithMessage("Enter first name");
        String lastName = userInteraction.getStringWithMessage("Enter last name");

        Date arrival = userInteraction.getDateWithMessage("Enter arrival date");
        if (arrival == null) {
            return;
        }

        Date departure = userInteraction.getDateWithMessage("Enter departure date");
        if (departure == null) {
            return;
        }

        System.out.println(clientController.addResident(firstName, lastName, arrival, departure));
    }
}

