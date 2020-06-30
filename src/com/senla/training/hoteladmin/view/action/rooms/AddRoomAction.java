package com.senla.training.hoteladmin.view.action.rooms;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.model.room.RoomStatus;
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
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;

public class AddRoomAction implements IAction {
    private RoomController roomController = RoomController.getInstance(
            RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance(),
                    ClientServiceImpl.
                            getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                                    HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                                    ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance())));


    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        RoomStatus roomStatus;
        try {
            roomStatus = userInteraction.getRoomStatus();
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
            System.out.println("Wrong price");
            return;
        }

        System.out.println("Enter capacity");
        Integer capacity;
        try {
            capacity = userInteraction.getInt();
            if (capacity < 1) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong capacity");
            return;
        }

        System.out.println("Enter stars");
        Integer stars;
        try {
            stars = userInteraction.getInt();
            if (stars < 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong stars");
            return;
        }

        System.out.println(roomController.addRoom(roomStatus, price, capacity, stars));
    }
}

