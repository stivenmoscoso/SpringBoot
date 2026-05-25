package com.example.eventify.controller;

import com.example.eventify.model.Event;
import com.example.eventify.model.Venue;
import com.example.eventify.service.EventService;
import com.example.eventify.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminCatalogControllerTest {
    private EventService eventService;
    private VenueService venueService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        venueService = mock(VenueService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminCatalogController(eventService, venueService))
                .build();
    }

    @Test
    void showCatalogAddsEventsVenuesAndFormObjects() throws Exception {
        Event event = new Event(1L, "Mundial de futbol", LocalDate.of(2026, 6, 11), "Canada, EEUU, Mexico");
        Venue venue = new Venue(1L, "Auditorio Central", "Calle 123", 250);
        when(eventService.findAll()).thenReturn(List.of(event));
        when(venueService.findAll()).thenReturn(List.of(venue));

        mockMvc.perform(get("/admin/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/catalog"))
                .andExpect(model().attribute("events", List.of(event)))
                .andExpect(model().attribute("venues", List.of(venue)))
                .andExpect(model().attributeExists("event", "venue"));
    }

    @Test
    void createEventRedirectsToCatalog() throws Exception {
        mockMvc.perform(post("/admin/events")
                        .param("nombre", "Mundial de futbol")
                        .param("fecha", "2026-06-11")
                        .param("descripcion", "Canada, EEUU, Mexico"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/catalog"));

        verify(eventService).create(any(Event.class));
    }

    @Test
    void createVenueRedirectsToCatalog() throws Exception {
        mockMvc.perform(post("/admin/venues")
                        .param("nombre", "Auditorio Central")
                        .param("direccion", "Calle 123")
                        .param("capacidad", "250"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/catalog"));

        verify(venueService).create(any(Venue.class));
    }
}
