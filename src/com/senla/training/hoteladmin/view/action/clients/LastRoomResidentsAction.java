package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class LastRoomResidentsAction implements IAction {
    private ClientController clientController =  ClientController.getInstance(ClientServiceImpl.
            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                    HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                    ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter id of the room");
        Integer roomId;
        try {
            roomId = userInteraction.getInt();
            if (roomId < 1) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong id");
            return;
        }
        System.out.println(clientController.getLastThreeResidents(roomId));
    }
}

