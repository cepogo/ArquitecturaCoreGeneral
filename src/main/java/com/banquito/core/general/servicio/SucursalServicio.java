package com.banquito.core.general.servicio;

import com.banquito.core.general.dto.SucursalDTO;
import com.banquito.core.general.dto.SucursalCreacionDTO;
import com.banquito.core.general.dto.SucursalUpdateDTO;
import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.excepcion.ActualizarEntidadException;
import com.banquito.core.general.excepcion.CrearEntidadException;
import com.banquito.core.general.excepcion.EntidadNoEncontradaException;
import com.banquito.core.general.mapper.SucursalMapper;
import com.banquito.core.general.modelo.Sucursal;
import com.banquito.core.general.repositorio.EntidadBancariaRepositorio;
import com.banquito.core.general.repositorio.LocacionGeograficaRepositorio;
import com.banquito.core.general.repositorio.SucursalRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucursalServicio {
    private final SucursalRepositorio sucursalRepositorio;
    private final EntidadBancariaRepositorio entidadBancariaRepositorio;
    private final LocacionGeograficaRepositorio locacionGeograficaRepositorio;
    private final SucursalMapper sucursalMapper;

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

    // Crear sucursal con DTO de creación
    @Transactional
    public SucursalDTO crearSucursal(SucursalCreacionDTO dto) {
        log.info("Iniciando creación de sucursal con código: {}", dto.getCodigo());
        try {
            Sucursal entity = new Sucursal();
            entity.setCodigo(dto.getCodigo());
            entity.setNombre(dto.getNombre());
            entity.setFechaCreacion(dto.getFechaCreacion());
            entity.setCorreoElectronico(dto.getCorreoElectronico());
            entity.setTelefono(dto.getTelefono());
            entity.setDireccionLinea1(dto.getDireccionLinea1());
            entity.setDireccionLinea2(dto.getDireccionLinea2());
            entity.setLatitud(dto.getLatitud());
            entity.setLongitud(dto.getLongitud());
            entity.setEstado(EstadoSucursalesEnum.ACTIVO.name());
            entity.setVersion(1L);
            entity.setIdEntidadBancaria(entidadBancariaRepositorio.findById(dto.getIdEntidadBancaria()).orElse(null));
            entity.setIdLocacion(locacionGeograficaRepositorio.findById(dto.getIdLocacion()).orElse(null));
            Sucursal savedEntity = sucursalRepositorio.save(entity);
            log.info("Sucursal con código {} creada exitosamente.", savedEntity.getCodigo());
            return sucursalMapper.toDTO(savedEntity);
        } catch (Exception e) {
            log.error("Error al crear sucursal: {}", e.getMessage(), e);
            throw new CrearEntidadException("Sucursal", "Error al crear la sucursal: " + e.getMessage());
        }
    }

    // Modificar datos de sucursal (solo campos permitidos, usando SucursalUpdateDTO)
    @Transactional
    public SucursalDTO modificarSucursal(SucursalUpdateDTO dto) {
        log.info("Iniciando modificación de sucursal con código: {}", dto.getCodigo());
        Sucursal entity = sucursalRepositorio.findById(dto.getCodigo())
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada", 2, "Sucursal"));
        try {
            if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
            if (dto.getCorreoElectronico() != null) entity.setCorreoElectronico(dto.getCorreoElectronico());
            if (dto.getTelefono() != null) entity.setTelefono(dto.getTelefono());
            if (dto.getDireccionLinea1() != null) entity.setDireccionLinea1(dto.getDireccionLinea1());
            if (dto.getDireccionLinea2() != null) entity.setDireccionLinea2(dto.getDireccionLinea2());
            if (dto.getLatitud() != null) entity.setLatitud(dto.getLatitud());
            if (dto.getLongitud() != null) entity.setLongitud(dto.getLongitud());
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            Sucursal savedEntity = sucursalRepositorio.save(entity);
            log.info("Sucursal con código {} modificada exitosamente.", savedEntity.getCodigo());
            return sucursalMapper.toDTO(savedEntity);
        } catch (Exception e) {
            log.error("Error al modificar sucursal con código {}: {}", dto.getCodigo(), e.getMessage(), e);
            throw new ActualizarEntidadException("Sucursal", "Error al modificar la sucursal: " + e.getMessage());
        }
    }

    // Modificar estado de sucursal
    @Transactional
    public SucursalDTO modificarEstadoSucursal(String codigo, EstadoSucursalesEnum estado) {
        log.info("Iniciando modificación de sucursal con código: {}", codigo);
        Sucursal entity = sucursalRepositorio.findById(codigo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada", 2, "Sucursal"));
        try {
            entity.setEstado(estado.name());
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            Sucursal savedEntity = sucursalRepositorio.save(entity);
            log.info("Estado de sucursal {} modificado exitosamente.", savedEntity.getCodigo());
            return sucursalMapper.toDTO(savedEntity);
        } catch (Exception e) {
            log.error("Error al modificar estado de sucursal {}: {}", codigo, e.getMessage(), e);
            throw new ActualizarEntidadException("Sucursal", "Error al modificar el estado de la sucursal: " + e.getMessage());
        }
    }

    // Cambiar estado de sucursal (ACTIVO/INACTIVO/CONSTRUCCION/REMODELACION)
    @Transactional
    public void cambiarEstadoSucursal(String codigo, EstadoSucursalesEnum nuevoEstado) {
        log.info("Cambiando estado de sucursal {} a {}", codigo, nuevoEstado.name());
        Sucursal entity = sucursalRepositorio.findById(codigo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Sucursal no encontrada", 2, "Sucursal"));
        try {
            // Solo validar para cambio a ACTIVO
            if (nuevoEstado == EstadoSucursalesEnum.ACTIVO) {
                if (entity.getIdEntidadBancaria() == null || entity.getIdLocacion() == null) {
                    log.warn("No se puede activar la sucursal porque no tiene entidad bancaria o locación asociada.");
                    throw new ActualizarEntidadException("Sucursal", "No se puede activar la sucursal porque no tiene entidad bancaria o locación asociada.");
                }
                // Verificar que la entidad bancaria esté ACTIVA
                if (!"ACTIVO".equals(entity.getIdEntidadBancaria().getEstado().name())) {
                    log.warn("No se puede activar la sucursal porque la entidad bancaria '{}' está inactiva.", entity.getIdEntidadBancaria().getNombre());
                    throw new ActualizarEntidadException("Sucursal", "No se puede activar la sucursal porque la entidad bancaria '" + entity.getIdEntidadBancaria().getNombre() + "' está inactiva.");
                }
                // Verificar que la locación geográfica esté ACTIVA
                if (!"ACTIVO".equals(entity.getIdLocacion().getEstado().name())) {
                    log.warn("No se puede activar la sucursal porque la locación geográfica '{}' está inactiva.", entity.getIdLocacion().getNombre());
                    throw new ActualizarEntidadException("Sucursal", "No se puede activar la sucursal porque la locación geográfica '" + entity.getIdLocacion().getNombre() + "' está inactiva.");
                }
            }
            entity.setEstado(nuevoEstado.name());
            entity.setVersion(entity.getVersion() == null ? 1L : entity.getVersion() + 1L);
            sucursalRepositorio.save(entity);
            log.info("Estado de sucursal {} cambiado a {}.", codigo, nuevoEstado.name());
        } catch (Exception e) {
            log.error("Error al cambiar estado de sucursal {}: {}", codigo, e.getMessage(), e);
            throw new ActualizarEntidadException("Sucursal", "Error al cambiar el estado de la sucursal: " + e.getMessage());
        }
    }

    // Listar sucursales activas por locación
    public List<SucursalDTO> listarSucursalesActivasPorLocacion(Integer idLocacion) {
        log.info("Listando sucursales activas para locación ID: {}", idLocacion);
        List<Sucursal> sucursales = sucursalRepositorio.findByIdLocacion_IdLocacionAndEstado(idLocacion, EstadoSucursalesEnum.ACTIVO.name());
        log.info("Se encontraron {} sucursales activas para la locación ID: {}", sucursales.size(), idLocacion);
        return sucursales.stream().map(sucursalMapper::toDTO).collect(Collectors.toList());
    }

    // Listar todas las sucursales
    public List<SucursalDTO> listarTodasLasSucursales() {
        log.info("Listando todas las sucursales");
        List<Sucursal> sucursales = sucursalRepositorio.findAll();
        log.info("Se encontraron {} sucursales en total", sucursales.size());
        return sucursales.stream().map(sucursalMapper::toDTO).collect(Collectors.toList());
    }
} 