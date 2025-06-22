package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.EstructuraGeograficaDTO;
import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import com.banquito.core.general.mapper.EstructuraGeograficaMapper;
import com.banquito.core.general.mapper.LocacionGeograficaMapper;
import com.banquito.core.general.modelo.EstructuraGeografica;
import com.banquito.core.general.modelo.EstructuraGeograficaId;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.repositorio.EstructuraGeograficaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EliminarEntidadException;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstructuraLocacionGeograficaServicio {
    @Autowired
    private EstructuraGeograficaRepositorio estructuraGeograficaRepositorio;
    @Autowired
    private LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    @Autowired
    private EstructuraGeograficaMapper estructuraGeograficaMapper;
    @Autowired
    private LocacionGeograficaMapper locacionGeograficaMapper;

    // Crear Estructura Geográfica
    @Transactional
    public EstructuraGeograficaDTO crearEstructuraGeografica(EstructuraGeograficaDTO dto) {
        try {
            EstructuraGeografica entity = estructuraGeograficaMapper.toEntity(dto);
            entity.setEstado(EstadoGeneralEnum.ACTIVO);
            entity.setVersion(1L); // Versión inicial
            return estructuraGeograficaMapper.toDTO(estructuraGeograficaRepositorio.save(entity));
        } catch (Exception e) {
            throw new CrearEntidadException("EstructuraGeografica", "Error al crear la estructura geográfica: " + e.getMessage());
        }
    }

    // Crear Locación Geográfica
    @Transactional
    public LocacionGeograficaDTO crearLocacionGeografica(LocacionGeograficaDTO dto) {
        try {
            LocacionGeografica entity = locacionGeograficaMapper.toEntity(dto);
            entity.setEstado(EstadoLocacionesGeograficasEnum.ACTIVO);
            entity.setVersion(1L); // Versión inicial
            return locacionGeograficaMapper.toDTO(locacionGeograficaRepositorio.save(entity));
        } catch (Exception e) {
            throw new CrearEntidadException("LocacionGeografica", "Error al crear la locación geográfica: " + e.getMessage());
        }
    }

    // Eliminación lógica Estructura Geográfica
    @Transactional
    public void eliminarLogicoEstructuraGeografica(String idPais, Integer codigoNivel) {
        EstructuraGeograficaId id = new EstructuraGeograficaId();
        id.setIdPais(idPais);
        id.setCodigoNivel(java.math.BigDecimal.valueOf(codigoNivel));
        EstructuraGeografica entity = estructuraGeograficaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Estructura geográfica no encontrada", 2, "EstructuraGeografica"));
        try {
            entity.setEstado(EstadoGeneralEnum.INACTIVO);
            estructuraGeograficaRepositorio.save(entity);
        } catch (Exception e) {
            throw new EliminarEntidadException("EstructuraGeografica", "Error al eliminar lógicamente la estructura geográfica: " + e.getMessage());
        }
    }

    // Eliminación lógica Locación Geográfica
    @Transactional
    public void eliminarLogicoLocacionGeografica(Integer idLocacion) {
        LocacionGeografica entity = locacionGeograficaRepositorio.findById(idLocacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("Locación geográfica no encontrada", 2, "LocacionGeografica"));
        try {
            entity.setEstado(EstadoLocacionesGeograficasEnum.INACTIVO);
            locacionGeograficaRepositorio.save(entity);
        } catch (Exception e) {
            throw new EliminarEntidadException("LocacionGeografica", "Error al eliminar lógicamente la locación geográfica: " + e.getMessage());
        }
    }

    // Modificar Estructura Geográfica
    @Transactional
    public EstructuraGeograficaDTO modificarEstructuraGeografica(EstructuraGeograficaDTO dto) {
        EstructuraGeograficaId id = new EstructuraGeograficaId();
        id.setIdPais(dto.getIdPais());
        id.setCodigoNivel(java.math.BigDecimal.valueOf(dto.getCodigoNivel()));
        EstructuraGeografica entity = estructuraGeograficaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Estructura geográfica no encontrada", 2, "EstructuraGeografica"));
        try {
            // Actualización parcial
            if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
            if (dto.getEstado() != null) entity.setEstado(dto.getEstado());
            // idPais y codigoNivel no se actualizan porque son parte de la clave primaria
            // Incrementar versión
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return estructuraGeograficaMapper.toDTO(estructuraGeograficaRepositorio.save(entity));
        } catch (Exception e) {
            throw new ActualizarEntidadException("EstructuraGeografica", "Error al modificar la estructura geográfica: " + e.getMessage());
        }
    }

    // Modificar Locación Geográfica
    @Transactional
    public LocacionGeograficaDTO modificarLocacionGeografica(LocacionGeograficaDTO dto) {
        LocacionGeografica entity = locacionGeograficaRepositorio.findById(dto.getIdLocacion())
                .orElseThrow(() -> new EntidadNoEncontradaException("Locación geográfica no encontrada", 2, "LocacionGeografica"));
        try {
            // Actualización parcial
            if (dto.getIdLocacionPadre() != null) {
                LocacionGeografica padre = locacionGeograficaRepositorio.findById(dto.getIdLocacionPadre()).orElse(null);
                entity.setIdLocacionPadre(padre);
            }
            if (dto.getIdPais() != null && dto.getCodigoNivel() != null) {
                // Cambiar la estructura geográfica asociada
                EstructuraGeograficaId estructuraId = new EstructuraGeograficaId();
                estructuraId.setIdPais(dto.getIdPais());
                estructuraId.setCodigoNivel(java.math.BigDecimal.valueOf(dto.getCodigoNivel()));
                EstructuraGeografica estructura = estructuraGeograficaRepositorio.findById(estructuraId).orElse(null);
                entity.setEstructuraGeografica(estructura);
            }
            if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
            if (dto.getCodigoTelefonoArea() != null) entity.setCodigoTelefonoArea(dto.getCodigoTelefonoArea());
            if (dto.getCodigoGeografico() != null) entity.setCodigoGeografico(dto.getCodigoGeografico());
            if (dto.getCodigoPostal() != null) entity.setCodigoPostal(dto.getCodigoPostal());
            if (dto.getEstado() != null) entity.setEstado(dto.getEstado());
            // Incrementar versión
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return locacionGeograficaMapper.toDTO(locacionGeograficaRepositorio.save(entity));
        } catch (Exception e) {
            throw new ActualizarEntidadException("LocacionGeografica", "Error al modificar la locación geográfica: " + e.getMessage());
        }
    }

    // Listar locaciones geográficas activas por nivel
    public List<LocacionGeograficaDTO> listarLocacionesActivasPorNivel(String idPais, Integer codigoNivel) {
        List<LocacionGeografica> locaciones = locacionGeograficaRepositorio.findAll()
                .stream()
                .filter(l -> l.getEstructuraGeografica() != null
                        && l.getEstructuraGeografica().getId() != null
                        && idPais.equals(l.getEstructuraGeografica().getId().getIdPais())
                        && l.getEstructuraGeografica().getId().getCodigoNivel().intValue() == codigoNivel
                        && l.getEstado() == EstadoLocacionesGeograficasEnum.ACTIVO)
                .collect(Collectors.toList());
        return locaciones.stream().map(locacionGeograficaMapper::toDTO).collect(Collectors.toList());
    }
} 