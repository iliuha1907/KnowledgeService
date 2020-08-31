package dao;

import model.room.Room;
import util.sort.RoomsSortCriterion;

import java.sql.Connection;
import java.util.List;

public interface RoomDao extends GenericDao<Room> {

    List<Room> getSortedRooms(RoomsSortCriterion criterion, Connection connection);

    List<Room> getFreeRooms(Connection connection);

    List<Room> getSortedFreeRooms(RoomsSortCriterion criterion, Connection connection);

    Integer getNumberOfFreeRooms(Connection connection);

    Room getFirstFreeRoom(Connection connection);
}

