package com.example.eventify.service;


import com.example.eventify.model.Event;
import com.example.eventify.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public List<Event> findByNombre(String nombre) {
        return eventRepository.findByNombre(nombre);
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }
    public Event create(Event event) {
        if  (event.getNombre() == null || event.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return eventRepository.save(event);
    }
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }
}
