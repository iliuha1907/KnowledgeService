package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.util.UserInteraction;

public class MenuController {
    private static MenuController instance;
    private Builder builder;
    private Navigator navigator;

    private MenuController(Builder builder) {
        this.builder = builder;
    }

    public static MenuController getInstance(Builder builder){
        if(instance == null){
            instance = new MenuController(builder);
        }
        return instance;
    }

    public void run() {
        builder.buildMenu();
        navigator = Navigator.getInstance(builder.getRootMenu());
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

