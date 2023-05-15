package com.rick.cursomc.configurations;

import com.rick.cursomc.services.DBService;
import com.rick.cursomc.services.EmailService;
import com.rick.cursomc.services.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private DBService dbService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String value;

    @Bean
    public boolean instanciaDB() throws ParseException {
        if(value.equals("create")) {
            dbService.instanciaDB();
        }
        return false;
    }

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }

}
