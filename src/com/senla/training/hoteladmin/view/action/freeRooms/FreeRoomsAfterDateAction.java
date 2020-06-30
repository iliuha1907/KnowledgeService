package com.senla.training.hoteladmin.view.action.freeRooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.ArchivServiceImpl;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.service.HotelServiceServiceImpl;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.service.writer.ClientWriterImpl;
import com.senla.training.hoteladmin.service.writer.HotelServiceWriterImpl;
import com.senla.training.hoteladmin.service.writer.RoomWriterImpl;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

import java.util.Date;

public class FreeRoomsAfterDateAction implements IAction {
    private RoomController roomController = RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance(),
                    ClientServiceImpl.
                            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                                    HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                                    ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance())));


    @Override
    public void execute() {
        System.out.println("Enter date");
        Date date;
        try {
            date = DateUtil.getDate(UserInteraction.getInstance().getString());
        } catch (Exception ex) {
            System.out.println("Wrong date");
            return;
        }
        System.out.println(roomController.getFreeRoomsAfterDate(date));
    }
}

