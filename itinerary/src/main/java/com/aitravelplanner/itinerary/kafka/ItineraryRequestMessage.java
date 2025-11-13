package com.aitravelplanner.itinerary.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Request message sent to AI service
public class ItineraryRequestMessage {
    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("days")
    private Integer days;

    @JsonProperty("interests")
    private List<String> interests;

    // Constructors
    public ItineraryRequestMessage() {}

    public ItineraryRequestMessage(String requestId, String destination, Integer days, List<String> interests) {
        this.requestId = requestId;
        this.destination = destination;
        this.days = days;
        this.interests = interests;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }
}
