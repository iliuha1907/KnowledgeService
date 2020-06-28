package com.senla.training.hoteladmin.view.action.freeRooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.service.RoomWriterImpl;
import com.senla.training.hoteladmin.view.IAction;

public class FreeRoomsCountAction implements IAction {
    private RoomController roomController =  RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(roomController.getNumberOfFreeRooms());
    }

}

