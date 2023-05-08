package me.widua.linkshortener.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class BeanConfiguration {

    @Bean
    public Random random(){
        return new Random();
    }

}
