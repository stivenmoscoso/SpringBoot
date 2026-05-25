package com.example.eventify.dto;

import java.time.LocalDate;

public record EventSummaryDTO(
        String nombreEvento,
        LocalDate fecha,
        String nombreLugar,
        String ciudad
) {
}
