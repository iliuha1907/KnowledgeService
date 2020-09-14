package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.springconfiguration.*;
import com.senla.training.hoteladmin.util.AppenderBuilder;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.MenuController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    private static EntityManagerProvider entityManagerProvider;

    public static void main(String[] args) {
        System.err.close();
        System.setErr(System.out);
        UserInteraction.startWorking();
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(
                    ConfigurationsClassesProvider.getClasses());
            AppenderBuilder appenderBuilder = context.getBean(AppenderBuilder.class);
            appenderBuilder.build();
            entityManagerProvider = context.getBean(EntityManagerProvider.class);
            MenuController menuController = context.getBean(MenuController.class);
            menuController.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
            entityManagerProvider.close();
        }
    }
}

