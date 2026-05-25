package com.example.eventify.controller;

import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.model.Category;
import com.example.eventify.model.Event;
import com.example.eventify.model.Venue;
import com.example.eventify.service.CategoryService;
import com.example.eventify.service.EventService;
import com.example.eventify.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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
    private CategoryService categoryService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        venueService = mock(VenueService.class);
        categoryService = mock(CategoryService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminCatalogController(eventService, venueService, categoryService))
                .build();
    }

    @Test
    void showCatalogAddsEventsVenuesAndFormObjects() throws Exception {
        EventSummaryDTO event = new EventSummaryDTO("Mundial de futbol", LocalDate.of(2026, 6, 11), "Auditorio Central", "Bogota");
        Venue venue = new Venue(1L, "Auditorio Central", "Calle 123", 250);
        Category category = new Category(1L, "Deportes");
        SliceImpl<EventSummaryDTO> events = new SliceImpl<>(List.of(event), PageRequest.of(0, 12), false);
        when(eventService.findByFilters(isNull(), isNull(), isNull(), isNull(), isNull(), any(Pageable.class))).thenReturn(events);
        when(venueService.findAll()).thenReturn(List.of(venue));
        when(categoryService.findAll()).thenReturn(List.of(category));

        mockMvc.perform(get("/admin/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/catalog"))
                .andExpect(model().attribute("events", events))
                .andExpect(model().attribute("venues", List.of(venue)))
                .andExpect(model().attribute("categories", List.of(category)))
                .andExpect(model().attributeExists("event", "venue"));
    }

    @Test
    void createEventRedirectsToCatalog() throws Exception {
        Venue venue = new Venue(1L, "Auditorio Central", "Calle 123", 250);
        when(venueService.findById(1L)).thenReturn(venue);
        when(categoryService.findAllById(List.of(1L))).thenReturn(java.util.Set.of(new Category(1L, "Deportes")));

        mockMvc.perform(post("/admin/events")
                        .param("nombre", "Mundial de futbol")
                        .param("fecha", "2026-06-11")
                        .param("descripcion", "Canada, EEUU, Mexico")
                        .param("venueId", "1")
                        .param("categoryIds", "1"))
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
