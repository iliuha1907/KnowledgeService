package com.senla.training.hoteladmin.springconfiguration;

import com.senla.training.hoteladmin.view.Builder;
import com.senla.training.hoteladmin.view.MenuController;
import com.senla.training.hoteladmin.view.Navigator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewConfigurator {

    @Bean
    public Builder builder() {
        return new Builder();
    }

    @Bean
    public MenuController menuController() {
        return new MenuController();
    }

    @Bean
    public Navigator navigator() {
        return new Navigator();
    }
}
