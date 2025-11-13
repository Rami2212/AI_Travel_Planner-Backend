package com.aitravelplanner.itinerary.repository;

import com.aitravelplanner.itinerary.model.Itinerary;
import com.aitravelplanner.itinerary.model.ItineraryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, UUID> {

    // Find all itineraries by user ID
    List<Itinerary> findByUserIdOrderByCreatedAtDesc(UUID userId);

    // Find itineraries by status
    List<Itinerary> findByStatusOrderByCreatedAtDesc(ItineraryStatus status);

    // Count total itineraries
    @Query("SELECT COUNT(i) FROM Itinerary i")
    long countTotalItineraries();

    // Count itineraries by status
    long countByStatus(ItineraryStatus status);

    // Find all itineraries ordered by creation date
    @Query("SELECT i FROM Itinerary i ORDER BY i.createdAt DESC")
    List<Itinerary> findAllOrderByCreatedAtDesc();
}