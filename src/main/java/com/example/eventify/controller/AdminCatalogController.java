package com.example.eventify.controller;

import com.example.eventify.dto.EventCreateDTO;
import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.dto.VenueCreateDTO;
import com.example.eventify.service.CategoryService;
import com.example.eventify.service.EventService;
import com.example.eventify.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminCatalogController {
    private final EventService eventService;
    private final VenueService venueService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home() {
        return "redirect:/admin/catalog";
    }

    @GetMapping("/admin/catalog")
    public String showCatalog(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "fecha"));
        Slice<EventSummaryDTO> events = eventService.findByFilters(city, category, capacity, startDate, endDate, pageRequest);
        model.addAttribute("events", events);
        model.addAttribute("venues", venueService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        if (!model.containsAttribute("event")) {
            model.addAttribute("event", new EventCreateDTO(null, null, null, null, null, null));
        }
        if (!model.containsAttribute("venue")) {
            model.addAttribute("venue", new VenueCreateDTO(null, null, null, null, null));
        }
        model.addAttribute("city", city);
        model.addAttribute("category", category);
        model.addAttribute("capacity", capacity);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("pageSize", pageRequest.getPageSize());
        return "admin/catalog";
    }

    @PostMapping("/admin/events")
    public String createEvent(
            @Valid @ModelAttribute("event") EventCreateDTO event,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", bindingResult);
            redirectAttributes.addFlashAttribute("event", event);
            redirectAttributes.addFlashAttribute("error", "Revisa los campos del evento");
            return "redirect:/admin/catalog";
        }
        eventService.create(event);
        redirectAttributes.addFlashAttribute("message", "Evento registrado correctamente");
        return "redirect:/admin/catalog";
    }

    @PostMapping("/admin/venues")
    public String createVenue(
            @Valid @ModelAttribute("venue") VenueCreateDTO venue,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.venue", bindingResult);
            redirectAttributes.addFlashAttribute("venue", venue);
            redirectAttributes.addFlashAttribute("error", "Revisa los campos del venue");
            return "redirect:/admin/catalog";
        }
        venueService.create(venue);
        redirectAttributes.addFlashAttribute("message", "Venue registrado correctamente");
        return "redirect:/admin/catalog";
    }
}
