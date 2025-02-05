package com.application.urbanRide.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class SimpleMailConfig {

    @Bean
    SimpleMailMessage getSimpleMailMessage()
    {
        return new SimpleMailMessage();
    }
}
