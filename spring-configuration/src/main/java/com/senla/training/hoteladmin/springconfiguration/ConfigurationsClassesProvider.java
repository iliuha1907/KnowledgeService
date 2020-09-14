package com.senla.training.hoteladmin.springconfiguration;

public class ConfigurationsClassesProvider {

    public static Class<?>[] getClasses() {
        return new Class<?>[]{AppenderBuilderConfigurator.class, ControllerConfigurator.class, CsvConfigurator.class,
                DaoConfigurator.class, PropertyConfigurator.class, ServiceConfigurator.class, ViewConfigurator.class};
    }
}
