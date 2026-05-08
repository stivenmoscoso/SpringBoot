package com.example.eventify.controller;

import com.example.eventify.model.Venue;
import com.example.eventify.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @GetMapping
    public List<Venue> findAll() {
        return venueService.findAll();
    }

    @GetMapping("/{id}")
    public Venue findById(@PathVariable Long id) {
        return venueService.findById(id);
    }

    @PostMapping
    public Venue create(@RequestBody Venue newVenue) {
        return venueService.create(newVenue);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        venueService.deleteById(id);
    }
}
