package com.example.eventify.service;

import com.example.eventify.dto.VenueCreateDTO;
import com.example.eventify.dto.VenueResponseDTO;
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

    public List<VenueResponseDTO> findByNombre(String nombre) {
        return venueRepository.findByNombre(nombre).stream()
                .map(this::toResponse)
                .toList();
    }

    public VenueResponseDTO findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public VenueResponseDTO create(VenueCreateDTO venueDTO) {
        Venue venue = new Venue();
        venue.setNombre(venueDTO.nombre());
        venue.setDireccion(venueDTO.direccion());
        venue.setCapacidad(venueDTO.capacidad());
        venue.setCiudad(venueDTO.ciudad());
        return toResponse(venueRepository.save(venue));
    }

    public VenueResponseDTO update(Long id, VenueCreateDTO venueDTO) {
        Venue existingVenue = findEntityById(id);
        existingVenue.setNombre(venueDTO.nombre());
        existingVenue.setDireccion(venueDTO.direccion());
        existingVenue.setCapacidad(venueDTO.capacidad());
        existingVenue.setCiudad(venueDTO.ciudad());
        return toResponse(venueRepository.save(existingVenue));
    }

    public void deleteById(Long id) {
        Venue existingVenue = findEntityById(id);
        venueRepository.delete(existingVenue);
    }

    private Venue findEntityById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado"));
    }

    private VenueResponseDTO toResponse(Venue venue) {
        return new VenueResponseDTO(
                venue.getId(),
                venue.getNombre(),
                venue.getDireccion(),
                venue.getCapacidad(),
                venue.getCiudad()
        );
    }
}
