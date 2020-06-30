package com.senla.training.hoteladmin.view.action.hotelService;

import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.service.writer.ClientWriterImpl;
import com.senla.training.hoteladmin.service.writer.HotelServiceWriterImpl;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class ClientHotelServicesDateAction implements IAction {
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
        System.out.println(hotelServiceController.getSortedClientServices(id,
                HotelServiceSortCriterion.DATE));

    }
}

