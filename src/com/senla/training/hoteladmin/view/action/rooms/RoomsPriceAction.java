package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.model.room.RoomsSortCriterion;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.view.IAction;

public class RoomsPriceAction implements IAction {
    private RoomController roomController = new RoomController(
            RoomServiceImpl.getInstance(RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE));
    }
}

