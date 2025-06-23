package com.banquito.core.general.mapper;

import com.banquito.core.general.dto.SucursalDTO;
import com.banquito.core.general.modelo.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SucursalMapper {
    SucursalMapper INSTANCE = Mappers.getMapper(SucursalMapper.class);

    @Mappings({
        @Mapping(source = "idEntidadBancaria.idEntidadBancaria", target = "idEntidadBancaria"),
        @Mapping(source = "idLocacion.idLocacion", target = "idLocacion"),
        @Mapping(target = "estado", expression = "java(com.banquito.core.general.enums.EstadoSucursalesEnum.valueOf(entity.getEstado()))")
    })
    SucursalDTO toDTO(Sucursal entity);

    @Mappings({
        @Mapping(source = "idEntidadBancaria", target = "idEntidadBancaria.idEntidadBancaria"),
        @Mapping(source = "idLocacion", target = "idLocacion.idLocacion"),
        @Mapping(target = "estado", expression = "java(dto.getEstado().name())")
    })
    Sucursal toEntity(SucursalDTO dto);
} 