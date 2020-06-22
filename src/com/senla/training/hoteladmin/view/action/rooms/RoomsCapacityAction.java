package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.view.IAction;

public class RoomsCapacityAction implements IAction {
    private RoomController roomController = RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(roomController.getSortedRooms(RoomsSortCriterion.CAPACITY));
    }
}

