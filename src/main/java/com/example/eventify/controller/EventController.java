package com.example.eventify.controller;


import com.example.eventify.model.Event;
import com.example.eventify.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping
    public List<Event> findAll() {
        return eventService.findAll();
    }
    @GetMapping("/{id}")
    public Event findById(@PathVariable Long id) {
        return eventService.findById(id);
    }
    @PostMapping
    public Event create(@RequestBody Event newEvent) {
        return eventService.create(newEvent);
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        eventService.deleteById(id);
    }
}
