package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.modelo.LocacionGeografica;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocacionGeograficaMapper {
    LocacionGeograficaMapper INSTANCE = Mappers.getMapper(LocacionGeograficaMapper.class);

    @Mappings({
        @Mapping(source = "estructuraGeografica.id.idPais", target = "idPais"),
        @Mapping(source = "estructuraGeografica.id.codigoNivel", target = "codigoNivel"),
        @Mapping(source = "idLocacionPadre.idLocacion", target = "idLocacionPadre", ignore = true)
    })
    LocacionGeograficaDTO toDTO(LocacionGeografica entity);

    @Mappings({
        @Mapping(target = "estructuraGeografica.id.idPais", source = "idPais"),
        @Mapping(target = "estructuraGeografica.id.codigoNivel", source = "codigoNivel"),
        @Mapping(target = "idLocacionPadre.idLocacion", source = "idLocacionPadre", ignore = true)
    })
    LocacionGeografica toEntity(LocacionGeograficaDTO dto);
} 