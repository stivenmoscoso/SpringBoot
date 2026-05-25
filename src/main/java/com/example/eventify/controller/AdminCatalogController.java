package com.example.eventify.controller;

import com.example.eventify.model.Event;
import com.example.eventify.model.Venue;
import com.example.eventify.service.EventService;
import com.example.eventify.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminCatalogController {
    private final EventService eventService;
    private final VenueService venueService;

    @GetMapping("/")
    public String home() {
        return "redirect:/admin/catalog";
    }

    @GetMapping("/admin/catalog")
    public String showCatalog(Model model) {
        model.addAttribute("events", eventService.findAll());
        model.addAttribute("venues", venueService.findAll());
        model.addAttribute("event", new Event());
        model.addAttribute("venue", new Venue());
        return "admin/catalog";
    }

    @PostMapping("/admin/events")
    public String createEvent(@ModelAttribute("event") Event event, RedirectAttributes redirectAttributes) {
        eventService.create(event);
        redirectAttributes.addFlashAttribute("message", "Evento registrado correctamente");
        return "redirect:/admin/catalog";
    }

    @PostMapping("/admin/venues")
    public String createVenue(@ModelAttribute("venue") Venue venue, RedirectAttributes redirectAttributes) {
        venueService.create(venue);
        redirectAttributes.addFlashAttribute("message", "Venue registrado correctamente");
        return "redirect:/admin/catalog";
    }
}
