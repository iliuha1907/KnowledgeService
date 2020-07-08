package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

public class ChangeRoomStatusAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        Integer id = userInteraction.getNaturalIntWithMessage("Enter id of the room");
        if (id == null) {
            return;
        }

        RoomStatus roomStatus = userInteraction.getRoomStatus();
        if (roomStatus == null) {
            return;
        }

        System.out.println(roomController.setRoomStatus(id, roomStatus));
    }
}

