package com.senla.training.hotelAdmin.view.action.hotelService;

import com.senla.training.hotelAdmin.controller.HotelServiceController;
import com.senla.training.hotelAdmin.util.UserInteraction;
import com.senla.training.hotelAdmin.view.IAction;

import java.math.BigDecimal;

public class ChangeHotelServicePriceAction implements IAction {
    private HotelServiceController hotelServiceController = HotelServiceController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        System.out.println("Enter id");
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

        System.out.println(hotelServiceController.setServicePrice(id, price));
    }
}

