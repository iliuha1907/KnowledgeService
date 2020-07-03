package com.senla.training.hotelAdmin.util;

import com.senla.training.hotelAdmin.model.room.RoomStatus;
import com.senla.training.hotelAdmin.model.hotelService.HotelServiceType;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserInteraction {
    private static UserInteraction instance;
    private Scanner input;

    private UserInteraction() {
        input = new Scanner(System.in);
    }

    public static UserInteraction getInstance() {
        if (instance == null) {
            instance = new UserInteraction();
        }
        return instance;
    }

    public Integer getInt() {
        try {
            return Integer.parseInt(input.nextLine());
        } catch (Exception ex) {
            return null;
        }
    }

    public BigDecimal getBigDecimal() {
        try {
            return new BigDecimal(input.nextLine());
        } catch (Exception ex) {
            return null;
        }
    }

    public String getString() {
        return input.nextLine();
    }

    public RoomStatus getRoomStatus() {
        System.out.println("Enter 1 to repaired, 2 to served");
        Integer choice = getInt();
        if (choice == null) {
            return null;
        }
        if (choice == 1) {
            return RoomStatus.REPAIRED;
        } else if (choice == 2) {
            return RoomStatus.SERVED;
        }
        return null;
    }

    public HotelServiceType getServiceType() {
        System.out.println("Enter 1 to spa, 2 to massage, 3 to sauna");
        Integer choice = getInt();
        if (choice == null) {
            return null;
        }
        if (choice.equals(1)) {
            return HotelServiceType.SPA;
        }
        if (choice.equals(2)) {
            return HotelServiceType.MASSAGE;
        }
        if (choice.equals(3)) {
            return HotelServiceType.SAUNA;
        }
        return null;
    }

    public void stopWorking() {
        input.close();
    }
}

