package config;

import com.senla.training.hoteladmin.controller.*;
import com.senla.training.hoteladmin.csvapi.writeread.*;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.dao.visit.VisitDao;
import com.senla.training.hoteladmin.dto.mapper.*;
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

import static org.mockito.Mockito.mock;

@Configuration
public class ControllerTestConfigurator {

    @Bean
    public ClientController clientController(){
        return new ClientController();
    }

    @Bean
    public HotelServiceController hotelServiceController(){
        return new HotelServiceController();
    }

    @Bean
    public VisitController visitController(){
        return new VisitController();
    }

    @Bean
    public RoomController roomController(){
        return new RoomController();
    }

    @Bean
    public ReservationController reservationController(){
        return new ReservationController();
    }

    @Bean
    public ClientDao clientDao(){
        return mock(ClientDao.class);
    }

    @Bean
    public HotelServiceDao hotelServiceDao(){
        return mock(HotelServiceDao.class);
    }

    @Bean
    public VisitDao visitDao(){
        return mock(VisitDao.class);
    }

    @Bean
    public RoomDao roomDao(){
        return mock(RoomDao.class);
    }

    @Bean
    public ReservationDao reservationDao(){
        return mock(ReservationDao.class);
    }

    @Bean
    public ClientMapper clientMapper(){
        return mock(ClientMapper.class);
    }

    @Bean
    public HotelServiceMapper hotelServiceMapper(){
        return mock(HotelServiceMapper.class);
    }

    @Bean
    public VisitMapper visitsMapper(){
        return mock(VisitMapper.class);
    }

    @Bean
    public RoomMapper roomsMapper(){
        return mock(RoomMapper.class);
    }

    @Bean
    public ReservationMapper reservationsMapper(){
        return mock(ReservationMapper.class);
    }

    @Bean
    public MessageDtoMapper messageDtoMapper(){
        return mock(MessageDtoMapper.class);
    }

    @Bean
    public ClientService clientService(){
        return new ClientServiceImpl();
    }

    @Bean
    public HotelServiceService hotelServiceService(){
        return new HotelServiceServiceImpl();
    }

    @Bean
    public VisitService visitService(){
        return new VisitServiceImpl();
    }

    @Bean
    public RoomService roomService(){
        return new RoomServiceImpl();
    }

    @Bean
    public ReservationService reservationService(){
        return new ReservationServiceImpl();
    }

    @Bean
    public ClientReaderWriter clientReaderWriter(){
        return mock(ClientReaderWriter.class);
    }

    @Bean
    public HotelServiceReaderWriter hotelServiceReaderWriter(){
        return mock(HotelServiceReaderWriter.class);
    }

    @Bean
    public VisitReaderWriter visitReaderWriter(){
        return mock(VisitReaderWriter.class);
    }

    @Bean
    public RoomReaderWriter roomReaderWriter(){
        return mock(RoomReaderWriter.class);
    }

    @Bean
    public ReservationReaderWriter reservationReaderWriter(){
        return mock(ReservationReaderWriter.class);
    }
}
