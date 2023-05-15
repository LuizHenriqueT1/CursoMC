package com.rick.cursomc.configurations;

import com.rick.cursomc.services.DBService;
import com.rick.cursomc.services.EmailService;
import com.rick.cursomc.services.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDatabase() throws ParseException {
        dbService.instanciaDB();
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }
}