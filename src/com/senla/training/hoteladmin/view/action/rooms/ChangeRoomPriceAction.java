package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.service.RoomWriterImpl;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;

public class ChangeRoomPriceAction implements IAction {
    private RoomController roomController =  RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter id of the room");
        Integer roomId;
        try {
            roomId = userInteraction.getInt();
            if (roomId < 1) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong id");
            return;
        }

        System.out.println("Enter price");
        BigDecimal price;
        try {
            price = userInteraction.getBigDecimal();
            if (price.compareTo(BigDecimal.ZERO) == -1) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong price");
            return;
        }
        System.out.println(roomController.setRoomPrice(roomId, price));
    }
}

