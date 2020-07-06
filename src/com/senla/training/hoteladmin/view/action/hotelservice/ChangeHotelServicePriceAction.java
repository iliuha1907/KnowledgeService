package com.senla.training.hoteladmin.view.action.hotelservice;

import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;

public class ChangeHotelServicePriceAction implements IAction {
    private HotelServiceController hotelServiceController = HotelServiceController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        Integer id = userInteraction.getNaturalIntWithMessage("Enter id");
        if (id == null) {
            return;
        }

        BigDecimal price = userInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }

        System.out.println(hotelServiceController.setServicePrice(id, price));
    }
}

