package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.EntidadBancariaDTO;
import com.banquito.core.general.dto.EntidadBancariaMonedaDTO;
import com.banquito.core.general.dto.EntidadBancariaUpdateDTO;
import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.EntidadBancariaMapper;
import com.banquito.core.general.mapper.EntidadBancariaMonedaMapper;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.EntidadBancariaMoneda;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.repositorio.EntidadBancariaMonedaRepositorio;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.SucursalRepositorio;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EntidadBancariaMonedaServicio {
    private final EntidadBancariaRepositorio entidadBancariaRepositorio;
    private final EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio;
    private final MonedaRepositorio monedaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;
    private final EntidadBancariaMapper entidadBancariaMapper;
    private final EntidadBancariaMonedaMapper entidadBancariaMonedaMapper;
    private final MonedaMapper monedaMapper;

    public EntidadBancariaMonedaServicio(EntidadBancariaRepositorio entidadBancariaRepositorio, EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio, MonedaRepositorio monedaRepositorio, SucursalRepositorio sucursalRepositorio, EntidadBancariaMapper entidadBancariaMapper, EntidadBancariaMonedaMapper entidadBancariaMonedaMapper, MonedaMapper monedaMapper) {
        this.entidadBancariaRepositorio = entidadBancariaRepositorio;
        this.entidadBancariaMonedaRepositorio = entidadBancariaMonedaRepositorio;
        this.monedaRepositorio = monedaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.entidadBancariaMapper = entidadBancariaMapper;
        this.entidadBancariaMonedaMapper = entidadBancariaMonedaMapper;
        this.monedaMapper = monedaMapper;
    }


    @Transactional
    public EntidadBancariaDTO modificarParcialmenteEntidadBancaria(Integer id, EntidadBancariaUpdateDTO entidadBancariaDTO) {
        log.info("Modificando la entidad bancaria con ID: {}", id);
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la entidad bancaria con ID: {}", id);
                    return new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria");
                });
        Optional.ofNullable(entidadBancariaDTO.getNombre()).ifPresent(entidad::setNombre);
        Optional.ofNullable(entidadBancariaDTO.getCodigoLocal()).ifPresent(entidad::setCodigoLocal);
        Optional.ofNullable(entidadBancariaDTO.getCodigoInternacional()).ifPresent(entidad::setCodigoInternacional);

        entidad.setVersion(entidad.getVersion() + 1);
        entidadBancariaRepositorio.save(entidad);
        log.info("Entidad bancaria con ID: {} modificada exitosamente", id);
        return entidadBancariaMapper.toDTO(entidad);
    }

    @Transactional
    public EntidadBancariaDTO cambiarEstadoEntidadBancaria(Integer id, EstadoGeneralEnum nuevoEstado) {
        log.info("Cambiando estado de la entidad bancaria con ID: {} a {}", id, nuevoEstado);

        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));

        entidad.setEstado(nuevoEstado);
        entidad.setVersion(entidad.getVersion() + 1);

        if (nuevoEstado == EstadoGeneralEnum.INACTIVO) {
            List<EntidadBancariaMoneda> monedasAsociadas = entidadBancariaMonedaRepositorio.findByIdEntidadBancaria(entidad);
            if (!monedasAsociadas.isEmpty()) {
                for (EntidadBancariaMoneda ebm : monedasAsociadas) {
                    ebm.setEstado(EstadoGeneralEnum.INACTIVO);
                    ebm.setVersion(ebm.getVersion() + 1);
                    entidadBancariaMonedaRepositorio.save(ebm);
                }
                log.info("Todas las monedas asociadas a la entidad bancaria con ID: {} han sido desactivadas", id);
            }

            List<Sucursal> sucursalesAsociadas = sucursalRepositorio.findByIdEntidadBancaria(entidad);
            if (!sucursalesAsociadas.isEmpty()) {
                for (Sucursal sucursal : sucursalesAsociadas) {
                    sucursal.setEstado(EstadoSucursalesEnum.INACTIVO.getValor());
                    sucursal.setVersion(sucursal.getVersion() + 1);
                    sucursalRepositorio.save(sucursal);
                }
                log.info("Todas las sucursales asociadas a la entidad bancaria con ID: {} han sido desactivadas", id);
            }
        }
        log.info("Estado de la entidad bancaria actualizado a {} para la entidad con ID: {}", nuevoEstado, id);

        return entidadBancariaMapper.toDTO(entidadBancariaRepositorio.save(entidad));
    }

    public EntidadBancariaDTO obtenerPorId(Integer id) {
        log.info("Obteniendo entidad bancaria con ID: {}", id);
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + id, 2, "EntidadBancaria"));
        return entidadBancariaMapper.toDTO(entidad);
    }

    public EntidadBancariaDTO obtenerPrimeraEntidadBancariaActiva() {
        log.info("Obteniendo la primera entidad bancaria activa");
        EntidadBancaria entidad = entidadBancariaRepositorio.findFirstByEstado(EstadoGeneralEnum.ACTIVO)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontraron entidades bancarias activas.", 2, "EntidadBancaria"));
        return entidadBancariaMapper.toDTO(entidad);
    }

    @Transactional
    public EntidadBancariaMonedaDTO agregarMonedaAEntidadBancaria(Integer idEntidad, String idMoneda) {
        log.info("Agregando moneda con ID: {} a la entidad bancaria con ID: {}", idMoneda, idEntidad);

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

        Optional<EntidadBancariaMoneda> relacionExistente = entidadBancariaMonedaRepositorio.findByIdEntidadBancariaAndIdMoneda(entidad, moneda);
        if (relacionExistente.isPresent()) {
            log.warn("La entidad bancaria con ID: {} ya está relacionada con la moneda con ID: {} .", idEntidad, idMoneda);
            throw new CrearEntidadException("EntidadBancariaMoneda", "La relación entre la entidad '" + entidad.getNombre() + "' y la moneda '" + moneda.getNombre() + "' ya existe.");
        }

        EntidadBancariaMoneda nuevaRelacion = new EntidadBancariaMoneda();
        nuevaRelacion.setIdEntidadBancaria(entidad);
        nuevaRelacion.setIdMoneda(moneda);
        nuevaRelacion.setEstado(EstadoGeneralEnum.ACTIVO);
        nuevaRelacion.setVersion(1L);

        log.info("Moneda con ID: {} agregada exitosamente a la entidad bancaria con ID: {}", idMoneda, idEntidad);
        return entidadBancariaMonedaMapper.toDTO(entidadBancariaMonedaRepositorio.save(nuevaRelacion));
    }

    @Transactional
    public EntidadBancariaMonedaDTO cambiarEstadoMonedaDeEntidad(Integer idRelacion, EstadoGeneralEnum nuevoEstado) {
        log.info("Cambiando estado de la relación de moneda con ID: {} a {}", idRelacion, nuevoEstado);
        EntidadBancariaMoneda relacion = entidadBancariaMonedaRepositorio.findById(idRelacion)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la relación con ID: " + idRelacion, 2, "EntidadBancariaMoneda"));

        if (nuevoEstado.equals(EstadoGeneralEnum.ACTIVO)) {
            if (!relacion.getIdEntidadBancaria().getEstado().equals(EstadoGeneralEnum.ACTIVO)) {
                log.warn("No se puede activar la relación porque la entidad bancaria con ID: {} está inactiva.", relacion.getIdEntidadBancaria().getIdEntidadBancaria());
                throw new ActualizarEntidadException("EntidadBancariaMoneda", "No se puede activar la relación porque la entidad bancaria está inactiva.");
            }
            Moneda moneda = monedaRepositorio.findById(relacion.getIdMoneda().getIdMoneda())
                    .orElseThrow(() -> new EntidadNoEncontradaException("Moneda no encontrada", 2, "Moneda"));
            if (!moneda.getEstado().equals(EstadoGeneralEnum.ACTIVO)) {
                log.warn("No se puede activar la relación porque la moneda con ID: {} está inactiva.", relacion.getIdMoneda().getIdMoneda());
                throw new ActualizarEntidadException("EntidadBancariaMoneda", "No se puede activar la relación porque la moneda está inactiva.");
            }
        }
        relacion.setEstado(nuevoEstado);
        relacion.setVersion(relacion.getVersion() + 1);
        log.info("Estado de la relación de moneda con ID: {} cambiado a {}", idRelacion, nuevoEstado);
        return entidadBancariaMonedaMapper.toDTO(entidadBancariaMonedaRepositorio.save(relacion));
    }

    public List<MonedaDTO> obtenerMonedasActivasDeEntidad(Integer idEntidad) {
        log.info("Obteniendo monedas activas de la entidad bancaria con ID: {}", idEntidad);
        EntidadBancaria entidad = entidadBancariaRepositorio.findById(idEntidad)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la entidad bancaria con ID: " + idEntidad, 2, "EntidadBancaria"));

        List<EntidadBancariaMoneda> relacionesActivas = entidadBancariaMonedaRepositorio.findByIdEntidadBancariaAndEstado(entidad, EstadoGeneralEnum.ACTIVO);

        List<String> idsMonedas = relacionesActivas.stream()
                .map(rel -> rel.getIdMoneda().getIdMoneda())
                .collect(Collectors.toList());

        return monedaRepositorio.findAllById(idsMonedas)
                .stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
