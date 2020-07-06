package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;

public class AddRoomAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        RoomStatus roomStatus = userInteraction.getRoomStatus();
        if (roomStatus == null) {
            return;
        }

        BigDecimal price = userInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }

        Integer capacity = userInteraction.getNaturalIntWithMessage("Enter capacity");
        if (capacity == null) {
            return;
        }

        Integer stars = userInteraction.getNaturalIntWithMessage("Enter stars");
        if (stars == null) {
            return;
        }

        System.out.println(roomController.addRoom(roomStatus, price, capacity, stars));
    }
}

