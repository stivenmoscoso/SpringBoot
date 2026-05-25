package com.example.eventify.controller;


import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.model.Event;
import com.example.eventify.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Operaciones para gestionar eventos")
public class EventController {
    private final EventService eventService;

    @GetMapping
    @Operation(summary = "Listar eventos")
    public ResponseEntity<Slice<EventSummaryDTO>> findAll(@ParameterObject @PageableDefault(page = 0, size = 8, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(eventService.findAll(pageable));
    }

    @GetMapping("/consulta")
    @Operation(summary = "Consultar eventos por nombre")
    public ResponseEntity<List<Event>> findByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(eventService.findByNombre(nombre));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar evento por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<Event> findById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar evento")
    public ResponseEntity<Event> create(@RequestBody Event newEvent) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(newEvent));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evento eliminado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento actualizado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<Event> update(@PathVariable Long id, @RequestBody Event newEvent) {
        return ResponseEntity.ok(eventService.update(id, newEvent));
    }
}
