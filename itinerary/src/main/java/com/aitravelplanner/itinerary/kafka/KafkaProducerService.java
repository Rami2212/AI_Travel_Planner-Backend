package com.aitravelplanner.itinerary.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String ITINERARY_REQUEST_TOPIC = "itinerary.requests";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // Send itinerary request to AI service
    public void sendItineraryRequest(ItineraryRequestMessage message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(ITINERARY_REQUEST_TOPIC, message.getRequestId(), jsonMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize itinerary request", e);
        }
    }
}