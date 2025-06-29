package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.dto.FeriadoUpdateDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.FeriadoMapper;
import com.banquito.core.general.modelo.Feriado;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.repositorio.FeriadoRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.repositorio.PaisRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeriadoServicio {
    
    private final FeriadoRepositorio feriadoRepositorio;
    private final PaisRepositorio paisRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final FeriadoMapper feriadoMapper;

    // Crear feriado por país o locación geográfica
    @Transactional
    public FeriadoDTO crearFeriado(FeriadoCreacionDTO dto) {
        log.info("Iniciando creación de feriado: {} - Tipo: {}", dto.getNombre(), dto.getTipo());
        try {
            // Verificar que el país existe
            Pais pais = paisRepositorio.findById(dto.getIdPais())
                .orElseThrow(() -> new EntidadNoEncontradaException("País no encontrado: " + dto.getIdPais(), 2, "Pais"));
            
            // Crear la entidad feriado
            Feriado feriado = new Feriado();
            feriado.setFecha(dto.getFecha());
            feriado.setNombre(dto.getNombre());
            feriado.setTipo(dto.getTipo());
            feriado.setIdPais(pais);
            feriado.setEstado(EstadoGeneralEnum.ACTIVO);
            feriado.setVersion(1L);
            
            // Lógica para asignar locación según el tipo
            if (TipoFeriadosEnum.NACIONAL.equals(dto.getTipo())) {
                log.debug("Asignando feriado como NACIONAL para el país {}", dto.getIdPais());
                feriado.setIdLocacion(null);
            } else if (TipoFeriadosEnum.LOCAL.equals(dto.getTipo())) {
                log.debug("Asignando feriado como LOCAL para la locación {}", dto.getIdLocacion());
                if (dto.getIdLocacion() == null) {
                    throw new CrearEntidadException("Feriado", "Para feriados locales debe especificar idLocacion");
                }
                
                // Verificar que la locación existe y pertenece al país
                LocacionGeografica locacion = locacionGeograficaRepositorio.findById(dto.getIdLocacion())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Locación no encontrada: " + dto.getIdLocacion(), 2, "LocacionGeografica"));
                
                if (!dto.getIdPais().equals(locacion.getEstructuraGeografica().getId().getIdPais())) {
                    throw new CrearEntidadException("Feriado", "La locación no pertenece al país especificado");
                }
                
                feriado.setIdLocacion(locacion);
            }
            
            Feriado feriadoGuardado = feriadoRepositorio.save(feriado);
            log.info("Feriado {} creado exitosamente con ID: {}", feriadoGuardado.getNombre(), feriadoGuardado.getIdFeriado());
            
            return feriadoMapper.toDTO(feriadoGuardado);
        } catch (EntidadNoEncontradaException | CrearEntidadException e) {
            log.error("Error de validación al crear feriado {}: {}", dto.getNombre(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear feriado {}: {}", dto.getNombre(), e.getMessage(), e);
            throw new CrearEntidadException("Feriado", "Error al crear el feriado: " + e.getMessage());
        }
    }

    // Modificar solo fecha y nombre
    @Transactional
    public FeriadoDTO modificarFeriado(Integer idFeriado, FeriadoUpdateDTO dto) {
        log.info("Iniciando modificación del feriado con ID: {}", idFeriado);
        Feriado entity = feriadoRepositorio.findById(idFeriado)
                .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado", 2, "Feriado"));
        try {
            entity.setFecha(dto.getFecha());
            entity.setNombre(dto.getNombre());
            
            // Incrementar versión
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            
            Feriado feriadoActualizado = feriadoRepositorio.save(entity);
            log.info("Feriado con ID {} modificado exitosamente.", idFeriado);
            
            return feriadoMapper.toDTO(feriadoActualizado);
        } catch (Exception e) {
            log.error("Error al modificar feriado con ID {}: {}", idFeriado, e.getMessage(), e);
            throw new ActualizarEntidadException("Feriado", "Error al modificar el feriado: " + e.getMessage());
        }
    }

    // Cambiar estado de feriado (ACTIVO/INACTIVO)
    @Transactional
    public void cambiarEstadoFeriado(Integer idFeriado, EstadoGeneralEnum nuevoEstado) {
        log.info("Cambiando estado del feriado con ID: {} a {}", idFeriado, nuevoEstado);
        Feriado entity = feriadoRepositorio.findById(idFeriado)
                .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado", 2, "Feriado"));
        try {
            // Solo validar para cambio a ACTIVO
            if (nuevoEstado == EstadoGeneralEnum.ACTIVO) {
                if (entity.getIdPais() == null) {
                    log.warn("No se puede activar el feriado porque no tiene país asociado.");
                    throw new ActualizarEntidadException("Feriado", "No se puede activar el feriado porque no tiene país asociado.");
                }
                // Verificar que el país esté ACTIVO
                if (entity.getIdPais().getEstado() != EstadoGeneralEnum.ACTIVO) {
                    log.warn("No se puede activar el feriado porque el país '{}' está inactivo.", entity.getIdPais().getNombre());
                    throw new ActualizarEntidadException("Feriado", "No se puede activar el feriado porque el país '" + entity.getIdPais().getNombre() + "' está inactivo.");
                }
                // Si tiene locación asociada, verificar que esté ACTIVA
                if (entity.getIdLocacion() != null && entity.getIdLocacion().getEstado() != com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum.ACTIVO) {
                    log.warn("No se puede activar el feriado porque la locación geográfica '{}' está inactiva.", entity.getIdLocacion().getNombre());
                    throw new ActualizarEntidadException("Feriado", "No se puede activar el feriado porque la locación geográfica '" + entity.getIdLocacion().getNombre() + "' está inactiva.");
                }
            }
            entity.setEstado(nuevoEstado);
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            feriadoRepositorio.save(entity);
            log.info("Estado del feriado con ID {} cambiado a {}.", idFeriado, nuevoEstado);
        } catch (Exception e) {
            log.error("Error al cambiar estado del feriado con ID {}: {}", idFeriado, e.getMessage(), e);
            throw new ActualizarEntidadException("Feriado", "Error al cambiar el estado del feriado: " + e.getMessage());
        }
    }

    // Listar feriados activos por año y locación
    public List<FeriadoDTO> listarFeriadosActivosPorAnioYLocacion(int anio, Integer idLocacion) {
        log.info("Listando feriados activos para año {} y locación ID: {}", anio, idLocacion);
        
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        
        List<Feriado> feriados = feriadoRepositorio.findByEstadoAndFechaBetweenAndIdLocacion_IdLocacion(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin, idLocacion);
        
        List<FeriadoDTO> resultado = feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
        log.info("Se encontraron {} feriados activos para año {} y locación ID {}", resultado.size(), anio, idLocacion);
        
        return resultado;
    }

    // Listar todos los feriados (nacionales y locales) de un año
    public List<FeriadoDTO> listarFeriadosPorAnio(int anio) {
        log.info("Listando todos los feriados para el año: {}", anio);
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        List<Feriado> feriados = feriadoRepositorio.findByEstadoAndFechaBetween(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin);
        log.info("Se encontraron {} feriados en total para el año {}", feriados.size(), anio);
        return feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
    }

    // Listar solo feriados nacionales de un año
    public List<FeriadoDTO> listarFeriadosNacionalesPorAnio(int anio) {
        log.info("Listando feriados NACIONALES para el año: {}", anio);
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        List<Feriado> feriados = feriadoRepositorio.findByEstadoAndFechaBetweenAndTipo(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin, TipoFeriadosEnum.NACIONAL);
        log.info("Se encontraron {} feriados nacionales para el año {}", feriados.size(), anio);
        return feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
    }

    // Listar solo feriados locales de un año, opcionalmente filtrados por locación
    public List<FeriadoDTO> listarFeriadosLocalesPorAnio(int anio, Integer idLocacion) {
        log.info("Listando feriados LOCALES para el año: {}. Filtro de locación ID: {}", anio, idLocacion);
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        List<Feriado> feriados;
        if (idLocacion != null) {
            feriados = feriadoRepositorio.findByEstadoAndFechaBetweenAndTipoAndIdLocacion_IdLocacion(
                    EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin, TipoFeriadosEnum.LOCAL, idLocacion);
        } else {
            feriados = feriadoRepositorio.findByEstadoAndFechaBetweenAndTipo(
                    EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin, TipoFeriadosEnum.LOCAL);
        }
        log.info("Se encontraron {} feriados locales para los criterios de búsqueda.", feriados.size());
        return feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
    }
} 