package com.example.eventify.service;

import com.example.eventify.dto.VenueCreateDTO;
import com.example.eventify.dto.VenueResponseDTO;
import com.example.eventify.exception.ResourceNotFoundException;
import com.example.eventify.mapper.VenueMapper;
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
    private final VenueMapper venueMapper;

    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    public Page<VenueResponseDTO> findAll(Pageable pageable) {
        return venueRepository.findAll(pageable).map(venueMapper::toResponse);
    }

    public List<VenueResponseDTO> findByNombre(String nombre) {
        return venueRepository.findByNombre(nombre).stream()
                .map(venueMapper::toResponse)
                .toList();
    }

    public VenueResponseDTO findById(Long id) {
        return venueMapper.toResponse(findEntityById(id));
    }

    public VenueResponseDTO create(VenueCreateDTO venueDTO) {
        return venueMapper.toResponse(venueRepository.save(venueMapper.toEntity(venueDTO)));
    }

    public VenueResponseDTO update(Long id, VenueCreateDTO venueDTO) {
        Venue existingVenue = findEntityById(id);
        venueMapper.updateEntity(venueDTO, existingVenue);
        return venueMapper.toResponse(venueRepository.save(existingVenue));
    }

    public void deleteById(Long id) {
        Venue existingVenue = findEntityById(id);
        venueRepository.delete(existingVenue);
    }

    private Venue findEntityById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado"));
    }
}
