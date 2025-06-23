package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.EstructuraGeograficaDTO;
import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.dto.EstructuraGeograficaCreacionDTO;
import com.banquito.core.general.dto.EstructuraGeograficaUpdateDTO;
import com.banquito.core.general.dto.LocacionGeograficaCreacionDTO;
import com.banquito.core.general.dto.LocacionGeograficaUpdateDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import com.banquito.core.general.mapper.EstructuraGeograficaMapper;
import com.banquito.core.general.mapper.LocacionGeograficaMapper;
import com.banquito.core.general.modelo.EstructuraGeografica;
import com.banquito.core.general.modelo.EstructuraGeograficaId;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.repositorio.EstructuraGeograficaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.repositorio.PaisRepositorio;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EliminarEntidadException;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstructuraLocacionGeograficaServicio {
    private final EstructuraGeograficaRepositorio estructuraGeograficaRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final EstructuraGeograficaMapper estructuraGeograficaMapper;
    private final LocacionGeograficaMapper locacionGeograficaMapper;
    private final PaisRepositorio paisRepositorio;

    // Crear Estructura Geográfica
    @Transactional
    public EstructuraGeograficaDTO crearEstructuraGeografica(EstructuraGeograficaCreacionDTO dto) {
        try {
            Pais pais = this.paisRepositorio.findById(dto.getIdPais())
                    .orElseThrow(() -> new EntidadNoEncontradaException("El país con id " + dto.getIdPais() + " no existe.", 2, "Pais"));

            EstructuraGeografica entity = new EstructuraGeografica();
            EstructuraGeograficaId id = new EstructuraGeograficaId();
            id.setIdPais(dto.getIdPais());
            id.setCodigoNivel(java.math.BigDecimal.valueOf(dto.getCodigoNivel()));
            entity.setId(id);

            entity.setIdPais(pais); // Asignar la entidad Pais

            entity.setNombre(dto.getNombre());
            entity.setEstado(EstadoGeneralEnum.ACTIVO);
            entity.setVersion(1L); // Versión inicial
            return estructuraGeograficaMapper.toDTO(this.estructuraGeograficaRepositorio.save(entity));
        } catch (EntidadNoEncontradaException e) {
            throw e;
        } catch (Exception e) {
            throw new CrearEntidadException("EstructuraGeografica", "Error al crear la estructura geográfica: " + e.getMessage());
        }
    }

    // Crear Locación Geográfica
    @Transactional
    public LocacionGeograficaDTO crearLocacionGeografica(LocacionGeograficaCreacionDTO dto) {
        try {
            log.info("Creando nueva locación geográfica: {}", dto.getNombre());
            
            // Verificar que la estructura geográfica existe
            EstructuraGeograficaId estructuraId = new EstructuraGeograficaId();
            estructuraId.setIdPais(dto.getIdPais());
            estructuraId.setCodigoNivel(java.math.BigDecimal.valueOf(dto.getCodigoNivel()));
            
            EstructuraGeografica estructura = estructuraGeograficaRepositorio.findById(estructuraId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Estructura geográfica no encontrada para país: " + dto.getIdPais() + " y nivel: " + dto.getCodigoNivel(), 2, "EstructuraGeografica"));
            
            // Crear la locación geográfica
            LocacionGeografica locacion = new LocacionGeografica();
            locacion.setNombre(dto.getNombre());
            locacion.setCodigoTelefonoArea(dto.getCodigoTelefonoArea());
            locacion.setCodigoGeografico(dto.getCodigoGeografico());
            locacion.setCodigoPostal(dto.getCodigoPostal());
            locacion.setEstructuraGeografica(estructura);
            locacion.setEstado(EstadoLocacionesGeograficasEnum.ACTIVO);
            locacion.setVersion(1L);
            
            // Lógica automática para asignar la locación padre según el código de nivel
            java.math.BigDecimal codigoNivel = java.math.BigDecimal.valueOf(dto.getCodigoNivel());
            if (codigoNivel.equals(java.math.BigDecimal.ONE)) {
                // Nivel 1: no tiene padre
                locacion.setIdLocacionPadre(null);
            } else if (codigoNivel.equals(java.math.BigDecimal.valueOf(2))) {
                // Nivel 2: buscar la locación padre específica o la primera del país
                Optional<LocacionGeografica> locacionPadre;
                
                if (dto.getIdProvinciaPadre() != null) {
                    // Buscar la provincia específica proporcionada
                    locacionPadre = locacionGeograficaRepositorio.findById(dto.getIdProvinciaPadre())
                        .filter(loc -> loc.getEstructuraGeografica().getId().getIdPais().equals(dto.getIdPais()) &&
                                      loc.getEstructuraGeografica().getId().getCodigoNivel().equals(java.math.BigDecimal.ONE) &&
                                      loc.getEstado() == EstadoLocacionesGeograficasEnum.ACTIVO);
                    
                    if (locacionPadre.isEmpty()) {
                        throw new CrearEntidadException("LocacionGeografica", 
                            "No se encontró la provincia con ID " + dto.getIdProvinciaPadre() + 
                            " para el país " + dto.getIdPais() + " o no está activa");
                    }
                } else {
                    // Buscar la primera provincia del país (comportamiento anterior)
                    locacionPadre = locacionGeograficaRepositorio
                        .findFirstByEstructuraGeografica_Id_IdPaisAndEstructuraGeografica_Id_CodigoNivelAndEstado(
                            dto.getIdPais(), 
                            java.math.BigDecimal.ONE, 
                            EstadoLocacionesGeograficasEnum.ACTIVO
                        );
                    
                    if (locacionPadre.isEmpty()) {
                        throw new CrearEntidadException("LocacionGeografica", 
                            "No se encontró una locación padre de nivel 1 para el país: " + dto.getIdPais() + 
                            ". Debe crear primero una provincia o especificar idProvinciaPadre");
                    }
                }
                
                locacion.setIdLocacionPadre(locacionPadre.get());
            } else {
                throw new CrearEntidadException("LocacionGeografica", "Código de nivel no soportado: " + dto.getCodigoNivel() + ". Solo se soportan niveles 1 y 2.");
            }
            
            LocacionGeografica locacionGuardada = locacionGeograficaRepositorio.save(locacion);
            log.info("Locación geográfica creada exitosamente con ID: {}", locacionGuardada.getIdLocacion());
            
            return locacionGeograficaMapper.toDTO(locacionGuardada);
        } catch (EntidadNoEncontradaException | CrearEntidadException e) {
            log.error("Error al crear locación geográfica: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear locación geográfica", e);
            throw new CrearEntidadException("LocacionGeografica", "Error al crear locación geográfica: " + e.getMessage());
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
    public EstructuraGeograficaDTO modificarEstructuraGeografica(String idPais, Integer codigoNivel, EstructuraGeograficaUpdateDTO dto) {
        EstructuraGeograficaId id = new EstructuraGeograficaId();
        id.setIdPais(idPais);
        id.setCodigoNivel(java.math.BigDecimal.valueOf(codigoNivel));
        EstructuraGeografica entity = estructuraGeograficaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Estructura geográfica no encontrada", 2, "EstructuraGeografica"));
        try {
            // Actualización parcial
            if (dto.getNombre() != null) {
                entity.setNombre(dto.getNombre());
            }
            // Incrementar versión
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return estructuraGeograficaMapper.toDTO(estructuraGeograficaRepositorio.save(entity));
        } catch (Exception e) {
            throw new ActualizarEntidadException("EstructuraGeografica", "Error al modificar la estructura geográfica: " + e.getMessage());
        }
    }

    // Modificar Locación Geográfica
    @Transactional
    public LocacionGeograficaDTO modificarLocacionGeografica(Integer idLocacion, LocacionGeograficaUpdateDTO dto) {
        LocacionGeografica entity = this.locacionGeograficaRepositorio.findById(idLocacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("Locación geográfica no encontrada", 2, "LocacionGeografica"));
        try {
            // Actualización parcial
            if (dto.getIdLocacionPadre() != null) {
                LocacionGeografica padre = this.locacionGeograficaRepositorio.findById(dto.getIdLocacionPadre()).orElse(null);
                entity.setIdLocacionPadre(padre);
            }
            if (dto.getIdPais() != null && dto.getCodigoNivel() != null) {
                // Cambiar la estructura geográfica asociada
                EstructuraGeograficaId estructuraId = new EstructuraGeograficaId();
                estructuraId.setIdPais(dto.getIdPais());
                estructuraId.setCodigoNivel(java.math.BigDecimal.valueOf(dto.getCodigoNivel()));
                EstructuraGeografica estructura = this.estructuraGeograficaRepositorio.findById(estructuraId)
                        .orElseThrow(() -> new EntidadNoEncontradaException("La estructura geográfica no existe.", 2, "EstructuraGeografica"));
                entity.setEstructuraGeografica(estructura);
            }
            if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
            if (dto.getCodigoTelefonoArea() != null) entity.setCodigoTelefonoArea(dto.getCodigoTelefonoArea());
            if (dto.getCodigoGeografico() != null) entity.setCodigoGeografico(dto.getCodigoGeografico());
            if (dto.getCodigoPostal() != null) entity.setCodigoPostal(dto.getCodigoPostal());
            
            // Incrementar versión
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return locacionGeograficaMapper.toDTO(this.locacionGeograficaRepositorio.save(entity));
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

    // Listar todas las provincias de un país
    public List<LocacionGeograficaDTO> listarProvincias(String idPais) {
        List<LocacionGeografica> provincias = locacionGeograficaRepositorio.findAll()
                .stream()
                .filter(l -> l.getEstructuraGeografica() != null
                        && l.getEstructuraGeografica().getId() != null
                        && idPais.equals(l.getEstructuraGeografica().getId().getIdPais())
                        && l.getEstructuraGeografica().getId().getCodigoNivel().equals(java.math.BigDecimal.ONE)
                        && l.getEstado() == EstadoLocacionesGeograficasEnum.ACTIVO)
                .collect(Collectors.toList());
        return provincias.stream().map(locacionGeograficaMapper::toDTO).collect(Collectors.toList());
    }

    // Listar cantones de una provincia específica
    public List<LocacionGeograficaDTO> listarCantonesPorProvincia(String idPais, String nombreProvincia) {
        // Primero verificar que la provincia existe
        Optional<LocacionGeografica> provincia = locacionGeograficaRepositorio
            .findFirstByNombreAndEstructuraGeografica_Id_IdPaisAndEstructuraGeografica_Id_CodigoNivelAndEstado(
                nombreProvincia, idPais, java.math.BigDecimal.ONE, EstadoLocacionesGeograficasEnum.ACTIVO
            );
        
        if (provincia.isEmpty()) {
            throw new EntidadNoEncontradaException("Provincia '" + nombreProvincia + "' no encontrada para el país " + idPais, 2, "LocacionGeografica");
        }
        
        // Buscar cantones que tengan como padre a la provincia encontrada
        List<LocacionGeografica> cantones = locacionGeograficaRepositorio.findAll()
                .stream()
                .filter(l -> l.getEstructuraGeografica() != null
                        && l.getEstructuraGeografica().getId() != null
                        && idPais.equals(l.getEstructuraGeografica().getId().getIdPais())
                        && l.getEstructuraGeografica().getId().getCodigoNivel().equals(java.math.BigDecimal.valueOf(2))
                        && l.getIdLocacionPadre() != null
                        && provincia.get().getIdLocacion().equals(l.getIdLocacionPadre().getIdLocacion())
                        && l.getEstado() == EstadoLocacionesGeograficasEnum.ACTIVO)
                .collect(Collectors.toList());
        
        return cantones.stream().map(locacionGeograficaMapper::toDTO).collect(Collectors.toList());
    }

    // Cambiar estado de Estructura Geográfica
    @Transactional
    public EstructuraGeograficaDTO cambiarEstadoEstructuraGeografica(String idPais, Integer codigoNivel, EstadoGeneralEnum nuevoEstado) {
        EstructuraGeograficaId id = new EstructuraGeograficaId();
        id.setIdPais(idPais);
        id.setCodigoNivel(java.math.BigDecimal.valueOf(codigoNivel));
        EstructuraGeografica entity = this.estructuraGeograficaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Estructura geográfica no encontrada", 2, "EstructuraGeografica"));
        try {
            entity.setEstado(nuevoEstado);
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return this.estructuraGeograficaMapper.toDTO(this.estructuraGeograficaRepositorio.save(entity));
        } catch (Exception e) {
            throw new ActualizarEntidadException("EstructuraGeografica", "Error al cambiar el estado de la estructura geográfica: " + e.getMessage());
        }
    }
} 