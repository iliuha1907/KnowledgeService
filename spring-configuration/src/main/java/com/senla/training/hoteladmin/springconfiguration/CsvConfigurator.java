package com.senla.training.hoteladmin.springconfiguration;

import com.senla.training.hoteladmin.csvapi.writeread.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfigurator {

    @Bean
    public ClientReaderWriter clientReaderWriter() {
        return new ClientReaderWriter();
    }

    @Bean
    public RoomReaderWriter roomReaderWriter() {
        return new RoomReaderWriter();
    }

    @Bean
    public HotelServiceReaderWriter hotelServiceReaderWriter() {
        return new HotelServiceReaderWriter();
    }

    @Bean
    public VisitReaderWriter visitReaderWriter() {
        return new VisitReaderWriter();
    }

    @Bean
    public ReservationReaderWriter reservationReaderWriter() {
        return new ReservationReaderWriter();
    }
}
