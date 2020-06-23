package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.service.ArchivServiceImpl;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.service.ClientWriterImpl;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

import java.util.Date;

public class AddClientAction implements IAction {
    private ClientController clientController =  ClientController.getInstance(ClientServiceImpl.
            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance(), ClientWriterImpl.getInstance()));

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

