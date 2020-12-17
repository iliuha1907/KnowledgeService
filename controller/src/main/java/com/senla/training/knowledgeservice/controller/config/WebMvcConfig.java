package com.senla.training.knowledgeservice.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {"com.senla.training.knowledgeservice"})
public class WebMvcConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
            throws IOException {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer
                = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(
                Boolean.TRUE);
        propertySourcesPlaceholderConfigurer.setLocations(
                new PathMatchingResourcePatternResolver().getResources(
                        "classpath:app.properties"));
        return propertySourcesPlaceholderConfigurer;
    }
}
