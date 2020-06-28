package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

import java.util.Date;

public class AddClientAction implements IAction {
    private ClientController clientController =  ClientController.getInstance(ClientServiceImpl.
            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                    HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                    ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter first name");
        String firstName = userInteraction.getString();
        System.out.println("Enter last name");
        String lastName = userInteraction.getString();

        System.out.println("Enter arrival date");
        Date arrival;
        try {
            arrival = DateUtil.getDate(userInteraction.getString());
        } catch (Exception ex) {
            System.out.println("Wrong arrival date");
            return;
        }

        System.out.println("Enter departure date");
        Date departure;
        try {
            departure = DateUtil.getDate(userInteraction.getString());
        } catch (Exception ex) {
            System.out.println("Wrong departure date");
            return;
        }
        System.out.println(clientController.addResident(firstName, lastName, arrival, departure));
    }
}

