package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.view.IAction;

public class ClientsAlphabetAction implements IAction {
    private ClientController clientController =  ClientController.getInstance(ClientServiceImpl.
            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                    HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                    ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(clientController.getSortedClients(ClientsSortCriterion.ALPHABET));
    }
}

