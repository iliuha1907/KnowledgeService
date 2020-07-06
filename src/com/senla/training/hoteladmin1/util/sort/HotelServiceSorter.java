package com.senla.training.hoteladmin.util.sort;

import com.senla.training.hoteladmin.model.hotelservice.HotelService;

import java.util.List;

public class HotelServiceSorter {
    public static void sortByPrice(List<HotelService> hotelServices) {
        hotelServices.sort((o1, o2) -> {
            if (o1.getPrice() == null && o2.getPrice() == null) {
                return 0;
            }
            if (o1.getPrice() == null && o2.getPrice() != null) {
                return -1;
            }
            if (o1.getPrice() != null && o2.getPrice() == null) {
                return 1;
            }
            return o1.getPrice().compareTo(o2.getPrice());
        });
    }

    public static void sortByDate(List<HotelService> hotelServices) {
        hotelServices.sort((o1, o2) ->
        {
            if (o1.getDate() == null && o2.getDate() == null) {
                return 0;
            }
            if (o1.getDate() == null && o2.getDate() != null) {
                return -1;
            }
            if (o1.getDate() != null && o2.getDate() == null) {
                return 1;
            }
            return o1.getDate().compareTo(o2.getDate());
        });
    }
}

