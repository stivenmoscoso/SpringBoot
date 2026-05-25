package com.example.eventify.repository;

import com.example.eventify.dto.EventSummaryDTO;
import com.example.eventify.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @EntityGraph(attributePaths = {"venue", "categories"})
    @Query("select event from Event event where coalesce(event.deleted, false) = false")
    List<Event> findAll();

    @EntityGraph(attributePaths = {"venue", "categories"})
    @Query("select event from Event event where coalesce(event.deleted, false) = false and event.nombre = :nombre")
    List<Event> findByNombre(@Param("nombre") String nombre);

    @EntityGraph(attributePaths = {"venue", "categories"})
    @Query("select event from Event event where coalesce(event.deleted, false) = false and event.id = :id")
    Optional<Event> findById(@Param("id") Long id);

    @Query("""
            select new com.example.eventify.dto.EventSummaryDTO(
                event.nombre,
                event.fecha,
                venue.nombre,
                venue.ciudad
            )
            from Event event
            left join event.venue venue
            where coalesce(event.deleted, false) = false
            """)
    Slice<EventSummaryDTO> findSummaries(Pageable pageable);

    @Query("""
            select new com.example.eventify.dto.EventSummaryDTO(event.nombre, event.fecha, venue.nombre, venue.ciudad)
            from Event event
            left join event.venue venue
            where coalesce(event.deleted, false) = false
              and lower(venue.ciudad) = lower(:city)
            """)
    Slice<EventSummaryDTO> findByCity(@Param("city") String city, Pageable pageable);

    @Query("""
            select new com.example.eventify.dto.EventSummaryDTO(event.nombre, event.fecha, venue.nombre, venue.ciudad)
            from Event event
            left join event.venue venue
            where coalesce(event.deleted, false) = false
              and venue.capacidad >= :capacity
            """)
    Slice<EventSummaryDTO> findByVenueCapacityGreaterThanEqual(@Param("capacity") Integer capacity, Pageable pageable);

    @Query("""
            select new com.example.eventify.dto.EventSummaryDTO(event.nombre, event.fecha, venue.nombre, venue.ciudad)
            from Event event
            left join event.venue venue
            where coalesce(event.deleted, false) = false
              and event.fecha between :startDate and :endDate
            """)
    Slice<EventSummaryDTO> findByDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("""
            select new com.example.eventify.dto.EventSummaryDTO(event.nombre, event.fecha, venue.nombre, venue.ciudad)
            from Event event
            left join event.venue venue
            where coalesce(event.deleted, false) = false
              and exists (
                  select category.id
                  from event.categories category
                  where lower(category.nombre) = lower(:category)
              )
            """)
    Slice<EventSummaryDTO> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("""
            select new com.example.eventify.dto.EventSummaryDTO(event.nombre, event.fecha, venue.nombre, venue.ciudad)
            from Event event
            left join event.venue venue
            where coalesce(event.deleted, false) = false
              and (:city is null or lower(venue.ciudad) = lower(:city))
              and (:category is null or exists (
                  select categoryFilter.id
                  from event.categories categoryFilter
                  where lower(categoryFilter.nombre) = lower(:category)
              ))
              and (:capacity is null or venue.capacidad >= :capacity)
              and (:startDate is null or event.fecha >= :startDate)
              and (:endDate is null or event.fecha <= :endDate)
            """)
    Slice<EventSummaryDTO> findByFilters(
            @Param("city") String city,
            @Param("category") String category,
            @Param("capacity") Integer capacity,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
