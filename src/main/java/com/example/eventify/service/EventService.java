package com.example.eventify.service;


import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.exception.ResourceNotFoundException;
import com.example.eventify.model.Event;
import com.example.eventify.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Slice<EventSummaryDTO> findByCity(String city, Pageable pageable) {
        return eventRepository.findByCity(blankToNull(city), pageable);
    }

    public Slice<EventSummaryDTO> findByCategory(String category, Pageable pageable) {
        return eventRepository.findByCategory(blankToNull(category), pageable);
    }

    public Slice<EventSummaryDTO> findByCapacity(Integer capacity, Pageable pageable) {
        return eventRepository.findByCapacityGreaterThanEqual(capacity, pageable);
    }

    public Slice<EventSummaryDTO> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return eventRepository.findByDateBetween(startDate, endDate, pageable);
    }

    public Slice<EventSummaryDTO> findByFilters(
            String city,
            String category,
            Integer capacity,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        String normalizedCity = blankToNull(city);
        String normalizedCategory = blankToNull(category);

        if (normalizedCity != null
                && normalizedCategory == null
                && capacity == null
                && startDate == null
                && endDate == null) {
            return findByCity(normalizedCity, pageable);
        }

        if (normalizedCity == null
                && normalizedCategory != null
                && capacity == null
                && startDate == null
                && endDate == null) {
            return findByCategory(normalizedCategory, pageable);
        }

        if (normalizedCity == null
                && normalizedCategory == null
                && capacity != null
                && startDate == null
                && endDate == null) {
            return findByCapacity(capacity, pageable);
        }

        if (normalizedCity == null
                && normalizedCategory == null
                && capacity == null
                && startDate != null
                && endDate != null) {
            return findByDateBetween(startDate, endDate, pageable);
        }

        return eventRepository.findByFilters(
                normalizedCity,
                normalizedCategory,
                capacity,
                startDate,
                endDate,
                pageable
        );
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
        event.setActive(true);
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
        existingEvent.deactivate();
        eventRepository.save(existingEvent);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
