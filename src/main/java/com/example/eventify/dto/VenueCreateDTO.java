package com.example.eventify.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VenueCreateDTO(
        @NotBlank(message = "El nombre del venue es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener de 2 a 50 caracteres")
        String nombre,

        @NotBlank(message = "La direccion es obligatoria")
        @Size(max = 50, message = "La direccion no puede superar 50 caracteres")
        String direccion,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor que cero")
        Integer capacidad,

        @NotBlank(message = "La ciudad es obligatoria")
        @Size(max = 50, message = "La ciudad no puede superar 50 caracteres")
        String ciudad
) {
}
