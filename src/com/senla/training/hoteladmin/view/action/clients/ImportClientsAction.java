package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.repo.*;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.view.IAction;

public class ImportClientsAction implements IAction {
    private ClientController clientController =  ClientController.getInstance(ClientServiceImpl.
            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(),ServiceWriterImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(clientController.importClients());
    }
}

