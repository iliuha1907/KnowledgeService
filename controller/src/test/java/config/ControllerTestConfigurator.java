package config;

import com.senla.training.hoteladmin.controller.*;
import com.senla.training.hoteladmin.controller.security.TokenFilter;
import com.senla.training.hoteladmin.controller.security.TokenProvider;
import com.senla.training.hoteladmin.csvapi.writeread.*;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.dao.user.UserDao;
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
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.AuthenticationManager;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = {"com.senla.training.hoteladmin.controller","com.senla.training.hoteladmin.service"},
        excludeFilters =@ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.senla\\.training\\.hoteladmin\\.controller\\.[config|security].*"))
public class ControllerTestConfigurator {

    @Bean
    public ClientDao clientDao(){
        return mock(ClientDao.class);
    }

    @Bean
    public UserDao userDao(){
        return mock(UserDao.class);
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
    public ModelMapper modelMapper(){
        return mock(ModelMapper.class);
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
    public TokenDtoMapper tokenDtoMapper(){
        return mock(TokenDtoMapper.class);
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

    @Bean
    public AuthenticationManager authenticationManager(){
        return mock(AuthenticationManager.class);
    }
}
