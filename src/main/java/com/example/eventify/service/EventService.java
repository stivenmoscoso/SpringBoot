package com.example.eventify.service;


import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.exception.ResourceNotFoundException;
import com.example.eventify.model.Event;
import com.example.eventify.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Slice<EventSummaryDTO> findAll(Pageable pageable) {

        return eventRepository.findSummaries(pageable);
    }

    public List<Event> findByNombre(String nombre) {
        return eventRepository.findByNombre(nombre);
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
    }

    public Event create(Event event) {
        if  (event.getNombre() == null || event.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return eventRepository.save(event);
    }

    public Event update(Long id, Event event) {
        Event existingEvent = findById(id);
        existingEvent.setNombre(event.getNombre());
        existingEvent.setFecha(event.getFecha());
        existingEvent.setDescripcion(event.getDescripcion());
        existingEvent.setVenue(event.getVenue());
        if (event.getCategories() != null) {
            existingEvent.setCategories(event.getCategories());
        }
        return eventRepository.save(existingEvent);
    }

    public void deleteById(Long id) {
        Event existingEvent = findById(id);
        eventRepository.delete(existingEvent);
    }
}
