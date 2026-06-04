package com.example.eventify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta plana de venue para operaciones CRUD individuales.")
public record VenueResponseDTO(
        @Schema(description = "Identificador del venue", example = "1")
        Long id,
        String nombre,
        String direccion,
        Integer capacidad,
        String ciudad
) {
}
