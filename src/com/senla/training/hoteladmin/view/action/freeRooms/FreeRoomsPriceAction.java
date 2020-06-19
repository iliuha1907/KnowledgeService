package com.senla.training.hoteladmin.view.action.freeRooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.model.room.RoomsSortCriterion;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.view.IAction;

public class FreeRoomsPriceAction implements IAction {
    private RoomController roomController = new RoomController(
            RoomServiceImpl.getInstance(RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.PRICE));
    }
}

