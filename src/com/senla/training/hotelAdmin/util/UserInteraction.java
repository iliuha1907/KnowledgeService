package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;

import java.math.BigDecimal;
import java.util.Date;
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

    public Integer getNaturalIntWithMessage(String messsage) {
        System.out.println(messsage);
        try {
            Integer number = Integer.parseInt(input.nextLine());
            if(number < 1){
                System.out.println("Wrong input");
                return null;
            }
            return number;
        } catch (Exception ex) {
            System.out.println("Wrong input");
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

    public BigDecimal getPositiveBigDecimalWithMessage(String message) {
        System.out.println(message);
        try {
            BigDecimal number = new BigDecimal(input.nextLine());
            if(number.compareTo(BigDecimal.ZERO) < 0){
                System.out.println("Wrong input");
                return null;
            }
            return number;
        } catch (Exception ex) {
            System.out.println("Wrong input");
            return null;
        }
    }

    public String getString() {
        return input.nextLine();
    }

    public String getStringWithMessage(String message) {
        System.out.println(message);
        return input.nextLine();
    }

    public RoomStatus getRoomStatus() {
        System.out.println("Enter 1 to repaired, 2 to served");
        Integer choice = getInt();
        if (choice == null) {
            System.out.println("Wrong input");
            return null;
        }
        if (choice == 1) {
            return RoomStatus.REPAIRED;
        } else if (choice == 2) {
            return RoomStatus.SERVED;
        }
        System.out.println("Wrong input");
        return null;
    }

    public HotelServiceType getServiceType() {
        System.out.println("Enter 1 to spa, 2 to massage, 3 to sauna");
        Integer choice = getInt();
        if (choice == null) {
            System.out.println("Wrong input");
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
        System.out.println("Wrong input");
        return null;
    }

    public Date getDateWithMessage(String message){
        System.out.println(message);
        Date date = DateUtil.getDate(getString());
        if (date == null) {
            System.out.println("Wrong input");
            return null;
        }
        return date;
    }

    public void stopWorking() {
        input.close();
    }
}

