package com.senla.training.hotelAdmin.view.action.rooms;

import com.senla.training.hotelAdmin.controller.RoomController;
import com.senla.training.hotelAdmin.util.UserInteraction;
import com.senla.training.hotelAdmin.view.IAction;

import java.math.BigDecimal;

public class ChangeRoomPriceAction implements IAction {
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

        System.out.println("Enter price");
        BigDecimal price;
        price = userInteraction.getBigDecimal();
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Wrong price");
            return;
        }
        System.out.println(roomController.setRoomPrice(id, price));
    }
}

