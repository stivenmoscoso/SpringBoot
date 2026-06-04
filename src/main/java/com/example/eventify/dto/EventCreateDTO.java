package com.example.eventify.dto;

import com.example.eventify.validation.NoPastEvents;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record EventCreateDTO(
        @Schema(description = "Id del evento para actualizacion", example = "1")
        @Null(message = "No debes enviar el id al crear un evento")
        @NotNull(message = "El id es obligatorio al actualizar un evento")
        Long id,

        @Schema(description = "Nombre del evento", example = "Concierto de verano", minLength = 3, maxLength = 100)
        @NotBlank(message = "El nombre del evento es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener de 3 a 100 caracteres")
        String nombre,

        @Schema(description = "Fecha del evento", example = "2026-06-11")
        @NotNull(message = "La fecha es obligatoria")
        @Future(message = "La fecha debe ser posterior al momento actual")
        LocalDate fecha,

        @Schema(description = "Descripcion del evento", example = "Evento al aire libre", maxLength = 500)
        @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
        String descripcion,

        @Schema(description = "Id del venue asociado", example = "1")
        @NotNull(message = "El venue es obligatorio")
        Long venueId,

        @Schema(description = "Ids de categorias asociadas", example = "[1,2]")
        List<Long> categoryIds
) {
}
