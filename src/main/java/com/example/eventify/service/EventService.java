package com.example.eventify.service;


import com.example.eventify.dto.EventCreateDTO;
import com.example.eventify.dto.EventResponseDTO;
import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.exception.ResourceNotFoundException;
import com.example.eventify.model.Category;
import com.example.eventify.model.Event;
import com.example.eventify.model.Venue;
import com.example.eventify.repository.EventRepository;
import com.example.eventify.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final CategoryService categoryService;

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

    public List<EventResponseDTO> findByNombre(String nombre) {
        return eventRepository.findByNombre(nombre).stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponseDTO findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public EventResponseDTO create(EventCreateDTO eventDTO) {
        Event event = new Event();
        event.setNombre(eventDTO.nombre());
        event.setFecha(eventDTO.fecha());
        event.setDescripcion(eventDTO.descripcion());
        event.setVenue(findVenue(eventDTO.venueId()));
        event.setCategories(categoryService.findAllById(eventDTO.categoryIds()));
        event.setActive(true);
        return toResponse(eventRepository.save(event));
    }

    public EventResponseDTO update(Long id, EventCreateDTO eventDTO) {
        Event existingEvent = findEntityById(id);
        existingEvent.setNombre(eventDTO.nombre());
        existingEvent.setFecha(eventDTO.fecha());
        existingEvent.setDescripcion(eventDTO.descripcion());
        existingEvent.setVenue(findVenue(eventDTO.venueId()));
        Set<Category> categories = categoryService.findAllById(eventDTO.categoryIds());
        existingEvent.setCategories(categories);
        return toResponse(eventRepository.save(existingEvent));
    }

    public void deleteById(Long id) {
        Event existingEvent = findEntityById(id);
        existingEvent.deactivate();
        eventRepository.save(existingEvent);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private Venue findVenue(Long venueId) {
        if (venueId == null) {
            throw new IllegalArgumentException("El venue es obligatorio");
        }
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado"));
    }

    private Event findEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
    }

    private EventResponseDTO toResponse(Event event) {
        List<String> categoryNames = event.getCategories() == null
                ? List.of()
                : event.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());

        return new EventResponseDTO(
                event.getId(),
                event.getNombre(),
                event.getFecha(),
                event.getDescripcion(),
                event.getVenue() != null ? event.getVenue().getNombre() : null,
                categoryNames
        );
    }
}
