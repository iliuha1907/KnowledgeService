package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.model.svc.HotelServiceType;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;

public class ChangeServicePriceAction implements IAction {
    private HotelServiceController hotelServiceController = HotelServiceController.getInstance(
            HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                    HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                    ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        HotelServiceType type;
        try {
            type = userInteraction.getServiceType();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("Enter price");
        BigDecimal price;
        try {
            price = userInteraction.getBigDecimal();
            if (price.compareTo(BigDecimal.ZERO) == -1) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong passport number");
            return;
        }

        System.out.println(hotelServiceController.setServicePrice(type, price));
    }
}

