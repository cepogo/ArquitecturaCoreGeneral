package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.SucursalDTO;
import com.banquito.core.general.dto.SucursalCreacionDTO;
import com.banquito.core.general.dto.SucursalUpdateDTO;
import com.banquito.core.general.enums.EstadoSucursalesEnum;
import com.banquito.core.general.servicio.SucursalServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@Tag(name = "Sucursal", description = "Operaciones para sucursales bancarias")
public class SucursalController {

    private final SucursalServicio sucursalServicio;

    public SucursalController(SucursalServicio sucursalServicio) {
        this.sucursalServicio = sucursalServicio;
    }

    @PostMapping
    @Operation(summary = "Crear sucursal", description = "Crea una nueva sucursal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<SucursalDTO> crearSucursal(@Valid @RequestBody SucursalCreacionDTO dto) {
        return ResponseEntity.ok(sucursalServicio.crearSucursal(dto));
    }

    @PutMapping
    @Operation(summary = "Modificar datos de sucursal", description = "Modifica los datos permitidos de una sucursal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal modificada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<SucursalDTO> modificarSucursal(@Valid @RequestBody SucursalUpdateDTO dto) {
        return ResponseEntity.ok(sucursalServicio.modificarSucursal(dto));
    }

    @PutMapping("/{codigo}/estado")
    @Operation(summary = "Modificar estado de sucursal", description = "Modifica el estado de una sucursal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado de sucursal modificado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<SucursalDTO> modificarEstadoSucursal(@PathVariable String codigo, @RequestParam EstadoSucursalesEnum estado) {
        return ResponseEntity.ok(sucursalServicio.modificarEstadoSucursal(codigo, estado));
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminación lógica de sucursal", description = "Cambia el estado de la sucursal a INACTIVO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sucursal eliminada lógicamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Void> eliminarSucursal(@PathVariable String codigo) {
        sucursalServicio.eliminarLogicoSucursal(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activas/por-locacion/{idLocacion}")
    @Operation(summary = "Listar sucursales activas por locación", description = "Devuelve una lista de sucursales activas para una locación específica.")
    public ResponseEntity<List<SucursalDTO>> listarSucursalesActivasPorLocacion(@PathVariable Integer idLocacion) {
        return ResponseEntity.ok(sucursalServicio.listarSucursalesActivasPorLocacion(idLocacion));
    }
} 