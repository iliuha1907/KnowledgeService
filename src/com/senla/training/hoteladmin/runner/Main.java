package com.senla.training.hotelAdmin.runner;

import com.senla.training.hotelAdmin.view.Builder;
import com.senla.training.hotelAdmin.view.MenuController;

public class Main {

    public static void main(String[] args) {
        MenuController menuController = MenuController.getInstance(Builder.getInstance());
        menuController.run();
    }
}

