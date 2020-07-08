package com.senla.training.hoteladmin.view.action.freerooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

import java.util.Date;

public class FreeRoomsAfterDateAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        Date date = UserInteraction.getInstance().getDateWithMessage("Enter date");
        if (date == null) {
            return;
        }
        System.out.println(roomController.getFreeRoomsAfterDate(date));
    }
}

