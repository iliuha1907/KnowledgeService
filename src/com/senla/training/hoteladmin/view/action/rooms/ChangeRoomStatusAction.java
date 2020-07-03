package com.senla.training.hotelAdmin.view.action.rooms;

import com.senla.training.hotelAdmin.controller.RoomController;
import com.senla.training.hotelAdmin.model.room.RoomStatus;
import com.senla.training.hotelAdmin.util.UserInteraction;
import com.senla.training.hotelAdmin.view.IAction;

public class ChangeRoomStatusAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter id of the room");
        Integer id;
        id = userInteraction.getInt();
        if (id == null || id < 0) {
            System.out.println("Wrong id");
            return;
        }

        RoomStatus roomStatus;
        roomStatus = userInteraction.getRoomStatus();
        if (roomStatus == null) {
            System.out.println("Wrong status");
            return;
        }
        System.out.println(roomController.setRoomStatus(id, roomStatus));
    }
}

