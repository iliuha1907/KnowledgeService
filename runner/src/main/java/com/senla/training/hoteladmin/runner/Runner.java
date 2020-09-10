package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.injection.DependencyInjector;
import com.senla.training.hoteladmin.util.AppenderBuilder;
import com.senla.training.hoteladmin.util.ListOfPackagesProvider;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.MenuController;

public class Runner {

    public static void main(String[] args) throws Exception {
        System.err.close();
        System.setErr(System.out);
        UserInteraction.startWorking();
        try {
            DependencyInjector.init(ListOfPackagesProvider.getPackagesForInjection());
            AppenderBuilder.build();
            MenuController menuController = DependencyInjector.getClassInstance(MenuController.class);
            menuController.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
            EntityManagerProvider.close();
        }
    }
}

