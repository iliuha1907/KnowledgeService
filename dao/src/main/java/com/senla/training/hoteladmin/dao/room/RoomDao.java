package com.senla.training.hoteladmin.dao.room;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;

import java.util.List;

public interface RoomDao extends GenericDao<Room, Integer> {

    List<Room> getSortedRooms(RoomsSortCriterion criterion);

    List<Room> getFreeRooms();

    List<Room> getSortedFreeRooms(RoomsSortCriterion criterion);

    Long getNumberOfFreeRooms(Boolean freeOnly);

    Room getFirstFreeRoom();
}
