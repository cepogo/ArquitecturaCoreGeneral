package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.EntidadBancariaDTO;
import com.banquito.core.general.dto.EntidadBancariaMonedaDTO;
import com.banquito.core.general.dto.EntidadBancariaUpdateDTO;
import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.EntidadBancariaMapper;
import com.banquito.core.general.mapper.EntidadBancariaMonedaMapper;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.EntidadBancariaMoneda;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.repositorio.EntidadBancariaMonedaRepositorio;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntidadBancariaMonedaServicio {
    private final EntidadBancariaRepositorio entidadBancariaRepositorio;
    private final EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio;
    private final MonedaRepositorio monedaRepositorio;
    private final EntidadBancariaMapper entidadBancariaMapper;
    private final EntidadBancariaMonedaMapper entidadBancariaMonedaMapper;
    private final MonedaMapper monedaMapper;

    public EntidadBancariaMonedaServicio(EntidadBancariaRepositorio entidadBancariaRepositorio, EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio, MonedaRepositorio monedaRepositorio, EntidadBancariaMapper entidadBancariaMapper, EntidadBancariaMonedaMapper entidadBancariaMonedaMapper, MonedaMapper monedaMapper) {
        this.entidadBancariaRepositorio = entidadBancariaRepositorio;
        this.entidadBancariaMonedaRepositorio = entidadBancariaMonedaRepositorio;
        this.monedaRepositorio = monedaRepositorio;
        this.entidadBancariaMapper = entidadBancariaMapper;
        this.entidadBancariaMonedaMapper = entidadBancariaMonedaMapper;
        this.monedaMapper = monedaMapper;
    }

    @Transactional
    public EntidadBancariaDTO modificarEntidadBancaria(Integer id, EntidadBancariaDTO entidadBancariaDTO) {
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));

        entidad.setNombre(entidadBancariaDTO.getNombre());
        entidad.setCodigoLocal(entidadBancariaDTO.getCodigoLocal());
        entidad.setCodigoInternacional(entidadBancariaDTO.getCodigoInternacional());
        entidad.setVersion(entidad.getVersion().add(BigDecimal.ONE));
        return entidadBancariaMapper.toDTO(entidad);
    }

    @Transactional
    public EntidadBancariaDTO modificarParcialmenteEntidadBancaria(Integer id, EntidadBancariaUpdateDTO entidadBancariaDTO) {
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));

        Optional.ofNullable(entidadBancariaDTO.getNombre()).ifPresent(entidad::setNombre);
        Optional.ofNullable(entidadBancariaDTO.getCodigoLocal()).ifPresent(entidad::setCodigoLocal);
        Optional.ofNullable(entidadBancariaDTO.getCodigoInternacional()).ifPresent(entidad::setCodigoInternacional);
        Optional.ofNullable(entidadBancariaDTO.getEstado()).ifPresent(entidad::setEstado);

        entidad.setVersion(entidad.getVersion().add(BigDecimal.ONE));
        entidadBancariaRepositorio.save(entidad);

        return entidadBancariaMapper.toDTO(entidad);
    }

    @Transactional
    public void eliminarEntidadBancaria(Integer id) {
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));

        if (entidad.getEstado().equals(EstadoGeneralEnum.INACTIVO)) {
            throw new ActualizarEntidadException("EntidadBancaria", "La entidad ya se encuentra inactiva.");
        }
        entidad.setEstado(EstadoGeneralEnum.INACTIVO);
        entidad.setVersion(entidad.getVersion().add(BigDecimal.ONE));
        entidadBancariaRepositorio.save(entidad);

        List<EntidadBancariaMoneda> monedasAsociadas = entidadBancariaMonedaRepositorio.findByIdEntidadBancaria(entidad);
        for (EntidadBancariaMoneda ebm : monedasAsociadas) {
            ebm.setEstado(EstadoGeneralEnum.INACTIVO);
            ebm.setVersion(ebm.getVersion().add(BigDecimal.ONE));
            entidadBancariaMonedaRepositorio.save(ebm);
        }
    }

    public EntidadBancariaDTO obtenerPorId(Integer id) {
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));
        return entidadBancariaMapper.toDTO(entidad);
    }

    public EntidadBancariaDTO obtenerPrimeraEntidadBancariaActiva() {
        EntidadBancaria entidad = entidadBancariaRepositorio.findFirstByEstado(EstadoGeneralEnum.ACTIVO)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontraron entidades bancarias activas.", 2, "EntidadBancaria"));
        return entidadBancariaMapper.toDTO(entidad);
    }

    @Transactional
    public EntidadBancariaMonedaDTO agregarMonedaAEntidadBancaria(Integer idEntidad, String idMoneda) {
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(idEntidad)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + idEntidad, 2, "EntidadBancaria"));

        if (!entidad.getEstado().equals(EstadoGeneralEnum.ACTIVO)) {
            throw new CrearEntidadException("EntidadBancariaMoneda", "La entidad bancaria no está activa.");
        }
        Moneda moneda = monedaRepositorio.findById(idMoneda)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la moneda con ID: " + idMoneda, 2, "Moneda"));
        if (!moneda.getEstado().equals(EstadoGeneralEnum.ACTIVO)) {
            throw new CrearEntidadException("EntidadBancariaMoneda", "La moneda no está activa.");
        }

        EntidadBancariaMoneda nuevaRelacion = new EntidadBancariaMoneda();
        nuevaRelacion.setIdEntidadBancaria(entidad);
        nuevaRelacion.setIdMoneda(idMoneda);
        nuevaRelacion.setEstado(EstadoGeneralEnum.ACTIVO);
        nuevaRelacion.setVersion(BigDecimal.ONE);
        return entidadBancariaMonedaMapper.toDTO(entidadBancariaMonedaRepositorio.save(nuevaRelacion));
    }

    @Transactional
    public EntidadBancariaMonedaDTO cambiarEstadoMonedaDeEntidad(Integer idRelacion, EstadoGeneralEnum nuevoEstado) {
        EntidadBancariaMoneda relacion = entidadBancariaMonedaRepositorio.findById(idRelacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la relación con ID: " + idRelacion, 2, "EntidadBancariaMoneda"));
        relacion.setEstado(nuevoEstado);
        relacion.setVersion(relacion.getVersion().add(BigDecimal.ONE));
        return entidadBancariaMonedaMapper.toDTO(entidadBancariaMonedaRepositorio.save(relacion));
    }

    public List<MonedaDTO> obtenerMonedasActivasDeEntidad(Integer idEntidad) {
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(idEntidad)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + idEntidad, 2, "EntidadBancaria"));

        List<EntidadBancariaMoneda> relacionesActivas = entidadBancariaMonedaRepositorio.findByIdEntidadBancariaAndEstado(entidad, EstadoGeneralEnum.ACTIVO);

        List<String> idsMonedas = relacionesActivas.stream()
                .map(EntidadBancariaMoneda::getIdMoneda)
                .collect(Collectors.toList());

        return monedaRepositorio.findAllById(idsMonedas)
                .stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
