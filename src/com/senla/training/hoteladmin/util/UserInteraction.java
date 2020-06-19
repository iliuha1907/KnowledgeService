package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.svc.ServiceType;

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
        Integer choice;
        choice = Integer.parseInt(input.nextLine());
        return choice;
    }

    public BigDecimal getBigDecimal() {
        BigDecimal choice;
        choice = new BigDecimal(input.nextLine());
        return choice;
    }

    public String getString() {
        String choice;
        choice = input.nextLine();
        return choice;
    }

    public RoomStatus getRoomStatus() {
        System.out.println("Enter 1 to repaired, 2 to served");
        Integer choice = Integer.parseInt(input.nextLine());
        switch (choice) {
            case 1:
                return RoomStatus.REPAIRED;
            case 2:
                return RoomStatus.SERVED;
            default:
                throw new IllegalArgumentException("Unknown command");
        }
    }

    public ServiceType getServiceType() {
        System.out.println("Enter 1 to spa, 2 to massage, 3 to sauna");
        Integer choice = Integer.parseInt(input.nextLine());
        switch (choice) {
            case 1:
                return ServiceType.SPA;
            case 2:
                return ServiceType.MASSAGE;
            case 3:
                return ServiceType.SAUNA;
            default:
                throw new IllegalArgumentException("Unknown command");
        }
    }

    public void stopWorking() {
        input.close();
    }
}

