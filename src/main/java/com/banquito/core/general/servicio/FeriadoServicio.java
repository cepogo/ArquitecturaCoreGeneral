package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.dto.FeriadoUpdateDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EliminarEntidadException;
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
        try {
            log.info("Creando feriado: {} - Tipo: {}", dto.getNombre(), dto.getTipo());
            
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
                // Feriado nacional: idLocacion = null
                feriado.setIdLocacion(null);
                log.info("Feriado nacional creado para país: {}", dto.getIdPais());
            } else if (TipoFeriadosEnum.LOCAL.equals(dto.getTipo())) {
                // Feriado local: verificar que se proporcionó idLocacion
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
                log.info("Feriado local creado para locación: {} en país: {}", dto.getIdLocacion(), dto.getIdPais());
            }
            
            Feriado feriadoGuardado = feriadoRepositorio.save(feriado);
            log.info("Feriado creado exitosamente con ID: {}", feriadoGuardado.getIdFeriado());
            
            return feriadoMapper.toDTO(feriadoGuardado);
        } catch (EntidadNoEncontradaException | CrearEntidadException e) {
            log.error("Error al crear feriado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear feriado", e);
            throw new CrearEntidadException("Feriado", "Error al crear el feriado: " + e.getMessage());
        }
    }

    // Modificar solo fecha y nombre
    @Transactional
    public FeriadoDTO modificarFeriado(Integer idFeriado, FeriadoUpdateDTO dto) {
        Feriado entity = feriadoRepositorio.findById(idFeriado)
                .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado", 2, "Feriado"));
        try {
            log.info("Modificando feriado con ID: {}", idFeriado);
            
            // Actualizar solo fecha y nombre
            entity.setFecha(dto.getFecha());
            entity.setNombre(dto.getNombre());
            
            // Incrementar versión
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            
            Feriado feriadoActualizado = feriadoRepositorio.save(entity);
            log.info("Feriado modificado exitosamente");
            
            return feriadoMapper.toDTO(feriadoActualizado);
        } catch (Exception e) {
            log.error("Error al modificar feriado", e);
            throw new ActualizarEntidadException("Feriado", "Error al modificar el feriado: " + e.getMessage());
        }
    }

    // Eliminación lógica
    @Transactional
    public void eliminarLogicoFeriado(Integer idFeriado) {
        Feriado entity = feriadoRepositorio.findById(idFeriado)
                .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado", 2, "Feriado"));
        try {
            log.info("Eliminando lógicamente feriado con ID: {}", idFeriado);
            
            entity.setEstado(EstadoGeneralEnum.INACTIVO);
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            feriadoRepositorio.save(entity);
            
            log.info("Feriado eliminado lógicamente exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar feriado", e);
            throw new EliminarEntidadException("Feriado", "Error al eliminar lógicamente el feriado: " + e.getMessage());
        }
    }

    // Listar feriados activos por año y locación
    public List<FeriadoDTO> listarFeriadosActivosPorAnioYLocacion(int anio, Integer idLocacion) {
        log.info("Listando feriados activos para año: {} y locación: {}", anio, idLocacion);
        
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        
        List<Feriado> feriados = feriadoRepositorio.findByEstadoAndFechaBetweenAndIdLocacion_IdLocacion(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin, idLocacion);
        
        List<FeriadoDTO> resultado = feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
        log.info("Se encontraron {} feriados activos", resultado.size());
        
        return resultado;
    }

    // Listar todos los feriados (nacionales y locales) de un año
    public List<FeriadoDTO> listarFeriadosPorAnio(int anio) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        List<Feriado> feriados = feriadoRepositorio.findByEstadoAndFechaBetween(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin);
        return feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
    }

    // Listar solo feriados nacionales de un año
    public List<FeriadoDTO> listarFeriadosNacionalesPorAnio(int anio) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        List<Feriado> feriados = feriadoRepositorio.findByEstadoAndFechaBetweenAndTipo(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin, TipoFeriadosEnum.NACIONAL);
        return feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
    }

    // Listar solo feriados locales de un año, opcionalmente filtrados por locación
    public List<FeriadoDTO> listarFeriadosLocalesPorAnio(int anio, Integer idLocacion) {
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
        return feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
    }
} 