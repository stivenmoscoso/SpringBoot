package com.example.eventify.controller;


import com.example.eventify.model.Event;
import com.example.eventify.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Event>> findAll() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/consulta")
    @Operation(summary = "Consultar eventos por nombre")
    public ResponseEntity<List<Event>> findByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(eventService.findByNombre(nombre));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar evento por id")
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
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
