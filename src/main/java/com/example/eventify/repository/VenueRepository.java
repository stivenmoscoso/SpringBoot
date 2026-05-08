package com.example.eventify.repository;

import com.example.eventify.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class VenueRepository {
    private final Map<Long, Venue> venues = new HashMap<>();
    private Long currentId = 1L;

    public List<Venue> findAll() {
        return new ArrayList<>(venues.values());
    }

    public Optional<Venue> findById(Long id) {
        return Optional.ofNullable(venues.get(id));
    }

    public Venue save(Venue venue) {
        if (venue.getId() == null) {
            venue.setId(currentId);
            currentId++;
        }

        venues.put(venue.getId(), venue);
        return venue;
    }

    public void deleteById(Long id) {
        venues.remove(id);
    }
}
