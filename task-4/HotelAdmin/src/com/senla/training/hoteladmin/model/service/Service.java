package com.senla.training.hoteladmin.model.service;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Service {
   private double price;
   private ServiceType type;
   private Client client;
   private Date date;

   public Service(double price, ServiceType type, Client client, Date date){
       if(!(date.compareTo(client.getArrivalDate())>-1 && date.compareTo(client.getDepartureDate())<1)){
           throw new IllegalArgumentException("Can not assign srvice for client " +
                   "since it not suitable for the duration of the stay");
       }
       this.price = price;
       this.type = type;
       this.client = client;
       this.date = date;
   }

    public void setPrice(double price){
       this.price = price;
   }

   public void setType(ServiceType type){
       this.type = type;
   }

   public void setClient(Client client){
       this.client = client;
   }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public ServiceType getType(){
       return type;
    }

    public Client getClient() {
        return client;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Service %s, price: %.2f, date: %s for client %s %s",type,price,
                DateUtil.getStr(date),client.getFirstName(),client.getLastName());
    }
}
