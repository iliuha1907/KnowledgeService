package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.service.RoomWriterImpl;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class RoomDetailsAction implements IAction {
    private RoomController roomController =  RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println("Enter id of the room");
        try {
            System.out.println(roomController.getRoomInfo(UserInteraction.getInstance().getInt()));
        } catch (Exception ex) {
            System.out.println("Wrong id");
            return;
        }

    }
}

