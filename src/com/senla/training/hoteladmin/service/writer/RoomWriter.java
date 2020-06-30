package com.senla.training.hoteladmin.service.writer;

import com.senla.training.hoteladmin.model.room.Room;

import java.io.IOException;
import java.util.List;

public interface RoomWriter {
    String FILE_NAME = "Files/rooms.csv";
    String SEPARATOR = ";";

    void writeRooms(List<Room> rooms) throws IOException;

    List<Room> readRooms() throws IOException;
}

