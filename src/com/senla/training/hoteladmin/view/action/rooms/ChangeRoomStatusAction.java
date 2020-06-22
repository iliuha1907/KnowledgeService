package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

public class ChangeRoomStatusAction implements IAction {
    private RoomController roomController =  RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter number of the room");
        Integer roomNumber;
        try {
            roomNumber = userInteraction.getInt();
        } catch (Exception ex) {
            System.out.println("Wrong number");
            return;
        }

        RoomStatus roomStatus;
        try {
            roomStatus = userInteraction.getRoomStatus();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        System.out.println(roomController.setRoomStatus(roomNumber, roomStatus));
    }
}

