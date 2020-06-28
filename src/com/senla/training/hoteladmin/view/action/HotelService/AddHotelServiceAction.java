package com.senla.training.hoteladmin.view.action.HotelService;

import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.model.svc.HotelServiceType;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;
import java.util.Date;

public class AddHotelServiceAction implements IAction {
    private HotelServiceController hotelServiceController = HotelServiceController.getInstance(
            HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                            HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                            ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();
        System.out.println("Enter id of the client");
        Integer id;
        try {
            id = userInteraction.getInt();
            if (id < 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong id");
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
            System.out.println("Wrong price");
            return;
        }

        HotelServiceType type;
        try {
            type = userInteraction.getServiceType();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("Enter date");
        Date date;
        try {
            date = DateUtil.getDate(userInteraction.getString());
        } catch (Exception ex) {
            System.out.println("Wrong date");
            return;
        }
        System.out.println(hotelServiceController.addService(price, type, id, date));
    }
}

