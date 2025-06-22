package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.modelo.Pais;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaisMapper {
    PaisDTO toDTO(Pais pais);
    Pais toEntity(PaisDTO paisDTO);
}