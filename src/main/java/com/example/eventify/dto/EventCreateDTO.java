package com.example.eventify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record EventCreateDTO(
        @NotBlank (message = "El nombre del evento es obligatorio")
        @Size (min = 2, max = 200, message = "El titulo debe tener de 2 a 200 caracteres")
        String nombre,

        @NotNull
        LocalDate fecha,

        @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
        String descripcion,

        @NotNull(message = "El venue es obligatorio")
        Long venueId,

        List<Long> categoryIds
) {
}
