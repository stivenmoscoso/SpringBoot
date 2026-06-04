package com.example.eventify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Respuesta plana de evento para operaciones CRUD individuales.")
public record EventResponseDTO(
        @Schema(description = "Identificador del evento", example = "1")
        Long id,
        @Schema(description = "Nombre del evento", example = "Concierto de verano")
        String nombre,
        @Schema(description = "Fecha del evento", example = "2026-06-11")
        LocalDate fecha,
        @Schema(description = "Descripcion del evento", example = "Evento al aire libre")
        String descripcion,
        @Schema(description = "Nombre del venue asociado", example = "Auditorio Central")
        String venueName,
        @Schema(description = "Nombres de las categorias asociadas")
        List<String> categoryNames
) {
}
