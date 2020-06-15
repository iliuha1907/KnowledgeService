package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.util.UserInteraction;

public class MenuController {
    private Builder builder;
    private Navigator navigator;

    public MenuController(Builder builder) {
        this.builder = builder;
        builder.buildMenu();
        navigator = new Navigator(builder.getRootMenu());
    }

    public void run() {
        UserInteraction userInteraction = UserInteraction.getInstance();
        while (true) {
            navigator.printMenu();
            Integer choice;
            try {
                choice = userInteraction.getInt() - 1;
            } catch (Exception ex) {
                System.out.println("Wrong input");
                continue;
            }
            if (!navigator.navigate(choice)) {
                break;
            }
        }
        userInteraction.stopWorking();
    }
}

