package com.example.eventify.service;

import com.example.eventify.model.Event;
import com.example.eventify.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void findAllReturnsRepositoryEvents() {
        List<Event> events = List.of(new Event(1L, "Concierto", LocalDate.now(), "Musica en vivo"));
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.findAll();

        assertEquals(events, result);
        verify(eventRepository).findAll();
    }

    @Test
    void findByIdReturnsEventWhenExists() {
        Event event = new Event(1L, "Concierto", LocalDate.now(), "Musica en vivo");
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.findById(1L);

        assertEquals(event, result);
        verify(eventRepository).findById(1L);
    }

    @Test
    void createRejectsBlankName() {
        Event event = new Event(null, " ", LocalDate.now(), "Musica en vivo");

        assertThrows(IllegalArgumentException.class, () -> eventService.create(event));
        verify(eventRepository, never()).save(event);
    }

    @Test
    void createSavesValidEvent() {
        Event event = new Event(null, "Concierto", LocalDate.now(), "Musica en vivo");
        Event savedEvent = new Event(1L, "Concierto", LocalDate.now(), "Musica en vivo");
        when(eventRepository.save(event)).thenReturn(savedEvent);

        Event result = eventService.create(event);

        assertEquals(savedEvent, result);
        verify(eventRepository).save(event);
    }
}
