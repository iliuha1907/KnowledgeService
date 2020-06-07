package com.senla.training.hoteladmin.model.room;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.DateUtil;

import java.text.SimpleDateFormat;

public class Room {
    private int number;
    private RoomStatus status;
    private double price;
    private int capacity;
    private int stars;
    private Client resident;

    public Room(int number, RoomStatus status, double price, int capacity, int stars){
        this.number = number;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        resident = null;
    }

    public void setStatus(RoomStatus status){
        this.status = status;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setResident(Client resident){
        this.resident = resident;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public void setStars(int stars){
        this.stars = stars;
    }

    public int getNumber() {
        return number;
    }

    public RoomStatus getStatus(){
        return status;
    }

    public double getPrice() {
        return price;
    }

    public Client getResident() {
        return resident;
    }

    public int getStars(){
        return stars;
    }

    public int getCapacity(){
        return capacity;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Room) && this.number == ((Room) obj).getNumber();
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        String result = String.format("Room # %d, status:%s, capacity: %d, stars: %d, price: %.2f",number
        ,status.toString(),capacity,stars,price);
        if(resident != null){
            result += ", taken from "+ DateUtil.getStr(resident.getArrivalDate())+
                    " to "+DateUtil.getStr(resident.getDepartureDate());
        }
        return result;
    }
}
