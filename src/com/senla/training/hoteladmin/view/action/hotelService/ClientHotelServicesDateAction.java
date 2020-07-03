package com.senla.training.hotelAdmin.view.action.hotelService;

import com.senla.training.hotelAdmin.controller.HotelServiceController;
import com.senla.training.hotelAdmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hotelAdmin.view.IAction;
import com.senla.training.hotelAdmin.util.UserInteraction;

public class ClientHotelServicesDateAction implements IAction {
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

        System.out.println(hotelServiceController.getSortedClientServices(id,
                HotelServiceSortCriterion.DATE));

    }
}

