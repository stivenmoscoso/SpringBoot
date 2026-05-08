package com.example.eventify.controller;

import com.example.eventify.model.Venue;
import com.example.eventify.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venues", description = "Operaciones para gestionar venues")
public class VenueController {
    private final VenueService venueService;

    @GetMapping
    @Operation(summary = "Listar venues")
    public ResponseEntity<List<Venue>> findAll() {
        return ResponseEntity.ok(venueService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar venue por id")
    public ResponseEntity<Venue> findById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar venue")
    public ResponseEntity<Venue> create(@RequestBody Venue newVenue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.create(newVenue));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venue")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        venueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
