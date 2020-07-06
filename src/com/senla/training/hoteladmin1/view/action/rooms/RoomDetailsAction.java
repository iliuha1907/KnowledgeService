package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class RoomDetailsAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        Integer id = UserInteraction.getInstance().getNaturalIntWithMessage("Enter id of the room");
        if (id == null) {
            return;
        }

        System.out.println(roomController.getRoomInfo(id));
    }
}

