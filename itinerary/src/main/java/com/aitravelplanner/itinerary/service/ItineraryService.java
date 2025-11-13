package com.aitravelplanner.itinerary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aitravelplanner.itinerary.kafka.ItineraryRequestMessage;
import com.aitravelplanner.itinerary.kafka.KafkaProducerService;
import com.aitravelplanner.itinerary.model.Itinerary;
import com.aitravelplanner.itinerary.model.ItineraryStatus;
import com.aitravelplanner.itinerary.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    // Create new itinerary request
    public Itinerary createItineraryRequest(UUID userId, String destination, Integer days, List<String> interests) {
        // Save itinerary to database
        Itinerary itinerary = new Itinerary(userId, destination, days, String.join(",", interests));
        itinerary = itineraryRepository.save(itinerary);

        // Send request to AI service via Kafka
        ItineraryRequestMessage requestMessage = new ItineraryRequestMessage(
                itinerary.getId().toString(),
                destination,
                days,
                interests
        );

        kafkaProducerService.sendItineraryRequest(requestMessage);

        return itinerary;
    }

    // Update itinerary with generated plan
    public void updateItineraryWithPlan(UUID itineraryId, String planJson) {
        Optional<Itinerary> itineraryOpt = itineraryRepository.findById(itineraryId);
        if (itineraryOpt.isPresent()) {
            Itinerary itinerary = itineraryOpt.get();
            itinerary.setPlanJson(planJson);
            itinerary.setStatus(ItineraryStatus.COMPLETED);
            itineraryRepository.save(itinerary);
        }
    }

    // Mark itinerary as failed
    public void markItineraryAsFailed(UUID itineraryId, String error) {
        Optional<Itinerary> itineraryOpt = itineraryRepository.findById(itineraryId);
        if (itineraryOpt.isPresent()) {
            Itinerary itinerary = itineraryOpt.get();
            itinerary.setStatus(ItineraryStatus.FAILED);
            // Store error in planJson field for now
            itinerary.setPlanJson("{\"error\":\"" + error + "\"}");
            itineraryRepository.save(itinerary);
        }
    }

    // Get itinerary by ID
    public Optional<Itinerary> getItineraryById(UUID id) {
        return itineraryRepository.findById(id);
    }

    // Get user's itineraries
    public List<Itinerary> getUserItineraries(UUID userId) {
        return itineraryRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Get all itineraries (Admin only)
    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAllOrderByCreatedAtDesc();
    }

    // Delete itinerary
    public boolean deleteItinerary(UUID itineraryId, UUID userId) {
        Optional<Itinerary> itineraryOpt = itineraryRepository.findById(itineraryId);
        if (itineraryOpt.isPresent()) {
            Itinerary itinerary = itineraryOpt.get();
            if (itinerary.getUserId().equals(userId)) {
                itineraryRepository.delete(itinerary);
                return true;
            }
        }
        return false;
    }

    // Get itinerary statistics
    public ItineraryStats getItineraryStats() {
        long totalItineraries = itineraryRepository.countTotalItineraries();
        long completedItineraries = itineraryRepository.countByStatus(ItineraryStatus.COMPLETED);
        long pendingItineraries = itineraryRepository.countByStatus(ItineraryStatus.PENDING);
        long failedItineraries = itineraryRepository.countByStatus(ItineraryStatus.FAILED);

        return new ItineraryStats(totalItineraries, completedItineraries, pendingItineraries, failedItineraries);
    }

    // Parse interests string back to list
    public List<String> parseInterests(String interestsString) {
        if (interestsString == null || interestsString.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(interestsString.split(","));
    }
}

