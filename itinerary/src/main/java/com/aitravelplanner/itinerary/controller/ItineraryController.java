package com.aitravelplanner.itinerary.controller;

import com.aitravelplanner.itinerary.model.Itinerary;
import com.aitravelplanner.itinerary.service.ItineraryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/itineraries")
@CrossOrigin(origins = "*")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    // Create new itinerary request
    @PostMapping("/create")
    public ResponseEntity<?> createItinerary(@RequestBody @Valid ItineraryCreateRequest request) {
        try {
            Itinerary itinerary = itineraryService.createItineraryRequest(
                    request.getUserId(),
                    request.getDestination(),
                    request.getDays(),
                    request.getInterests()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("id", itinerary.getId());
            response.put("destination", itinerary.getDestination());
            response.put("days", itinerary.getDays());
            response.put("status", itinerary.getStatus().name());
            response.put("message", "Itinerary request submitted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get itinerary by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getItinerary(@PathVariable UUID id) {
        Optional<Itinerary> itineraryOpt = itineraryService.getItineraryById(id);

        if (itineraryOpt.isPresent()) {
            Itinerary itinerary = itineraryOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("id", itinerary.getId());
            response.put("destination", itinerary.getDestination());
            response.put("days", itinerary.getDays());
            response.put("interests", itineraryService.parseInterests(itinerary.getInterests()));
            response.put("status", itinerary.getStatus().name());
            response.put("planJson", itinerary.getPlanJson());
            response.put("createdAt", itinerary.getCreatedAt());
            response.put("updatedAt", itinerary.getUpdatedAt());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get user's itineraries
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserItineraries(@PathVariable UUID userId) {
        List<Itinerary> itineraries = itineraryService.getUserItineraries(userId);

        List<Map<String, Object>> response = itineraries.stream()
                .map(itinerary -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", itinerary.getId());
                    item.put("destination", itinerary.getDestination());
                    item.put("days", itinerary.getDays());
                    item.put("status", itinerary.getStatus().name());
                    item.put("createdAt", itinerary.getCreatedAt());
                    return item;
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    // Delete itinerary
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteItinerary(@PathVariable UUID id, @PathVariable UUID userId) {
        boolean deleted = itineraryService.deleteItinerary(id, userId);

        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Itinerary deleted successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Itinerary not found or unauthorized"));
        }
    }

    // Admin: Get all itineraries
    @GetMapping("/admin/all")
    public ResponseEntity<List<Map<String, Object>>> getAllItineraries() {
        List<Itinerary> itineraries = itineraryService.getAllItineraries();

        List<Map<String, Object>> response = itineraries.stream()
                .map(itinerary -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", itinerary.getId());
                    item.put("userId", itinerary.getUserId());
                    item.put("destination", itinerary.getDestination());
                    item.put("days", itinerary.getDays());
                    item.put("status", itinerary.getStatus().name());
                    item.put("createdAt", itinerary.getCreatedAt());
                    return item;
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    // Admin: Get itinerary statistics
    @GetMapping("/admin/stats")
    public ResponseEntity<?> getItineraryStats() {
        var stats = itineraryService.getItineraryStats();

        Map<String, Object> response = new HashMap<>();
        response.put("totalItineraries", stats.getTotalItineraries());
        response.put("completedItineraries", stats.getCompletedItineraries());
        response.put("pendingItineraries", stats.getPendingItineraries());
        response.put("failedItineraries", stats.getFailedItineraries());

        return ResponseEntity.ok(response);
    }
}

// Request DTO
class ItineraryCreateRequest {
    private UUID userId;
    private String destination;
    private Integer days;
    private List<String> interests;

    // Getters and Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }
}