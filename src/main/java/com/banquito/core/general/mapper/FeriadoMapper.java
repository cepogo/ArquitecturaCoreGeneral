package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.modelo.Feriado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeriadoMapper {
    FeriadoMapper INSTANCE = Mappers.getMapper(FeriadoMapper.class);

    @Mappings({
        @Mapping(source = "idPais.idPais", target = "idPais"),
        @Mapping(source = "idLocacion.idLocacion", target = "idLocacion")
    })
    FeriadoDTO toDTO(Feriado entity);

    @Mappings({
        @Mapping(target = "idPais.idPais", source = "idPais"),
        @Mapping(target = "idLocacion.idLocacion", source = "idLocacion")
    })
    Feriado toEntity(FeriadoDTO dto);
} 