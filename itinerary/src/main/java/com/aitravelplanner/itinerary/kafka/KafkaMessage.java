package com.aitravelplanner.itinerary.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Response message received from AI service
class ItineraryResponseMessage {
    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("itinerary")
    private List<DayPlan> itinerary;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("error")
    private String error;

    // Constructors
    public ItineraryResponseMessage() {}

    public ItineraryResponseMessage(String requestId, List<DayPlan> itinerary) {
        this.requestId = requestId;
        this.itinerary = itinerary;
        this.success = true;
    }

    public ItineraryResponseMessage(String requestId, String error) {
        this.requestId = requestId;
        this.error = error;
        this.success = false;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public List<DayPlan> getItinerary() { return itinerary; }
    public void setItinerary(List<DayPlan> itinerary) { this.itinerary = itinerary; }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

// Day plan within itinerary
class DayPlan {
    @JsonProperty("day")
    private Integer day;

    @JsonProperty("plan")
    private String plan;

    // Constructors
    public DayPlan() {}

    public DayPlan(Integer day, String plan) {
        this.day = day;
        this.plan = plan;
    }

    // Getters and Setters
    public Integer getDay() { return day; }
    public void setDay(Integer day) { this.day = day; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
}