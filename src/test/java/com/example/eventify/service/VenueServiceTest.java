package com.example.eventify.service;

import com.example.eventify.model.Venue;
import com.example.eventify.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {
    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    @Test
    void findAllReturnsRepositoryVenues() {
        List<Venue> venues = List.of(new Venue(1L, "Auditorio Central", "Calle 123", 300));
        when(venueRepository.findAll()).thenReturn(venues);

        List<Venue> result = venueService.findAll();

        assertEquals(venues, result);
        verify(venueRepository).findAll();
    }

    @Test
    void findByIdReturnsVenueWhenExists() {
        Venue venue = new Venue(1L, "Auditorio Central", "Calle 123", 300);
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));

        Venue result = venueService.findById(1L);

        assertEquals(venue, result);
        verify(venueRepository).findById(1L);
    }

    @Test
    void createRejectsBlankName() {
        Venue venue = new Venue(null, " ", "Calle 123", 300);

        assertThrows(IllegalArgumentException.class, () -> venueService.create(venue));
        verify(venueRepository, never()).save(venue);
    }

    @Test
    void createRejectsInvalidCapacity() {
        Venue venue = new Venue(null, "Auditorio Central", "Calle 123", 0);

        assertThrows(IllegalArgumentException.class, () -> venueService.create(venue));
        verify(venueRepository, never()).save(venue);
    }

    @Test
    void createSavesValidVenue() {
        Venue venue = new Venue(null, "Auditorio Central", "Calle 123", 300);
        Venue savedVenue = new Venue(1L, "Auditorio Central", "Calle 123", 300);
        when(venueRepository.save(venue)).thenReturn(savedVenue);

        Venue result = venueService.create(venue);

        assertEquals(savedVenue, result);
        verify(venueRepository).save(venue);
    }
}
