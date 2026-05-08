package com.example.eventify.repository;


import com.example.eventify.model.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EventRepository {

    private final Map<Long, Event> events = new HashMap<>();
    private Long currentId = 1L;

    public List<Event> findAll() {
        return new ArrayList<>(events.values());
    }
    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(events.get(id));
    }
    public Event save(Event event) {
        if (event.getId() == null) {
            event.setId(currentId);
            currentId++;
        }
        events.put(event.getId(), event);
        return event;
    }
    public void deleteById(Long id) {
        events.remove(id);

    }
}

