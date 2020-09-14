package com.senla.training.hoteladmin.springconfiguration;

import com.senla.training.hoteladmin.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfigurator {

    @Bean
    public RoomController roomController() {
        return new RoomController();
    }

    @Bean
    public ClientController clientController() {
        return new ClientController();
    }

    @Bean
    public HotelServiceController hotelServiceController() {
        return new HotelServiceController();
    }

    @Bean
    public VisitController visitController() {
        return new VisitController();
    }

    @Bean
    public ReservationController reservationController() {
        return new ReservationController();
    }
}
