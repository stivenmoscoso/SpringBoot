package com.example.eventify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Resumen plano de evento para listados masivos. Evita serializar entidades JPA pesadas.")
public record EventSummaryDTO(
        @Schema(description = "Nombre del evento", example = "Mundial de futbol")
        String nombreEvento,
        @Schema(description = "Fecha de realizacion del evento", example = "2026-06-11")
        LocalDate fecha,
        @Schema(description = "Nombre del lugar asociado", example = "Auditorio Central")
        String nombreLugar,
        @Schema(description = "Ciudad del lugar asociado", example = "Bogota")
        String ciudad
) {
}
