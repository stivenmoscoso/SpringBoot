package com.example.eventify.repository;

import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @EntityGraph(attributePaths = {"venue", "categories"})
    List<Event> findAll();

    @EntityGraph(attributePaths = {"venue", "categories"})
    List<Event> findByNombre(String nombre);

    @EntityGraph(attributePaths = {"venue", "categories"})
    Optional<Event> findById(Long id);

    @Query("""
            select new com.example.eventify.dto.EventSummaryDTO(
                event.nombre,
                event.fecha,
                venue.nombre,
                venue.ciudad
            )
            from Event event
            left join event.venue venue
            """)
    Slice<EventSummaryDTO> findSummaries(Pageable pageable);
}
