package com.senla.training.hotelAdmin.view.action.hotelService;

import com.senla.training.hotelAdmin.controller.HotelServiceController;
import com.senla.training.hotelAdmin.model.hotelService.HotelServiceType;
import com.senla.training.hotelAdmin.util.DateUtil;
import com.senla.training.hotelAdmin.util.UserInteraction;
import com.senla.training.hotelAdmin.view.IAction;

import java.math.BigDecimal;
import java.util.Date;

public class AddHotelServiceAction implements IAction {
    private HotelServiceController hotelServiceController = HotelServiceController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();
        System.out.println("Enter id of the client");
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

        HotelServiceType type;
        type = userInteraction.getServiceType();
        if (type == null) {
            System.out.println("Wrong type");
            return;
        }

        System.out.println("Enter date");
        Date date;
        date = DateUtil.getDate(userInteraction.getString());
        if (date == null) {
            System.out.println("Wrong date");
            return;
        }
        System.out.println(hotelServiceController.addService(price, type, id, date));
    }
}

