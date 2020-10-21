package config;

import com.senla.training.hoteladmin.csvapi.writeread.*;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.dao.user.UserDao;
import com.senla.training.hoteladmin.dao.visit.VisitDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = {"com.senla.training.hoteladmin.service"})
public class TestConfigurator {

    @Bean
    public ClientDao clientDao(){
        return mock(ClientDao.class);
    }

    @Bean
    public UserDao userDao(){
        return mock(UserDao.class);
    }

    @Bean
    public RoomDao roomDao(){
        return mock(RoomDao.class);
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
    public ReservationDao reservationDao(){
        return mock(ReservationDao.class);
    }

    @Bean
    public ClientReaderWriter clientReaderWriter(){
        return mock(ClientReaderWriter.class);
    }

    @Bean
    public RoomReaderWriter roomReaderWriter(){
        return mock(RoomReaderWriter.class);
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
    public ReservationReaderWriter reservationReaderWriter(){
        return mock(ReservationReaderWriter.class);
    }
}
