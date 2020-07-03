package com.senla.training.hotelAdmin.view.action.clients;

import com.senla.training.hotelAdmin.controller.ClientController;
import com.senla.training.hotelAdmin.util.DateUtil;
import com.senla.training.hotelAdmin.view.IAction;
import com.senla.training.hotelAdmin.util.UserInteraction;

import java.util.Date;

public class AddClientAction implements IAction {
    private ClientController clientController = ClientController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter first name");
        String firstName = userInteraction.getString();
        System.out.println("Enter last name");
        String lastName = userInteraction.getString();

        System.out.println("Enter arrival date");
        Date arrival;
        arrival = DateUtil.getDate(userInteraction.getString());
        if (arrival == null) {
            System.out.println("Wrong arrival date");
            return;
        }

        System.out.println("Enter departure date");
        Date departure;

        departure = DateUtil.getDate(userInteraction.getString());
        if (departure == null) {
            System.out.println("Wrong departure date");
            return;
        }

        System.out.println(clientController.addResident(firstName, lastName, arrival, departure));
    }
}

