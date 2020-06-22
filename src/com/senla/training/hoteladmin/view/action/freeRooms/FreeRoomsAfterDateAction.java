package com.senla.training.hoteladmin.view.action.freeRooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

import java.util.Date;

public class FreeRoomsAfterDateAction implements IAction {
    private RoomController roomController = RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println("Enter date");
        Date date;
        try {
            date = DateUtil.getDate(UserInteraction.getInstance().getString());
        } catch (Exception ex) {
            System.out.println("Wrong date");
            return;
        }
        System.out.println(roomController.getFreeRoomsAfterDate(date));
    }
}

