package com.aitravelplanner.itinerary.service;

// Stats class
public class ItineraryStats {
    private long totalItineraries;
    private long completedItineraries;
    private long pendingItineraries;
    private long failedItineraries;

    public ItineraryStats(long totalItineraries, long completedItineraries, long pendingItineraries, long failedItineraries) {
        this.totalItineraries = totalItineraries;
        this.completedItineraries = completedItineraries;
        this.pendingItineraries = pendingItineraries;
        this.failedItineraries = failedItineraries;
    }

    // Getters
    public long getTotalItineraries() { return totalItineraries; }
    public long getCompletedItineraries() { return completedItineraries; }
    public long getPendingItineraries() { return pendingItineraries; }
    public long getFailedItineraries() { return failedItineraries; }
}
