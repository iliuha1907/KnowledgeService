package com.senla.training.hotelAdmin.view.action.freeRooms;

import com.senla.training.hotelAdmin.controller.RoomController;
import com.senla.training.hotelAdmin.util.DateUtil;
import com.senla.training.hotelAdmin.view.IAction;
import com.senla.training.hotelAdmin.util.UserInteraction;

import java.util.Date;

public class FreeRoomsAfterDateAction implements IAction {
    private RoomController roomController = RoomController.getInstance();


    @Override
    public void execute() {
        System.out.println("Enter date");
        Date date;

        date = DateUtil.getDate(UserInteraction.getInstance().getString());
        if (date == null) {
            System.out.println("Wrong date");
            return;
        }
        System.out.println(roomController.getFreeRoomsAfterDate(date));
    }
}

