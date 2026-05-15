package com.example.eventify.service;

import com.example.eventify.exception.ResourceNotFoundException;
import com.example.eventify.model.Venue;
import com.example.eventify.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;

    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    public Page<Venue> findAll(Pageable pageable) {
        return venueRepository.findAll(pageable);
    }

    public List<Venue> findByNombre(String nombre) {
        return venueRepository.findByNombre(nombre);
    }

    public Venue findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado"));
    }

    public Venue create(Venue venue) {
        if (venue.getNombre() == null || venue.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (venue.getCapacidad() == null || venue.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }

        return venueRepository.save(venue);
    }

    public Venue update(Long id, Venue venue) {
        Venue existingVenue = findById(id);
        existingVenue.setNombre(venue.getNombre());
        existingVenue.setDireccion(venue.getDireccion());
        existingVenue.setCapacidad(venue.getCapacidad());
        return venueRepository.save(existingVenue);
    }

    public void deleteById(Long id) {
        Venue existingVenue = findById(id);
        venueRepository.delete(existingVenue);
    }
}
