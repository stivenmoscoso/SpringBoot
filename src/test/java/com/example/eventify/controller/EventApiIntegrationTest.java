package com.example.eventify.controller;

import com.example.eventify.model.Event;
import com.example.eventify.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:eventify-api-test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureMockMvc
class EventApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void missingEventReturnsNotFoundForReadUpdateAndDelete() throws Exception {
        Event updateRequest = new Event(null, "Evento inexistente", LocalDate.of(2026, 6, 11), "No existe");

        mockMvc.perform(get("/api/events/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Evento no encontrado"));

        mockMvc.perform(put("/api/events/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Evento no encontrado"));

        mockMvc.perform(delete("/api/events/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Evento no encontrado"));
    }

    @Test
    void pageZeroWithSizeFiveReturnsFiveEventsAndPaginationMetadata() throws Exception {
        List<Event> events = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            events.add(new Event(null, "Evento %03d".formatted(i), LocalDate.of(2026, 6, 11), "Descripcion " + i));
        }
        eventRepository.saveAll(events);

        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "nombre,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.content[0].nombreEvento").value("Evento 001"))
                .andExpect(jsonPath("$.totalPages").doesNotExist())
                .andExpect(jsonPath("$.totalElements").doesNotExist())
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false));
    }

    @Test
    void deleteExistingEventReturnsNoContentAndRemovesResource() throws Exception {
        Event event = eventRepository.save(new Event(null, "Evento cancelado", LocalDate.of(2026, 6, 11), "No se realizara"));

        mockMvc.perform(delete("/api/events/{id}", event.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Evento no encontrado"));
    }
}
