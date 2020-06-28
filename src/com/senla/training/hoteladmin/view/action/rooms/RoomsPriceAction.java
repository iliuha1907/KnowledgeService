package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.service.RoomWriterImpl;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.view.IAction;

public class RoomsPriceAction implements IAction {
    private RoomController roomController =  RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE));
    }
}

