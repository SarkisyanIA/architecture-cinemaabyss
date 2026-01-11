package com.cinemaabyss.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class EventsApplication {

//    docker-compose build --no-cache events-service
//    docker-compose down --rmi all
    public static void main(String[] args) {
        SpringApplication.run(EventsApplication.class, args);
    }

}
