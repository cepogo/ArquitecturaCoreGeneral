package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeriadoServicio {
    @Autowired
    private FeriadoRepositorio feriadoRepositorio;
    @Autowired
    private PaisRepositorio paisRepositorio;
    @Autowired
    private LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    @Autowired
    private FeriadoMapper feriadoMapper;

    // Crear feriado por país o locación geográfica
    @Transactional
    public FeriadoDTO crearFeriado(FeriadoDTO dto) {
        try {
            Feriado entity = feriadoMapper.toEntity(dto);
            entity.setEstado(EstadoGeneralEnum.ACTIVO);
            entity.setVersion(1L);
            if (dto.getIdPais() != null) {
                Pais pais = paisRepositorio.findById(dto.getIdPais()).orElse(null);
                entity.setIdPais(pais);
            }
            if (dto.getIdLocacion() != null) {
                LocacionGeografica loc = locacionGeograficaRepositorio.findById(dto.getIdLocacion()).orElse(null);
                entity.setIdLocacion(loc);
            }
            return feriadoMapper.toDTO(feriadoRepositorio.save(entity));
        } catch (Exception e) {
            throw new CrearEntidadException("Feriado", "Error al crear el feriado: " + e.getMessage());
        }
    }

    // Eliminación lógica
    @Transactional
    public void eliminarLogicoFeriado(Integer idFeriado) {
        Feriado entity = feriadoRepositorio.findById(idFeriado)
                .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado", 2, "Feriado"));
        try {
            entity.setEstado(EstadoGeneralEnum.INACTIVO);
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            feriadoRepositorio.save(entity);
        } catch (Exception e) {
            throw new EliminarEntidadException("Feriado", "Error al eliminar lógicamente el feriado: " + e.getMessage());
        }
    }

    // Modificar solo fecha y nombre
    @Transactional
    public FeriadoDTO modificarFeriado(FeriadoDTO dto) {
        Feriado entity = feriadoRepositorio.findById(dto.getIdFeriado())
                .orElseThrow(() -> new EntidadNoEncontradaException("Feriado no encontrado", 2, "Feriado"));
        try {
            if (dto.getFecha() != null) entity.setFecha(dto.getFecha());
            if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return feriadoMapper.toDTO(feriadoRepositorio.save(entity));
        } catch (Exception e) {
            throw new ActualizarEntidadException("Feriado", "Error al modificar el feriado: " + e.getMessage());
        }
    }

    // Listar feriados activos por año y locación
    public List<FeriadoDTO> listarFeriadosActivosPorAnioYLocacion(int anio, Integer idLocacion) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, Calendar.JANUARY, 1, 0, 0, 0);
        Date fechaInicio = cal.getTime();
        cal.set(anio, Calendar.DECEMBER, 31, 23, 59, 59);
        Date fechaFin = cal.getTime();
        List<Feriado> feriados = feriadoRepositorio.findByEstadoAndFechaBetweenAndIdLocacion_IdLocacion(
                EstadoGeneralEnum.ACTIVO, fechaInicio, fechaFin, idLocacion);
        return feriados.stream().map(feriadoMapper::toDTO).collect(Collectors.toList());
    }
} 