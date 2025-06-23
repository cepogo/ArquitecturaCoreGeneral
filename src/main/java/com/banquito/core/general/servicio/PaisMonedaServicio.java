package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.*;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.mapper.PaisMapper;
import com.banquito.core.general.modelo.*;
import com.banquito.core.general.repositorio.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaisMonedaServicio {
    private final PaisRepositorio paisRepositorio;
    private final MonedaRepositorio monedaRepositorio;
    private final EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio;
    private final FeriadoRepositorio feriadoRepositorio;
    private final EstructuraGeograficaRepositorio estructuraGeograficaRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final SucursalRepositorio sucursalRepositorio;

    private final PaisMapper paisMapper;
    private final MonedaMapper monedaMapper;


    public PaisMonedaServicio(PaisRepositorio paisRepositorio, MonedaRepositorio monedaRepositorio, EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio, FeriadoRepositorio feriadoRepositorio, EstructuraGeograficaRepositorio estructuraGeograficaRepositorio, LocacionGeograficaRepositorio locacionGeograficaRepositorio, SucursalRepositorio sucursalRepositorio, PaisMapper paisMapper, MonedaMapper monedaMapper) {
        this.paisRepositorio = paisRepositorio;
        this.monedaRepositorio = monedaRepositorio;
        this.entidadBancariaMonedaRepositorio = entidadBancariaMonedaRepositorio;
        this.feriadoRepositorio = feriadoRepositorio;
        this.estructuraGeograficaRepositorio = estructuraGeograficaRepositorio;
        this.locacionGeograficaRepositorio = locacionGeograficaRepositorio;
        this.sucursalRepositorio = sucursalRepositorio;
        this.paisMapper = paisMapper;
        this.monedaMapper = monedaMapper;
    }

    @Transactional
    public PaisDTO crearPais(PaisDTO paisDTO) {
        log.info("Iniciando creación de país: {}", paisDTO.getNombre());
        if (paisRepositorio.findByNombre(paisDTO.getNombre()).isPresent()) {
            log.warn("Ya existe un país con el nombre: {}", paisDTO.getNombre());
            throw new CrearEntidadException("Pais", "Ya existe un país con el nombre: " + paisDTO.getNombre());
        }
        Pais pais = paisMapper.toEntity(paisDTO);
        pais.setEstado(EstadoGeneralEnum.ACTIVO);
        pais.setVersion(1L);
        PaisDTO result = paisMapper.toDTO(paisRepositorio.save(pais));
        log.info("País creado exitosamente con ID: {}", result.getIdPais());
        return result;
    }

    public List<PaisDTO> listarPaisesActivos() {
        log.info("Listando países activos");
        return paisRepositorio.findByEstadoOrderByNombre(EstadoGeneralEnum.ACTIVO)
                .stream()
                .map(paisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaisDTO actualizarParcialmentePais(String idPais, PaisUpdateDTO paisUpdateDTO) {
        log.info("Iniciando actualización de datos del país con ID: {}", idPais);

        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> {
                    log.error("No se encontró el país con ID: {}", idPais);
                    return new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais");
                });
        Optional.ofNullable(paisUpdateDTO.getNombre()).ifPresent(pais::setNombre);
        Optional.ofNullable(paisUpdateDTO.getCodigoTelefono()).ifPresent(pais::setCodigoTelefono);

        pais.setVersion(pais.getVersion() + 1);
        paisRepositorio.save(pais);
        log.info("País con ID: {} actualizado exitosamente", idPais);
        return paisMapper.toDTO(pais);
    }


    @Transactional
    public PaisDTO cambiarEstadoPais(String idPais, EstadoGeneralEnum nuevoEstado) {
        log.info("Cambiando estado del país con ID: {} a {}", idPais, nuevoEstado);
        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> {
                    log.error("No se encontró el país con ID: {}", idPais);
                    return new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais");
                });
        pais.setEstado(nuevoEstado);
        pais.setVersion(pais.getVersion() + 1);

        if (nuevoEstado == EstadoGeneralEnum.INACTIVO) {

            List<Moneda> monedas = monedaRepositorio.findByIdPais_IdPais(idPais);
            if (!monedas.isEmpty()) {
                for (Moneda moneda : monedas) {
                    this.cambiarEstadoMoneda(moneda.getIdMoneda(), EstadoGeneralEnum.INACTIVO);
                    log.debug("Moneda: {} inactivada", moneda.getIdMoneda());
                }
                log.info("Todas las monedas asociadas al país con ID: {} han sido inactivadas", idPais);
            }

            List<Feriado> feriados = feriadoRepositorio.findByIdPais_IdPais(idPais);
            if (!feriados.isEmpty()) {
                for (Feriado feriado : feriados) {
                    feriado.setEstado(EstadoGeneralEnum.INACTIVO);
                    feriado.setVersion(feriado.getVersion()+ 1);
                    feriadoRepositorio.save(feriado);
                }
                log.info("Todos los feriados asociados al país con ID: {} han sido inactivados", idPais);
            }

            List<EstructuraGeografica> estructuras = estructuraGeograficaRepositorio.findById_IdPais(idPais);

            if (!estructuras.isEmpty()) {
                for (EstructuraGeografica estructura : estructuras) {
                    estructura.setEstado(EstadoGeneralEnum.INACTIVO);
                    estructura.setVersion(estructura.getVersion() + 1);
                    estructuraGeograficaRepositorio.save(estructura);
                    log.debug("Estructura geográfica: {} inactivada", estructura.getNombre());
                }
                log.info("Todas las estructuras geográficas asociadas al país con ID: {} han sido inactivadas", idPais);
            }

            List<LocacionGeografica> locaciones = locacionGeograficaRepositorio.findByEstructuraGeografica_Id_IdPais(idPais);
            if (!locaciones.isEmpty()) {
                for (LocacionGeografica locacion : locaciones) {
                    locacion.setEstado(EstadoLocacionesGeograficasEnum.INACTIVO);
                    locacion.setVersion(locacion.getVersion() + 1);
                    locacionGeograficaRepositorio.save(locacion);
                }
                log.info("Todas las locaciones geográficas asociadas al país con ID: {} han sido inactivadas", idPais);

                List<Integer> idsLocaciones = locaciones.stream().map(LocacionGeografica::getIdLocacion).collect(Collectors.toList());
                List<Sucursal> sucursales = sucursalRepositorio.findByIdLocacion_IdLocacionIn(idsLocaciones);
                if (!sucursales.isEmpty()) {
                    for (Sucursal sucursal : sucursales) {
                        sucursal.setEstado(EstadoSucursalesEnum.INACTIVO.getValor());
                        sucursal.setVersion(sucursal.getVersion() + 1);
                        sucursalRepositorio.save(sucursal);
                        log.debug("Sucursal: {} inactivada", sucursal.getNombre());
                    }
                    log.info("Todas las sucursales asociadas a las locaciones geográficas del país con ID: {} han sido inactivadas", idPais);
                }
            }
        }
        log.info("Estado del país actualizado a {} para el país con ID: {}", nuevoEstado, idPais);
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    @Transactional
    public MonedaDTO crearMonedaPorPais(String idPais, MonedaCreateDTO monedaDTO) {
        log.info("Iniciando creación de moneda para el país con ID: {}", idPais);
        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais"));

        if (pais.getEstado().equals(EstadoGeneralEnum.INACTIVO)) {
            log.warn("Se intentó crear una moneda para un país inactivo: {}", pais.getNombre());
            throw new CrearEntidadException("Moneda", "El país " + pais.getNombre() + " está inactivo.");
        }
        if (monedaRepositorio.findById(monedaDTO.getIdMoneda()).isPresent()) {
            log.warn("Se intentó crear una moneda con un id existente: {}", monedaDTO.getIdMoneda());
            throw new CrearEntidadException("Moneda", "Ya existe una moneda con el id: " + monedaDTO.getIdMoneda());
        }

        Moneda moneda = monedaMapper.toCreateEntity(monedaDTO);
        moneda.setIdPais(pais);
        moneda.setEstado(EstadoGeneralEnum.ACTIVO);
        moneda.setVersion(1L);

        log.info("Moneda creada exitosamente con ID: {}", moneda.getIdMoneda());

        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }

    public List<MonedaDTO> listarMonedasActivas() {
        log.info("Listando monedas activas.");
        return monedaRepositorio.findByEstado(EstadoGeneralEnum.ACTIVO)
                .stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MonedaDTO actualizarParcialmenteMoneda(String idMoneda, MonedaUpdateDTO monedaDTO) {
        log.info("Iniciando actualización parcial de la moneda con ID: {}", idMoneda);

        Moneda moneda = monedaRepositorio.findById(idMoneda)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la moneda con ID: " + idMoneda, 2, "Moneda"));

        Optional.ofNullable(monedaDTO.getNombre()).ifPresent(moneda::setNombre);
        Optional.ofNullable(monedaDTO.getSimbolo()).ifPresent(moneda::setSimbolo);

        moneda.setVersion(moneda.getVersion() + 1);
        monedaRepositorio.save(moneda);

        log.info("Moneda con ID: {} actualizada exitosamente", idMoneda);

        return monedaMapper.toDTO(moneda);
    }


    @Transactional
    public MonedaDTO cambiarEstadoMoneda(String idMoneda, EstadoGeneralEnum nuevoEstado) {
        log.info("Cambiando estado de la moneda con ID: {} a {}", idMoneda, nuevoEstado);

        Moneda moneda = monedaRepositorio.findById(idMoneda)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la moneda con ID: " + idMoneda, 2, "Moneda"));

        if (nuevoEstado == EstadoGeneralEnum.ACTIVO) {
            Pais paisDeLaMoneda = paisRepositorio.findById(moneda.getIdPais().getIdPais())
                    .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país asociado a la moneda", 2, "Pais"));
            if (paisDeLaMoneda.getEstado() == EstadoGeneralEnum.INACTIVO) {
                log.warn("No se puede activar la moneda porque el país '{}' está inactivo.", paisDeLaMoneda.getNombre());
                throw new ActualizarEntidadException("Moneda", "No se puede activar la moneda porque el país '" + paisDeLaMoneda.getNombre() + "' está inactivo.");
            }
        }
        moneda.setEstado(nuevoEstado);
        moneda.setVersion(moneda.getVersion() + 1);

        if (nuevoEstado == EstadoGeneralEnum.INACTIVO) {
            List<EntidadBancariaMoneda> relaciones = entidadBancariaMonedaRepositorio.findByIdMoneda_IdMonedaIn(List.of(idMoneda));
            if (!relaciones.isEmpty()) {
                for (EntidadBancariaMoneda relacion : relaciones) {
                    relacion.setEstado(EstadoGeneralEnum.INACTIVO);
                    relacion.setVersion(relacion.getVersion() + 1);
                    entidadBancariaMonedaRepositorio.save(relacion);
                    log.debug("Moneda: {} inactivada para la entidad: {}", relacion.getIdMoneda(),relacion.getIdEntidadBancaria().getNombre());
                }
            }
        }
        log.info("Estado de la moneda actualizado a {} para la moneda con ID: {}", nuevoEstado, idMoneda);
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }
}
