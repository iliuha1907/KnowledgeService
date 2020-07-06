package com.senla.training.hoteladmin.view.action.hotelservice;

import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class ClientHotelServicesDateAction implements IAction {
    private HotelServiceController hotelServiceController = HotelServiceController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        Integer id = userInteraction.getNaturalIntWithMessage("Enter id of the client");
        if (id == null) {
            return;
        }

        System.out.println(hotelServiceController.getSortedClientServices(id,
                HotelServiceSortCriterion.DATE));

    }
}

