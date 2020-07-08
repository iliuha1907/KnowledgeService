package com.senla.training.hoteladmin.view.action.hotelservice;

import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;
import java.util.Date;

public class AddHotelServiceAction implements IAction {
    private HotelServiceController hotelServiceController = HotelServiceController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        Integer id = userInteraction.getNaturalIntWithMessage("Enter id of the client");
        if (id == null) {
            return;
        }

        BigDecimal price = userInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }

        HotelServiceType type = userInteraction.getServiceType();
        if (type == null) {
            return;
        }

        Date date = UserInteraction.getInstance().getDateWithMessage("Enter date");
        if (date == null) {
            return;
        }

        System.out.println(hotelServiceController.addService(price, type, id, date));
    }
}

