package com.senla.training.hoteladmin.springconfiguration;

import com.senla.training.hoteladmin.service.client.ClientService;
import com.senla.training.hoteladmin.service.client.ClientServiceImpl;
import com.senla.training.hoteladmin.service.hotelservice.HotelServiceService;
import com.senla.training.hoteladmin.service.hotelservice.HotelServiceServiceImpl;
import com.senla.training.hoteladmin.service.reservation.ReservationService;
import com.senla.training.hoteladmin.service.reservation.ReservationServiceImpl;
import com.senla.training.hoteladmin.service.room.RoomService;
import com.senla.training.hoteladmin.service.room.RoomServiceImpl;
import com.senla.training.hoteladmin.service.visit.VisitService;
import com.senla.training.hoteladmin.service.visit.VisitServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ServiceConfigurator {

    @Bean
    public ClientService clientService() {
        return new ClientServiceImpl();
    }

    @Bean
    public RoomService roomService() {
        return new RoomServiceImpl();
    }

    @Bean
    public HotelServiceService hotelServiceService() {
        return new HotelServiceServiceImpl();
    }

    @Bean
    public ReservationService reservationService() {
        return new ReservationServiceImpl();
    }

    @Bean
    public VisitService visitService() {
        return new VisitServiceImpl();
    }
}
