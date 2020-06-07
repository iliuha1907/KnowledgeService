package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.service.Service;

import java.util.Arrays;
import java.util.Comparator;

public class ServiceManipulator {

    public static void sortByPrice(Service[] services) {
        Arrays.sort(services, new Comparator<Service>() {
            @Override
            public int compare(Service o1, Service o2) {
                return (int) (o1.getPrice() - o2.getPrice());
            }
        });
    }

    public static void sortByDate(Service[] services) {
        Arrays.sort(services, new Comparator<Service>() {
            @Override
            public int compare(Service o1, Service o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    public static Service[] getRealServices(Service[] services, int count) {
        Service[] realServices = new Service[count];
        for (int i = 0; i < services.length; i++) {
            if (services[i] == null) {
                break;
            }
            realServices[i] = services[i];
        }
        return realServices;
    }

    public static Service[] getClientServices(Service[] services, Client client) {
        int number = 0;
        for (int i = 0; i < services.length; i++) {
            if (services[i] == null) {
                break;
            }
            if (services[i].getClient().equals(client)) {
                number++;
            }
        }
        Service[] result = new Service[number];
        int k = 0;
        for (int i = 0; i < services.length; i++) {
            if (services[i] == null) {
                break;
            }
            if (services[i].getClient().equals(client)) {
                result[k++] = services[i];
            }
        }
        return result;
    }
}

