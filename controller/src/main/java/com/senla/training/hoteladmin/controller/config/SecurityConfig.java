package com.senla.training.hoteladmin.controller.config;

import com.senla.training.hoteladmin.controller.security.TokenFilter;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(value = "com.senla.training.hoteladmin")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenFilter tokenFilter;

    @Override
    protected void configure(HttpSecurity http) {
        try {
            http.csrf()
                    .disable()
                    .authorizeRequests()
                    .antMatchers("/authentication/login")
                    .permitAll()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        } catch (Exception ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
        try {
            return super.authenticationManagerBean();
        } catch (Exception ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
