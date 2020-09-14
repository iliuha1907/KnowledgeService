package com.senla.training.hoteladmin.springconfiguration;

import com.senla.training.hoteladmin.util.AppenderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppenderBuilderConfigurator {

    @Bean
    public AppenderBuilder appenderBuilder() {
        return new AppenderBuilder();
    }
}
