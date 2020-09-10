package com.senla.training.hoteladmin.dao.room;

import com.senla.training.hoteladmin.dao.HibernateDao;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;

import javax.persistence.EntityManager;
import java.util.List;

public interface RoomDao extends HibernateDao<Room> {

    List<Room> getSortedRooms(RoomsSortCriterion criterion, EntityManager entityManager);

    List<Room> getFreeRooms(EntityManager entityManager);

    List<Room> getSortedFreeRooms(RoomsSortCriterion criterion, EntityManager entityManager);

    Long getNumberOfFreeRooms(EntityManager entityManager);

    Room getFirstFreeRoom(EntityManager entityManager);
}
