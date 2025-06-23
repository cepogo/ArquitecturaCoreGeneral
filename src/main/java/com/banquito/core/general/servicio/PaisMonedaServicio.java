package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.MonedaUpdateDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.dto.PaisUpdateDTO;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
        if (paisRepositorio.findByNombre(paisDTO.getNombre()).isPresent()) {
            throw new CrearEntidadException("Pais", "Ya existe un país con el nombre: " + paisDTO.getNombre());
        }
        Pais pais = paisMapper.toEntity(paisDTO);
        pais.setEstado(EstadoGeneralEnum.ACTIVO);
        pais.setVersion(1L);
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    public List<PaisDTO> listarPaisesActivos() {
        return paisRepositorio.findByEstadoOrderByNombre(EstadoGeneralEnum.ACTIVO)
                .stream()
                .map(paisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaisDTO actualizarParcialmentePais(String idPais, PaisUpdateDTO paisUpdateDTO) {
        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais"));

        Optional.ofNullable(paisUpdateDTO.getNombre()).ifPresent(pais::setNombre);
        Optional.ofNullable(paisUpdateDTO.getCodigoTelefono()).ifPresent(pais::setCodigoTelefono);

        pais.setVersion(pais.getVersion() + 1);
        paisRepositorio.save(pais);

        return paisMapper.toDTO(pais);
    }


    @Transactional
    public PaisDTO cambiarEstadoPais(String idPais, EstadoGeneralEnum nuevoEstado) {
        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais"));

        pais.setEstado(nuevoEstado);
        pais.setVersion(pais.getVersion() + 1);

        if (nuevoEstado == EstadoGeneralEnum.INACTIVO) {

            List<Moneda> monedas = monedaRepositorio.findByIdPais_IdPais(idPais);
            if (!monedas.isEmpty()) {
                for (Moneda moneda : monedas) {
                    this.cambiarEstadoMoneda(moneda.getIdMoneda(), EstadoGeneralEnum.INACTIVO);
                }
            }

            List<Feriado> feriados = feriadoRepositorio.findByIdPais_IdPais(idPais);
            if (!feriados.isEmpty()) {
                for (Feriado feriado : feriados) {
                    feriado.setEstado(EstadoGeneralEnum.INACTIVO);
                    feriado.setVersion(feriado.getVersion()+ 1);
                    feriadoRepositorio.save(feriado);
                }
            }

            List<EstructuraGeografica> estructuras = estructuraGeograficaRepositorio.findById_IdPais(idPais);

            if (!estructuras.isEmpty()) {
                for (EstructuraGeografica estructura : estructuras) {
                    estructura.setEstado(EstadoGeneralEnum.INACTIVO);
                    estructura.setVersion(estructura.getVersion() + 1);
                    estructuraGeograficaRepositorio.save(estructura);
                }
            }

            List<LocacionGeografica> locaciones = locacionGeograficaRepositorio.findByEstructuraGeografica_Id_IdPais(idPais);
            if (!locaciones.isEmpty()) {
                for (LocacionGeografica locacion : locaciones) {
                    locacion.setEstado(EstadoLocacionesGeograficasEnum.INACTIVO);
                    locacion.setVersion(locacion.getVersion() + 1);
                    locacionGeograficaRepositorio.save(locacion);
                }

                List<Integer> idsLocaciones = locaciones.stream().map(LocacionGeografica::getIdLocacion).collect(Collectors.toList());
                List<Sucursal> sucursales = sucursalRepositorio.findByIdLocacion_IdLocacionIn(idsLocaciones);
                if (!sucursales.isEmpty()) {
                    for (Sucursal sucursal : sucursales) {
                        sucursal.setEstado(EstadoSucursalesEnum.INACTIVO.getValor());
                        sucursal.setVersion(sucursal.getVersion() + 1);
                        sucursalRepositorio.save(sucursal);
                    }
                }
            }
        }
        return paisMapper.toDTO(paisRepositorio.save(pais));
    }

    @Transactional
    public MonedaDTO crearMonedaPorPais(String idPais, MonedaDTO monedaDTO) {
        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais"));

        if (pais.getEstado().equals(EstadoGeneralEnum.INACTIVO)) {
            throw new CrearEntidadException("Moneda", "El país " + pais.getNombre() + " está inactivo.");
        }
        if (monedaRepositorio.findByNombre(monedaDTO.getNombre()).isPresent()) {
            throw new CrearEntidadException("Moneda", "Ya existe una moneda con el nombre: " + monedaDTO.getNombre());
        }

        Moneda moneda = monedaMapper.toEntity(monedaDTO);
        moneda.setIdPais(pais);
        moneda.setEstado(EstadoGeneralEnum.ACTIVO);
        moneda.setVersion(1L);
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }

    public List<MonedaDTO> listarMonedasActivas() {
        return monedaRepositorio.findByEstado(EstadoGeneralEnum.ACTIVO)
                .stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MonedaDTO actualizarParcialmenteMoneda(String idMoneda, MonedaUpdateDTO monedaDTO) {
        Moneda moneda = monedaRepositorio.findById(idMoneda)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la moneda con ID: " + idMoneda, 2, "Moneda"));

        Optional.ofNullable(monedaDTO.getNombre()).ifPresent(moneda::setNombre);
        Optional.ofNullable(monedaDTO.getSimbolo()).ifPresent(moneda::setSimbolo);

        moneda.setVersion(moneda.getVersion() + 1);
        monedaRepositorio.save(moneda);

        return monedaMapper.toDTO(moneda);
    }


    @Transactional
    public MonedaDTO cambiarEstadoMoneda(String idMoneda, EstadoGeneralEnum nuevoEstado) {
        Moneda moneda = monedaRepositorio.findById(idMoneda)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la moneda con ID: " + idMoneda, 2, "Moneda"));

        if (nuevoEstado == EstadoGeneralEnum.ACTIVO) {
            Pais paisDeLaMoneda = paisRepositorio.findById(moneda.getIdPais().getIdPais())
                    .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país asociado a la moneda", 2, "Pais"));
            if (paisDeLaMoneda.getEstado() == EstadoGeneralEnum.INACTIVO) {
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
                }
            }
        }
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }
}
