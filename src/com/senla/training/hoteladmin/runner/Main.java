package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.dao.DaoManager;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.MenuController;
import com.senla.training.injection.DependencyInjector;

public class Main {
    private static DaoManager daoManager;

    public static void main(String[] args) {
        UserInteraction.startWorking();
        try {
            DependencyInjector.init("com.senla.training.hoteladmin");
            daoManager = DependencyInjector.getClassInstance(DaoManager.class);
            MenuController menuController = DependencyInjector.getClassInstance(MenuController.class);
            menuController.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
            if (daoManager != null) {
                try {
                    daoManager.closeConnection();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Shutting app down");
                }
            }
        }
    }
}

