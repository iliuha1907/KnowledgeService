package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;

public class ChangeRoomPriceAction implements IAction {
    private RoomController roomController = RoomController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        Integer id = userInteraction.getNaturalIntWithMessage("Enter id of the room");
        if (id == null) {
            return;
        }

        BigDecimal price = userInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }

        System.out.println(roomController.setRoomPrice(id, price));
    }
}

