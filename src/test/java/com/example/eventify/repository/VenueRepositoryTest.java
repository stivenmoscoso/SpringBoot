package com.example.eventify.repository;

import com.example.eventify.model.Venue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class VenueRepositoryTest {
    @Autowired
    private VenueRepository venueRepository;

    @Test
    void savePersistsVenue() {
        Venue venue = new Venue(null, "Auditorio Central", "Calle 123", 300);

        Venue savedVenue = venueRepository.save(venue);

        assertEquals(List.of(savedVenue), venueRepository.findAll());
    }

    @Test
    void findByNombreReturnsMatchingVenues() {
        Venue venue = new Venue(null, "Auditorio Central", "Calle 123", 300);
        venueRepository.save(venue);

        List<Venue> venues = venueRepository.findByNombre("Auditorio Central");

        assertEquals(List.of(venue), venues);
    }
}
