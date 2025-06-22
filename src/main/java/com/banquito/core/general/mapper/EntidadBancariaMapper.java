package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.EntidadBancariaDTO;
import com.banquito.core.general.modelo.EntidadBancaria;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntidadBancariaMapper {
    EntidadBancariaDTO toDTO(EntidadBancaria entidadBancaria);
    EntidadBancaria toEntity(EntidadBancariaDTO entidadBancariaDTO);
}
