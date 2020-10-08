package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInteraction {

    private static final Logger LOGGER = LogManager.getLogger(UserInteraction.class);

    public static RoomsSortCriterion getRoomsSortCriterionFromString(String criterion) {
        if (criterion == null) {
            LOGGER.error("Error at getting RoomsSortCriterion: Wrong criterion");
            return null;
        }

        RoomsSortCriterion roomSortCriterion;
        try {
            roomSortCriterion = RoomsSortCriterion.valueOf(criterion.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Error at getting RoomsSortCriterion: Wrong criterion");
            return null;
        }
        return roomSortCriterion;
    }

    public static ReservationSortCriterion getReservationSortCriterionFromString(String criterion) {
        if (criterion == null) {
            LOGGER.error("Error at getting ReservationSortCriterion: Wrong criterion");
            return null;
        }

        ReservationSortCriterion reservationSortCriterion;
        try {
            reservationSortCriterion = ReservationSortCriterion.valueOf(criterion.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Error at getting ReservationSortCriterion: Wrong criterion");
            return null;
        }
        return reservationSortCriterion;
    }

    public static VisitSortCriterion getVisitSortCriterionFromString(String criterion) {
        if (criterion == null) {
            LOGGER.error("Error at getting VisitSortCriterion: Wrong criterion");
            return null;
        }

        VisitSortCriterion visitSortCriterion;
        try {
            visitSortCriterion = VisitSortCriterion.valueOf(criterion.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Error at getting VisitSortCriterion: Wrong criterion");
            return null;
        }
        return visitSortCriterion;
    }
}

