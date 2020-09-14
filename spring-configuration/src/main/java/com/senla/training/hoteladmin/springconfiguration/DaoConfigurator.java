package com.senla.training.hoteladmin.springconfiguration;

import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.client.ClientDaoImpl;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDaoImpl;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDaoImpl;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.dao.room.RoomDaoImpl;
import com.senla.training.hoteladmin.dao.visit.VisitDao;
import com.senla.training.hoteladmin.dao.visit.VisitDaoImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DaoConfigurator {

    @Value("${dao.entityManager.entityManagerName:Manager}")
    private String managerName;

    @Bean
    public RoomDao roomDao() {
        return new RoomDaoImpl();
    }

    @Bean
    public ClientDao clientDao() {
        return new ClientDaoImpl();
    }

    @Bean
    public HotelServiceDao hotelServiceDao() {
        return new HotelServiceDaoImpl();
    }

    @Bean
    public VisitDao visitDao() {
        return new VisitDaoImpl();
    }

    @Bean
    public ReservationDao reservationDao() {
        return new ReservationDaoImpl();
    }

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new EntityManagerProvider();
    }

    @Bean
    public LocalEntityManagerFactoryBean emf() {
        LocalEntityManagerFactoryBean result = new LocalEntityManagerFactoryBean();
        result.setPersistenceUnitName(managerName);
        return result;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager result = new JpaTransactionManager();
        result.setEntityManagerFactory(emf().getObject());
        return result;
    }
}
