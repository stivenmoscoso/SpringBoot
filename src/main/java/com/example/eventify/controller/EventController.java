package com.example.eventify.controller;

import com.example.eventify.dto.EventCreateDTO;
import com.example.eventify.dto.EventResponseDTO;
import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                    Devuelve un `Slice<EventSummaryDTO>` basado en records para listados masivos.
                    Los filtros relacionales se aplican sobre el venue y las categorias asociadas.
                    El resultado se ordena cronologicamente por fecha descendente.
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
            @Parameter(description = "Paginacion y orden adicional. Si se usa, se combina con el orden cronologico descendente del repositorio.", hidden = true)
            @ParameterObject @PageableDefault(page = 0, size = 8, sort = "fecha", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(eventService.findByFilters(city, category, capacity, startDate, endDate, pageable));
    }

    @GetMapping("/consulta")
    @Operation(summary = "Consultar eventos por nombre")
    public ResponseEntity<List<EventResponseDTO>> findByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(eventService.findByNombre(nombre));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar evento por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<EventResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar evento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento creado"),
            @ApiResponse(responseCode = "400", description = "Validacion fallida",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Recurso duplicado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Error inesperado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventCreateDTO newEvent) {
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
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento actualizado"),
            @ApiResponse(responseCode = "400", description = "Validacion fallida",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
            @ApiResponse(responseCode = "409", description = "Recurso duplicado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "422", description = "Violacion de regla de negocio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    })
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EventCreateDTO newEvent) {
        return ResponseEntity.ok(eventService.update(id, newEvent));
    }
}
