package com.example.eventify.repository;

import com.example.eventify.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    void findAllReturnsEmptyListWhenNoEventsWereSaved() {
        List<Event> events = eventRepository.findAll();

        assertTrue(events.isEmpty());
    }

    @Test
    void savePersistsEvent() {
        Event event = new Event(null, "Mundial de futbol", LocalDate.of(2026, 6, 11), "Canada, EEUU, Mexico");

        Event savedEvent = eventRepository.save(event);

        assertEquals(List.of(savedEvent), eventRepository.findAll());
    }

    @Test
    void findByNombreReturnsMatchingEvents() {
        Event event = new Event(null, "Mundial de futbol", LocalDate.of(2026, 6, 11), "Canada, EEUU, Mexico");
        eventRepository.save(event);

        List<Event> events = eventRepository.findByNombre("Mundial de futbol");

        assertEquals(List.of(event), events);
    }
}
