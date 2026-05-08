package com.example.eventify.repository;

import com.example.eventify.model.Event;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventRepositoryTest {
    @Test
    void findAllReturnsEmptyListWhenNoEventsWereSaved() {
        EventRepository eventRepository = new EventRepository();

        List<Event> events = eventRepository.findAll();

        assertTrue(events.isEmpty());
    }

    @Test
    void saveStoresEventInMemoryCollection() {
        EventRepository eventRepository = new EventRepository();
        Event event = new Event(null, "Mundial de futbol", LocalDate.of(2026, 6, 11), "Canada, EEUU, Mexico");

        Event savedEvent = eventRepository.save(event);

        assertEquals(1L, savedEvent.getId());
        assertEquals(List.of(savedEvent), eventRepository.findAll());
    }
}
