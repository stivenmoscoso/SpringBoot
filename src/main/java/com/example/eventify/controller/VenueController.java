package com.example.eventify.controller;

import com.example.eventify.dto.VenueCreateDTO;
import com.example.eventify.dto.VenueResponseDTO;
import com.example.eventify.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<com.example.eventify.model.Venue>> findAll(@ParameterObject @PageableDefault(page = 0, size = 8, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(venueService.findAll(pageable));
    }

    @GetMapping("/consulta")
    @Operation(summary = "Consultar venues por nombre")
    public ResponseEntity<List<VenueResponseDTO>> findByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(venueService.findByNombre(nombre));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar venue por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venue encontrado"),
            @ApiResponse(responseCode = "404", description = "Venue no encontrado")
    })
    public ResponseEntity<VenueResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar venue")
    public ResponseEntity<VenueResponseDTO> create(@RequestBody VenueCreateDTO newVenue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.create(newVenue));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar venue")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venue actualizado"),
            @ApiResponse(responseCode = "404", description = "Venue no encontrado")
    })
    public ResponseEntity<VenueResponseDTO> update(@PathVariable Long id, @RequestBody VenueCreateDTO newVenue) {
        return ResponseEntity.ok(venueService.update(id, newVenue));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venue")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Venue eliminado"),
            @ApiResponse(responseCode = "404", description = "Venue no encontrado")
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        venueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
