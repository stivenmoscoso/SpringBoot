package com.example.eventify.mapper;

import com.example.eventify.dto.VenueCreateDTO;
import com.example.eventify.dto.VenueResponseDTO;
import com.example.eventify.model.Venue;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    VenueResponseDTO toResponse(Venue venue);

    @Mapping(target = "id", ignore = true)
    Venue toEntity(VenueCreateDTO dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "direccion", source = "direccion")
    @Mapping(target = "capacidad", source = "capacidad")
    @Mapping(target = "ciudad", source = "ciudad")
    void updateEntity(VenueCreateDTO dto, @MappingTarget Venue venue);
}
