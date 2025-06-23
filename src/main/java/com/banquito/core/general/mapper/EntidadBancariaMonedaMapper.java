package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.EntidadBancariaMonedaDTO;
import com.banquito.core.general.modelo.EntidadBancariaMoneda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntidadBancariaMonedaMapper {

    @Mapping(source = "idEntidadBancaria.idEntidadBancaria", target = "idEntidadBancaria")
    EntidadBancariaMonedaDTO toDTO(EntidadBancariaMoneda entidadBancariaMoneda);
}
