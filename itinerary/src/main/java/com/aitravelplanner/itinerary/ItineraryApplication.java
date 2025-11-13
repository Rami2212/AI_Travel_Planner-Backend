package com.aitravelplanner.itinerary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@Slf4j
@SpringBootApplication
@EnableKafka
public class ItineraryApplication {

	public static void main(String[] args) {

		SpringApplication.run(ItineraryApplication.class, args);

		log.info("Itinerary Service is running...");

	}

}
