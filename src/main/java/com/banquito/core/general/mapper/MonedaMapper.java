package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.modelo.Moneda;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonedaMapper {
    MonedaDTO toDTO(Moneda moneda);
    Moneda toEntity(MonedaDTO monedaDTO);
}
