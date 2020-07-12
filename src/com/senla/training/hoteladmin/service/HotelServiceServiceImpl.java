package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.annotation.ConfigProperty;
import com.senla.training.hoteladmin.annotation.NeedDiClass;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.repository.ClientsRepository;
import com.senla.training.hoteladmin.repository.HotelServiceRepository;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.filecsv.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.util.idspread.HotelServiceIdProvider;
import com.senla.training.hoteladmin.util.serializator.Deserializator;
import com.senla.training.hoteladmin.util.serializator.Serializator;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hoteladmin.util.sort.HotelServiceSorter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NeedDiClass
public class HotelServiceServiceImpl implements HotelServiceService {
    @ConfigProperty
    private HotelServiceRepository hotelServiceRepository;
    @ConfigProperty
    private ClientsRepository clientsRepository;

    public HotelServiceServiceImpl() {
    }

    @Override
    public void setServices(List<HotelService> hotelServices) {
        hotelServiceRepository.setHotelServices(hotelServices);
    }

    @Override
    public void addService(BigDecimal price, HotelServiceType type, Integer clientId, Date date) {
        Client client = clientsRepository.getClientById(clientId);
        if (client == null) {
            throw new BusinessException("Error at adding service: no such client");
        }
        if (!(date.after(client.getArrivalDate()) && date.before(client.getDepartureDate()))) {
            throw new BusinessException("Error at adding service: incompatible dates");
        }
        hotelServiceRepository.addHotelService(new HotelService(price, type, client, date));
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
        HotelServiceReaderWriter.writeServices(hotelServiceRepository.getHotelServices());
    }

    @Override
    public void importServices() {
        List<HotelService> hotelServices = HotelServiceReaderWriter.readServices();
        hotelServices.forEach(hotelService -> {
            Client existing = clientsRepository.getClientById(hotelService.getClient().getId());
            if (existing == null) {
                throw new BusinessException("Could not import services: wrong idspread of a client");
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
        hotelServices.forEach(hotelService -> {
            hotelService.setClient(clientsRepository.getClientById(hotelService.getClient().getId()));
        });
        setServices(hotelServices);
    }

    @Override
    public void serializeId() {
        Serializator.serializeHotelServiceId(HotelServiceIdProvider.getCurrentId());
    }

    @Override
    public void deserializeId() {
        Integer id = Deserializator.deserializeHotelServiceId();
        HotelServiceIdProvider.setCurrentId(id);
    }

    private List<HotelService> getClientServices(Client client) {
        return hotelServiceRepository.getClientHotelServices(client.getId());
    }
}

