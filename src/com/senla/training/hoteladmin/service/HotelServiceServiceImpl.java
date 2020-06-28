package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.svc.HotelService;
import com.senla.training.hoteladmin.repository.HotelServiceRepository;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.HotelServiceIdProvider;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hoteladmin.model.svc.HotelServiceType;
import com.senla.training.hoteladmin.util.sort.HotelServiceSorter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class HotelServiceServiceImpl implements HotelServiceService {
    private static HotelServiceServiceImpl instance;
    private HotelServiceRepository hotelServiceRepository;
    private HotelServiceWriter hotelServiceWriter;

    private HotelServiceServiceImpl(HotelServiceRepository hotelServiceRepository, HotelServiceWriter hotelServiceWriter) {
        this.hotelServiceRepository = hotelServiceRepository;
        this.hotelServiceWriter = hotelServiceWriter;
    }

    public static HotelServiceService getInstance(HotelServiceRepository hotelServiceRepository, HotelServiceWriter hotelServiceWriter) {
        if (instance == null) {
            instance = new HotelServiceServiceImpl(hotelServiceRepository, hotelServiceWriter);
            return instance;
        }
        return instance;
    }

    @Override
    public boolean addService(HotelService hotelService) {
        if (!(hotelService.getDate().compareTo(hotelService.getClient().getArrivalDate()) > -1 &&
                hotelService.getDate().compareTo(hotelService.getClient().getDepartureDate()) < 1)) {
            return false;
        }
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        hotelServices.add(hotelService);
        hotelServiceRepository.setHotelServices(hotelServices);
        return true;
    }

    @Override
    public boolean setServicePrice(HotelServiceType type, BigDecimal price) {
        boolean exists = false;
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        ListIterator<HotelService> iterator = hotelServices.listIterator();
        while (iterator.hasNext()) {
            HotelService hotelService = iterator.next();
            if (hotelService.getType().equals(type)) {
                hotelService.setPrice(price);
                exists = true;
            }
        }
        hotelServiceRepository.setHotelServices(hotelServices);
        return exists;
    }

    private List<HotelService> getClientServices(List<HotelService> hotelServices, Client client) {
        LinkedList<HotelService> result = new LinkedList<>();
        hotelServices.forEach(e -> {
            if (e.getClient().equals(client)) {
                result.add(e);
            }
        });
        return result;
    }

    @Override
    public List<HotelService> getSortedClientServices(Client client, HotelServiceSortCriterion criterion) {
        List<HotelService> hotelServices = getClientServices(hotelServiceRepository.getHotelServices(), client);
        if(criterion.equals(HotelServiceSortCriterion.DATE)){
            HotelServiceSorter.sortByDate(hotelServices);
        }
        else if(criterion.equals(HotelServiceSortCriterion.PRICE)){
            HotelServiceSorter.sortByPrice(hotelServices);
        }
        return hotelServices;
    }

    @Override
    public List<HotelService> getServices(HotelServiceSortCriterion criterion) {
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        if(criterion.equals(HotelServiceSortCriterion.DATE)){
            HotelServiceSorter.sortByDate(hotelServices);
        }
        else if(criterion.equals(HotelServiceSortCriterion.PRICE)){
            HotelServiceSorter.sortByPrice(hotelServices);
        }
        return hotelServices;
    }

    @Override
    public HotelService getService(Integer clientId) {
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        for(HotelService hotelService : hotelServices){
            if(hotelService.getClient().getId().equals(clientId)){
                return hotelService;
            }
        }
        return null;
    }

    @Override
    public boolean removeService(Integer clientId) {
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        HotelService hotelService = getService(clientId);
        if(hotelServices.remove(hotelService)){
            hotelServiceRepository.setHotelServices(hotelServices);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean exportServices() {
        try {
            hotelServiceWriter.writeServices(hotelServiceRepository.getHotelServices());
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean importServices(ClientService clientService, RoomService roomService) {
        List<HotelService> hotelServices;
        try {
            hotelServices = hotelServiceWriter.readServices();
            hotelServices.forEach(e->{
                updateService(e);
                clientService.updateClient(e.getClient());
                roomService.updateRoom(e.getClient().getRoom());
            });
        }
        catch (Exception ex){
            return false;
        }

        return true;
    }

    @Override
    public void updateService(HotelService hotelService) {
        if(hotelService == null){
            return;
        }
        List<HotelService> hotelServices = hotelServiceRepository.getHotelServices();
        int index = hotelServices.indexOf(hotelService);
        if(index == -1){
            hotelService.setId(HotelServiceIdProvider.getNextId());
            hotelServices.add(hotelService);
        }
        else {
            hotelServices.set(index, hotelService);
        }
        hotelServiceRepository.setHotelServices(hotelServices);
    }
}

