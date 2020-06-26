package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SvcRepoImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

public class RemoveClientAction implements IAction {
    private ClientController clientController =  ClientController.getInstance(ClientServiceImpl.
            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(), ServiceWriterImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();
        System.out.println("Enter id of the client");
        Integer id;
        try {
            id = userInteraction.getInt();
            if (id < 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong id");
            return;
        }
        System.out.println(clientController.removeResident(id));
    }
}

