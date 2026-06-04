package com.example.eventify.mapper;

import com.example.eventify.dto.EventCreateDTO;
import com.example.eventify.dto.EventResponseDTO;
import com.example.eventify.exception.ResourceNotFoundException;
import com.example.eventify.model.Category;
import com.example.eventify.model.Event;
import com.example.eventify.model.Venue;
import com.example.eventify.repository.CategoryRepository;
import com.example.eventify.repository.VenueRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "venueName", source = "venue.nombre")
    @Mapping(target = "categoryNames", source = "categories")
    EventResponseDTO toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "venue", source = "venueId")
    @Mapping(target = "categories", source = "categoryIds")
    Event toEntity(EventCreateDTO dto, @Context VenueRepository venueRepository, @Context CategoryRepository categoryRepository);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "fecha", source = "fecha")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "venue", source = "venueId")
    @Mapping(target = "categories", source = "categoryIds")
    void updateEntity(EventCreateDTO dto, @MappingTarget Event event, @Context VenueRepository venueRepository, @Context CategoryRepository categoryRepository);

    default Venue map(Long venueId, @Context VenueRepository venueRepository) {
        if (venueId == null) {
            return null;
        }
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado"));
    }

    default Set<Category> map(List<Long> categoryIds, @Context CategoryRepository categoryRepository) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Set.of();
        }
        return categoryRepository.findAllById(categoryIds).stream().collect(Collectors.toSet());
    }

    default List<String> map(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }
        return categories.stream().map(Category::getName).toList();
    }
}
