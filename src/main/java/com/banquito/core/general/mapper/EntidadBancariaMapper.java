package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.EntidadBancariaDTO;
import com.banquito.core.general.modelo.EntidadBancaria;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntidadBancariaMapper {
    EntidadBancariaDTO toDTO(EntidadBancaria entidadBancaria);
    EntidadBancaria toEntity(EntidadBancariaDTO entidadBancariaDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(EntidadBancariaDTO dto, @MappingTarget EntidadBancaria entity);
}
