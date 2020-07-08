package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.view.Builder;
import com.senla.training.hoteladmin.view.MenuController;

public class Main {

    public static void main(String[] args) {
        MenuController menuController = MenuController.getInstance(Builder.getInstance());
        menuController.run();
    }
}

