package com.senla.training.hotelAdmin.view.action.rooms;

import com.senla.training.hotelAdmin.controller.RoomController;
import com.senla.training.hotelAdmin.view.IAction;
import com.senla.training.hotelAdmin.util.UserInteraction;

public class RoomDetailsAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        Integer id;
        id = UserInteraction.getInstance().getInt();
        if (id == null || id < 0) {
            System.out.println("Wrong id");
            return;
        }
        System.out.println(roomController.getRoomInfo(id));
    }
}

