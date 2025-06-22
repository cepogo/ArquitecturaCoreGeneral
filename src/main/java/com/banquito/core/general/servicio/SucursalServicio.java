package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.SucursalDTO;
import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.excepcion.EliminarEntidadException;
import com.banquito.core.general.mapper.SucursalMapper;
import com.banquito.core.general.modelo.EntidadBancaria;
import com.banquito.core.general.modelo.LocacionGeografica;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.repositorio.SucursalRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SucursalServicio {
    @Autowired
    private SucursalRepositorio sucursalRepositorio;
    @Autowired
    private EntidadBancariaRepositorio entidadBancariaRepositorio;
    @Autowired
    private LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    @Autowired
    private SucursalMapper sucursalMapper;

    // Crear sucursal
    @Transactional
    public SucursalDTO crearSucursal(SucursalDTO dto) {
        try {
            Sucursal entity = sucursalMapper.toEntity(dto);
            entity.setEstado(EstadoSucursalesEnum.ACTIVO.name());
            entity.setVersion(1L);
            entity.setFechaCreacion(new java.util.Date());
            // Set relaciones
            entity.setIdEntidadBancaria(entidadBancariaRepositorio.findById(dto.getIdEntidadBancaria()).orElse(null));
            entity.setIdLocacion(locacionGeograficaRepositorio.findById(dto.getIdLocacion()).orElse(null));
            return sucursalMapper.toDTO(sucursalRepositorio.save(entity));
        } catch (Exception e) {
            throw new CrearEntidadException("Sucursal", "Error al crear la sucursal: " + e.getMessage());
        }
    }

    // Modificar datos de sucursal (excepto entidad bancaria, fecha creación, estado, versión)
    @Transactional
    public SucursalDTO modificarSucursal(SucursalDTO dto) {
        Sucursal entity = sucursalRepositorio.findById(dto.getCodigo())
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada", 2, "Sucursal"));
        try {
            // Solo actualiza campos permitidos
            if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
            if (dto.getCorreoElectronico() != null) entity.setCorreoElectronico(dto.getCorreoElectronico());
            if (dto.getTelefono() != null) entity.setTelefono(dto.getTelefono());
            if (dto.getDireccionLinea1() != null) entity.setDireccionLinea1(dto.getDireccionLinea1());
            if (dto.getDireccionLinea2() != null) entity.setDireccionLinea2(dto.getDireccionLinea2());
            if (dto.getLatitud() != null) entity.setLatitud(dto.getLatitud());
            if (dto.getLongitud() != null) entity.setLongitud(dto.getLongitud());
            // Incrementa versión
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return sucursalMapper.toDTO(sucursalRepositorio.save(entity));
        } catch (Exception e) {
            throw new ActualizarEntidadException("Sucursal", "Error al modificar la sucursal: " + e.getMessage());
        }
    }

    // Modificar estado de sucursal
    @Transactional
    public SucursalDTO modificarEstadoSucursal(String codigo, EstadoSucursalesEnum estado) {
        Sucursal entity = sucursalRepositorio.findById(codigo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada", 2, "Sucursal"));
        try {
            entity.setEstado(estado.name());
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            return sucursalMapper.toDTO(sucursalRepositorio.save(entity));
        } catch (Exception e) {
            throw new ActualizarEntidadException("Sucursal", "Error al modificar el estado de la sucursal: " + e.getMessage());
        }
    }

    // Eliminación lógica (cambia estado a INACTIVO)
    @Transactional
    public void eliminarLogicoSucursal(String codigo) {
        Sucursal entity = sucursalRepositorio.findById(codigo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada", 2, "Sucursal"));
        try {
            entity.setEstado(EstadoSucursalesEnum.INACTIVO.name());
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            sucursalRepositorio.save(entity);
        } catch (Exception e) {
            throw new EliminarEntidadException("Sucursal", "Error al eliminar lógicamente la sucursal: " + e.getMessage());
        }
    }

    // Listar sucursales activas por locación
    public List<SucursalDTO> listarSucursalesActivasPorLocacion(Integer idLocacion) {
        List<Sucursal> sucursales = sucursalRepositorio.findByIdLocacion_IdLocacionAndEstado(idLocacion, EstadoSucursalesEnum.ACTIVO.name());
        return sucursales.stream().map(sucursalMapper::toDTO).collect(Collectors.toList());
    }
} 