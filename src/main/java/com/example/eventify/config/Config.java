package com.example.eventify.config;

import com.example.eventify.seeder.EventSeeder;
import com.example.eventify.seeder.UserSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    CommandLineRunner loadInitialData(
            UserSeeder userSeeder,
            EventSeeder eventSeeder
    ) {
        return args -> {
            userSeeder.seed();
            eventSeeder.seed();
        };
    }
}
