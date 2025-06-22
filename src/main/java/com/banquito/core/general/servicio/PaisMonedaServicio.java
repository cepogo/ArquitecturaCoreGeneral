package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.MonedaMapper;
import com.banquito.core.general.mapper.PaisMapper;
import com.banquito.core.general.modelo.Moneda;
import com.banquito.core.general.modelo.Pais;
import com.banquito.core.general.repositorio.MonedaRepositorio;
import com.banquito.core.general.repositorio.PaisRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaisMonedaServicio {
    private final PaisRepositorio paisRepositorio;
    private final MonedaRepositorio monedaRepositorio;
    private final PaisMapper paisMapper;
    private final MonedaMapper monedaMapper;


    public PaisMonedaServicio(PaisRepositorio paisRepositorio, MonedaRepositorio monedaRepositorio, PaisMapper paisMapper, MonedaMapper monedaMapper) {
        this.paisRepositorio = paisRepositorio;
        this.monedaRepositorio = monedaRepositorio;
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
    public PaisDTO actualizarPais(String idPais, PaisDTO paisDTO) {
        Pais pais = paisRepositorio.findById(idPais)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el país con ID: " + idPais, 2, "Pais"));

        pais.setNombre(paisDTO.getNombre());
        pais.setCodigoTelefono(paisDTO.getCodigoTelefono());
        pais.setVersion(pais.getVersion().add(BigDecimal.ONE));
        return paisMapper.toDTO(paisRepositorio.save(pais));
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
        for (Moneda moneda : monedas) {
            moneda.setEstado(EstadoGeneralEnum.INACTIVO.getValor());
            moneda.setVersion(moneda.getVersion().add(BigDecimal.ONE));
            monedaRepositorio.save(moneda);
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
        moneda.setEstado(EstadoGeneralEnum.ACTIVO.getValor());
        moneda.setVersion(BigDecimal.ONE);
        return monedaMapper.toDTO(monedaRepositorio.save(moneda));
    }

    public List<MonedaDTO> listarMonedasActivas() {
        return monedaRepositorio.findByEstado(EstadoGeneralEnum.ACTIVO.getValor())
                .stream()
                .map(monedaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
