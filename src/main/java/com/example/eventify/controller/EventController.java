package com.example.eventify.controller;


import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.model.Event;
import com.example.eventify.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Operaciones para gestionar eventos")
public class EventController {
    private final EventService eventService;

    @GetMapping
    @Operation(
            summary = "Listar eventos con filtros avanzados",
            description = """
                    Devuelve Slice<EventSummaryDTO>, un record plano para listados masivos.
                    Los filtros relacionales se aplican sobre el venue y las categorias asociadas.
                    Los eventos eliminados logicamente no se incluyen en el resultado.
                    """
    )
    public ResponseEntity<Slice<EventSummaryDTO>> findAll(
            @Parameter(description = "Ciudad del venue. Filtra por coincidencia exacta sin distinguir mayusculas.", example = "Bogota")
            @RequestParam(required = false) String city,
            @Parameter(description = "Nombre de categoria asociada al evento. Solo devuelve eventos vinculados a esa categoria.", example = "Deportes")
            @RequestParam(required = false) String category,
            @Parameter(description = "Capacidad minima del venue. Devuelve eventos cuyo lugar soporte al menos este aforo.", example = "300")
            @RequestParam(required = false) Integer capacity,
            @Parameter(description = "Fecha inicial inclusiva del evento. Formato ISO yyyy-MM-dd.", example = "2026-06-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha final inclusiva del evento. Formato ISO yyyy-MM-dd.", example = "2026-06-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject @PageableDefault(page = 0, size = 8, sort = "nombre") Pageable pageable
    ) {
        return ResponseEntity.ok(eventService.findByFilters(city, category, capacity, startDate, endDate, pageable));
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
    @Operation(
            summary = "Eliminar evento logicamente",
            description = """
                    Marca el evento como eliminado sin remover el registro fisico.
                    Desde ese momento el evento queda excluido de listados, filtros y consultas por id.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evento eliminado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Identificador del evento activo que se marcara como eliminado logicamente.", example = "1")
            @PathVariable Long id
    ) {
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
