package com.senla.training.hotelAdmin.view;

import com.senla.training.hotelAdmin.util.UserInteraction;

public class MenuController {
    private static MenuController instance;
    private Builder builder;
    private Navigator navigator;

    private MenuController(Builder builder) {
        this.builder = builder;
    }

    public static MenuController getInstance(Builder builder) {
        if (instance == null) {
            instance = new MenuController(builder);
        }
        return instance;
    }

    public void run() {
        builder.buildMenu();
        navigator = Navigator.getInstance(builder.getRootMenu());
        UserInteraction userInteraction = UserInteraction.getInstance();
        boolean stop = false;
        while (!stop) {
            navigator.printMenu();
            Integer choice = userInteraction.getInt() - 1;
            if (choice == null) {
                System.out.println("Wrong input");
                continue;
            }
            if (choice.equals(navigator.getCurrentMenu().getMenuItems().size())) {
                stop = true;
            } else {
                navigator.navigate(choice);
            }
        }
        userInteraction.stopWorking();
    }
}

