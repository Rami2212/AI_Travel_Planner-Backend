package com.aitravelplanner.itinerary.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aitravelplanner.itinerary.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumerService {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ObjectMapper objectMapper;

    // Listen for itinerary responses from AI service
    @KafkaListener(topics = "itinerary.responses", groupId = "itinerary-service")
    public void handleItineraryResponse(String message) {
        try {
            ItineraryResponseMessage response = objectMapper.readValue(message, ItineraryResponseMessage.class);

            UUID itineraryId = UUID.fromString(response.getRequestId());

            if (response.getSuccess()) {
                // Update itinerary with generated plan
                String itineraryJson = objectMapper.writeValueAsString(response.getItinerary());
                itineraryService.updateItineraryWithPlan(itineraryId, itineraryJson);
            } else {
                // Mark itinerary as failed
                itineraryService.markItineraryAsFailed(itineraryId, response.getError());
            }

        } catch (JsonProcessingException e) {
            System.err.println("Failed to process itinerary response: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request ID format: " + e.getMessage());
        }
    }
}