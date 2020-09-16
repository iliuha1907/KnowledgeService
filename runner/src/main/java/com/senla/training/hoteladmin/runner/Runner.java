package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.dao.HibernateConfigurator;
import com.senla.training.hoteladmin.util.AppenderBuilder;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.MenuController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

public class Runner {

    private static LocalEntityManagerFactoryBean localEntityManagerFactoryBean;

    public static void main(String[] args) {
        System.err.close();
        System.setErr(System.out);
        UserInteraction.startWorking();
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfigurator.class,
                    HibernateConfigurator.class);
            AppenderBuilder appenderBuilder = context.getBean(AppenderBuilder.class);
            appenderBuilder.build();
            localEntityManagerFactoryBean = context.getBean(LocalEntityManagerFactoryBean.class);
            MenuController menuController = context.getBean(MenuController.class);
            menuController.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
            localEntityManagerFactoryBean.destroy();
        }
    }
}

