package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.dto.PaisUpdateDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.mapper.PaisMapper;
import com.banquito.core.general.modelo.EntidadBancariaMoneda;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.repositorio.EntidadBancariaMonedaRepositorio;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.PaisRepositorio;
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
    private final PaisMapper paisMapper;
    private final MonedaMapper monedaMapper;
    private final EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio;


    public PaisMonedaServicio(PaisRepositorio paisRepositorio, MonedaRepositorio monedaRepositorio, PaisMapper paisMapper, MonedaMapper monedaMapper, EntidadBancariaMonedaRepositorio entidadBancariaMonedaRepositorio) {
        this.paisRepositorio = paisRepositorio;
        this.monedaRepositorio = monedaRepositorio;
        this.paisMapper = paisMapper;
        this.monedaMapper = monedaMapper;
        this.entidadBancariaMonedaRepositorio = entidadBancariaMonedaRepositorio;
    }

    @Transactional
    public PaisDTO crearPais(PaisDTO paisDTO) {
        if (paisRepositorio.findByNombre(paisDTO.getNombre()).isPresent()) {
            throw new CrearEntidadException("Pais", "Ya existe un país con el nombre: " + paisDTO.getNombre());
        }
        Pais pais = paisMapper.toEntity(paisDTO);
        pais.setEstado(EstadoGeneralEnum.ACTIVO);
        pais.setVersion(BigDecimal.ONE);
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
        Optional.ofNullable(paisUpdateDTO.getEstado()).ifPresent(pais::setEstado);

        pais.setVersion(pais.getVersion().add(BigDecimal.ONE));
        paisRepositorio.save(pais);

        return paisMapper.toDTO(pais);
    }

    @Transactional
    public void eliminarPais(String idPais) {
        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais"));

        if (pais.getEstado().equals(EstadoGeneralEnum.INACTIVO)) {
            throw new ActualizarEntidadException("Pais", "El país ya se encuentra inactivo.");
        }

        pais.setEstado(EstadoGeneralEnum.INACTIVO);
        pais.setVersion(pais.getVersion().add(BigDecimal.ONE));
        paisRepositorio.save(pais);

        List<Moneda> monedas = monedaRepositorio.findByIdPais(idPais);
        List<String> idsMonedas = monedas.stream().map(Moneda::getIdMoneda).toList();
        for (Moneda moneda : monedas) {
            moneda.setEstado(EstadoGeneralEnum.INACTIVO);
            moneda.setVersion(moneda.getVersion().add(BigDecimal.ONE));
            monedaRepositorio.save(moneda);
        }

        if (!idsMonedas.isEmpty()) {
            List<EntidadBancariaMoneda> relaciones = entidadBancariaMonedaRepositorio.findByIdMonedaIn(idsMonedas);
            for (EntidadBancariaMoneda relacion : relaciones) {
                relacion.setEstado(EstadoGeneralEnum.INACTIVO);
                relacion.setVersion(relacion.getVersion().add(BigDecimal.ONE));
                entidadBancariaMonedaRepositorio.save(relacion);
            }
        }
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
        moneda.setIdPais(idPais);
        moneda.setEstado(EstadoGeneralEnum.ACTIVO);
        moneda.setVersion(BigDecimal.ONE);
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }

    public List<MonedaDTO> listarMonedasActivas() {
        return monedaRepositorio.findByEstado(EstadoGeneralEnum.ACTIVO)
                .stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MonedaDTO actualizarParcialmenteMoneda(String idMoneda, MonedaDTO monedaDTO) {
        Moneda moneda = monedaRepositorio.findById(idMoneda)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la moneda con ID: " + idMoneda, 2, "Moneda"));

        Optional.ofNullable(monedaDTO.getNombre()).ifPresent(moneda::setNombre);
        Optional.ofNullable(monedaDTO.getSimbolo()).ifPresent(moneda::setSimbolo);
        Optional.ofNullable(monedaDTO.getEstado()).ifPresent(moneda::setEstado);

        moneda.setVersion(moneda.getVersion().add(BigDecimal.ONE));
        monedaRepositorio.save(moneda);

        return monedaMapper.toDTO(moneda);
    }

    @Transactional
    public void eliminarMoneda(String idMoneda) {
        Moneda moneda = monedaRepositorio.findById(idMoneda)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la moneda con ID: " + idMoneda, 2, "Moneda"));

        if (moneda.getEstado().equals(EstadoGeneralEnum.INACTIVO)) {
            throw new ActualizarEntidadException("Moneda", "La moneda ya se encuentra inactiva.");
        }

        moneda.setEstado(EstadoGeneralEnum.INACTIVO);
        moneda.setVersion(moneda.getVersion().add(BigDecimal.ONE));
        monedaRepositorio.save(moneda);

        List<EntidadBancariaMoneda> relaciones = entidadBancariaMonedaRepositorio.findByIdMonedaIn(List.of(idMoneda));
        for (EntidadBancariaMoneda relacion : relaciones) {
            relacion.setEstado(EstadoGeneralEnum.INACTIVO);
            relacion.setVersion(relacion.getVersion().add(BigDecimal.ONE));
            entidadBancariaMonedaRepositorio.save(relacion);
        }
    }
}
