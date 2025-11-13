package com.aitravelplanner.itinerary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "itineraries", schema = "itinerary_service")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User ID is required")
    @Column(nullable = false)
    private UUID userId;

    @NotBlank(message = "Destination is required")
    @Column(nullable = false, length = 255)
    private String destination;

    @Min(value = 1, message = "Days must be at least 1")
    @Column(nullable = false)
    private Integer days;

    @Column(columnDefinition = "TEXT")
    private String interests; // JSON string of interests array

    @Column(columnDefinition = "TEXT")
    private String planJson; // Generated itinerary JSON

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItineraryStatus status = ItineraryStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Itinerary() {}

    public Itinerary(UUID userId, String destination, Integer days, String interests) {
        this.userId = userId;
        this.destination = destination;
        this.days = days;
        this.interests = interests;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public String getPlanJson() { return planJson; }
    public void setPlanJson(String planJson) { this.planJson = planJson; }

    public ItineraryStatus getStatus() { return status; }
    public void setStatus(ItineraryStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}