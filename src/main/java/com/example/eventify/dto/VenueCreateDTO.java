package com.example.eventify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public record VenueCreateDTO(
        @Schema(description = "Id del venue para actualizacion", example = "1")
        @Null(message = "No debes enviar el id al crear un venue")
        @NotNull(message = "El id es obligatorio al actualizar un venue")
        Long id,

        @Schema(description = "Nombre del venue", example = "Auditorio Central", minLength = 3, maxLength = 100)
        @NotBlank(message = "El nombre del venue es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener de 3 a 100 caracteres")
        String nombre,

        @Schema(description = "Direccion del venue", example = "Calle 123", maxLength = 50)
        @NotBlank(message = "La direccion es obligatoria")
        @Size(max = 50, message = "La direccion no puede superar 50 caracteres")
        String direccion,

        @Schema(description = "Capacidad del venue", example = "250", minimum = "1")
        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor que cero")
        Integer capacidad,

        @Schema(description = "Ciudad del venue", example = "Bogota", maxLength = 50)
        @NotBlank(message = "La ciudad es obligatoria")
        @Size(max = 50, message = "La ciudad no puede superar 50 caracteres")
        String ciudad
) {
}
