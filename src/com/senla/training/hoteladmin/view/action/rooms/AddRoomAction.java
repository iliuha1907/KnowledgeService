package com.senla.training.hotelAdmin.view.action.rooms;

import com.senla.training.hotelAdmin.controller.RoomController;
import com.senla.training.hotelAdmin.model.room.RoomStatus;
import com.senla.training.hotelAdmin.util.UserInteraction;
import com.senla.training.hotelAdmin.view.IAction;

import java.math.BigDecimal;

public class AddRoomAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        RoomStatus roomStatus;
        roomStatus = userInteraction.getRoomStatus();
        if (roomStatus == null) {
            System.out.println("Wrong status");
            return;
        }


        System.out.println("Enter price");
        BigDecimal price;
        price = userInteraction.getBigDecimal();
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Wrong price");
            return;
        }

        System.out.println("Enter capacity");
        Integer capacity;
        capacity = userInteraction.getInt();
        if (capacity == null || capacity < 1) {
            System.out.println("Wrong capacity");
            return;
        }

        System.out.println("Enter stars");
        Integer stars;
        stars = userInteraction.getInt();
        if (stars == null || stars < 0) {
            System.out.println("Wrong stars");
            return;
        }

        System.out.println(roomController.addRoom(roomStatus, price, capacity, stars));
    }
}

