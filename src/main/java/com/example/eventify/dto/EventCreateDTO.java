package com.example.eventify.dto;

import com.example.eventify.validation.NoPastEvents;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record EventCreateDTO(
        @Null(message = "No debes enviar el id al crear un evento")
        @NotNull(message = "El id es obligatorio al actualizar un evento")
        Long id,

        @NotBlank( message = "El nombre del evento es obligatorio")
        @Size( min = 3, max = 100, message = "El nombre debe tener de 3 a 100 caracteres")
        String nombre,

        @NotNull( message = "La fecha es obligatoria")
        @Future(message = "La fecha debe ser posterior al momento actual")
        LocalDate fecha,

        @Size( max = 500, message = "La descripcion no puede superar 500 caracteres")
        String descripcion,

        @NotNull( message = "El venue es obligatorio")
        Long venueId,

        List<Long> categoryIds
) {
}
