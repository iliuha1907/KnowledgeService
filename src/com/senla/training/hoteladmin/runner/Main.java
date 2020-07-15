package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.injection.DependencyInjector;
import com.senla.training.hoteladmin.view.MenuController;

public class Main {

    public static void main(String[] args) {
        UserInteraction.startWorking();
        DependencyInjector dependencyInjector = new DependencyInjector();
        try {
            dependencyInjector.init();
            MenuController menuController = (MenuController) dependencyInjector.getStartClass(MenuController.class);
            if (menuController == null) {
                System.out.println("Shutting app down");
            } else {
                menuController.run();
            }
        } catch (IncorrectWorkException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
        }
    }
}

