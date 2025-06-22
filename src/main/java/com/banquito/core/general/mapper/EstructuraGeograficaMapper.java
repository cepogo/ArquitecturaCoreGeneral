package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.EstructuraGeograficaDTO;
import com.banquito.core.general.modelo.EstructuraGeografica;
import com.banquito.core.general.modelo.Pais;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EstructuraGeograficaMapper {
    EstructuraGeograficaMapper INSTANCE = Mappers.getMapper(EstructuraGeograficaMapper.class);

    @Mappings({
        @Mapping(source = "id.idPais", target = "idPais"),
        @Mapping(source = "id.codigoNivel", target = "codigoNivel")
    })
    EstructuraGeograficaDTO toDTO(EstructuraGeografica entity);

    @Mappings({
        @Mapping(target = "id.idPais", source = "idPais"),
        @Mapping(target = "id.codigoNivel", source = "codigoNivel")
    })
    EstructuraGeografica toEntity(EstructuraGeograficaDTO dto);

    // Métodos auxiliares para MapStruct
    default String paisToString(Pais pais) {
        return pais != null ? pais.getIdPais() : null;
    }

    default Pais stringToPais(String idPais) {
        if (idPais == null) return null;
        Pais pais = new Pais();
        pais.setIdPais(idPais);
        return pais;
    }
} 