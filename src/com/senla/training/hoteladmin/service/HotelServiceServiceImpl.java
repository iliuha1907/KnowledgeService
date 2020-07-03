package com.senla.training.hotelAdmin.service;

import com.senla.training.hotelAdmin.exception.BusinessException;
import com.senla.training.hotelAdmin.model.hotelService.HotelService;
import com.senla.training.hotelAdmin.model.hotelService.HotelServiceType;
import com.senla.training.hotelAdmin.repository.HotelServiceRepository;
import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hotelAdmin.util.fileCsv.writeRead.HotelServiceWriter;
import com.senla.training.hotelAdmin.util.serializator.Deserializator;
import com.senla.training.hotelAdmin.util.serializator.Serializator;
import com.senla.training.hotelAdmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hotelAdmin.util.sort.HotelServiceSorter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HotelServiceServiceImpl implements HotelServiceService {
    private static HotelServiceService instance;
    private HotelServiceRepository hotelServiceRepository;
    private ClientService clientService;

    private HotelServiceServiceImpl() {
        this.hotelServiceRepository = HotelServiceRepositoryImpl.getInstance();
        this.clientService = ClientServiceImpl.getInstance();
    }

    public static HotelServiceService getInstance() {
        if (instance == null) {
            instance = new HotelServiceServiceImpl();
            return instance;
        }
        return instance;
    }

    @Override
    public void setServices(List<HotelService> hotelServices) {
        hotelServiceRepository.setHotelServices(hotelServices);
    }

    @Override
    public void addService(BigDecimal price, HotelServiceType type, Integer clientId, Date date) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            throw new BusinessException("Error at adding service: no such client");
        }
        if (!((date).compareTo(client.getArrivalDate()) > -1 &&
                date.compareTo(client.getDepartureDate()) < 1)) {
            throw new BusinessException("Error at adding service: incompatible dates");
        }
        hotelServiceRepository.addHotelService(new HotelService(null, price, type, client, date));
    }

    @Override
    public void setServicePrice(Integer id, BigDecimal price) {
        HotelService hotelService = hotelServiceRepository.getHotelServiceById(id);
        if (hotelService == null) {
            throw new BusinessException("Error at modifying service: ino such service");
        }
        hotelService.setPrice(price);
    }

    @Override
    public List<HotelService> getSortedClientServices(Client client, HotelServiceSortCriterion criterion) {
        List<HotelService> hotelServices = getClientServices(client);
        if (criterion.equals(HotelServiceSortCriterion.DATE)) {
            HotelServiceSorter.sortByDate(hotelServices);
        } else if (criterion.equals(HotelServiceSortCriterion.PRICE)) {
            HotelServiceSorter.sortByPrice(hotelServices);
        }
        return hotelServices;
    }

    @Override
    public List<HotelService> getServices(HotelServiceSortCriterion criterion) {
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        if (criterion.equals(HotelServiceSortCriterion.DATE)) {
            HotelServiceSorter.sortByDate(hotelServices);
        } else if (criterion.equals(HotelServiceSortCriterion.PRICE)) {
            HotelServiceSorter.sortByPrice(hotelServices);
        }
        return hotelServices;
    }

    @Override
    public void exportServices() {
        HotelServiceWriter.writeServices(hotelServiceRepository.getHotelServices());
    }

    @Override
    public void importServices() {
        List<HotelService> hotelServices = HotelServiceWriter.readServices();
        if (hotelServices == null) {
            throw new BusinessException("Could not import services");
        }
        hotelServices.forEach(hotelService -> {
            Client existing = clientService.getClientById(hotelService.getClient().getId());

            if (existing == null) {
                throw new BusinessException("Could not import services: wrong id of a client");
            }
            hotelService.setClient(existing);
            updateService(hotelService);
        });
    }

    @Override
    public void updateService(HotelService hotelService) {
        if (hotelService == null) {
            return;
        }
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        int index = hotelServices.indexOf(hotelService);
        if (index == -1) {
            hotelServiceRepository.addHotelService(hotelService);
        } else {
            hotelServices.set(index, hotelService);
        }
    }

    @Override
    public void serializeServices() {
        Serializator.serializeServices(hotelServiceRepository.getHotelServices());
    }

    @Override
    public void deserializeServices() {
        List<HotelService> hotelServices = Deserializator.deserializeServices();
        if (hotelServices == null) {
            throw new BusinessException("Error at deserialization of services");
        }
        hotelServices.forEach(hotelService -> {
            hotelService.setClient(clientService.getClientById(hotelService.getClient().getId()));
        });
        setServices(hotelServices);
    }

    private List<HotelService> getClientServices(Client client) {
        return hotelServiceRepository.getClientHotelServices(client.getId());
    }
}

