package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.Pais;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonedaMapper {
    MonedaDTO toDTO(Moneda moneda);
    Moneda toEntity(MonedaDTO monedaDTO);

    default String map(Pais value) {
        return value != null ? value.getIdPais() : null;
    }

    default Pais map(String value) {
        if (value == null) return null;
        Pais pais = new Pais();
        pais.setIdPais(value);
        return pais;
    }
}
