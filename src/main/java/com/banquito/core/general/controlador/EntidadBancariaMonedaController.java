package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.EntidadBancariaDTO;
import com.banquito.core.general.dto.EntidadBancariaMonedaDTO;
import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.servicio.EntidadBancariaMonedaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entidades-bancarias")
@Tag(name = "EntidadBancariaMoneda", description = "Operaciones sobre Entidades Bancarias y la gestión de sus monedas")
public class EntidadBancariaMonedaController {

    private final EntidadBancariaMonedaServicio servicio;

    public EntidadBancariaMonedaController(EntidadBancariaMonedaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entidad bancaria por ID", description = "Busca y devuelve una entidad bancaria por su clave primaria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada")
    })
    public ResponseEntity<EntidadBancariaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(servicio.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar entidad bancaria", description = "Actualiza los datos de una entidad bancaria existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidad modificada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada")
    })
    public ResponseEntity<EntidadBancariaDTO> modificarEntidadBancaria(@PathVariable Integer id, @Valid @RequestBody EntidadBancariaDTO dto) {
        return ResponseEntity.ok(servicio.modificarEntidadBancaria(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminación lógica de entidad bancaria", description = "Inactiva una entidad bancaria y todas sus relaciones con monedas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entidad inactivada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada")
    })
    public ResponseEntity<Void> eliminarEntidadBancaria(@PathVariable Integer id) {
        servicio.eliminarEntidadBancaria(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activa/primera")
    @Operation(summary = "Obtener la primera entidad bancaria activa", description = "Busca y devuelve la primera entidad bancaria que encuentre con estado ACTIVO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidad encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontraron entidades activas")
    })
    public ResponseEntity<EntidadBancariaDTO> obtenerPrimeraEntidadBancariaActiva() {
        return ResponseEntity.ok(servicio.obtenerPrimeraEntidadBancariaActiva());
    }

    @PostMapping("/{idEntidad}/monedas/{idMoneda}")
    @Operation(summary = "Agregar una moneda a una entidad bancaria", description = "Crea una relación entre una entidad bancaria y una moneda. Ambas deben estar activas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moneda agregada exitosamente"),
            @ApiResponse(responseCode = "400", description = "La entidad o la moneda no están activas"),
            @ApiResponse(responseCode = "404", description = "Entidad o moneda no encontrada")
    })
    public ResponseEntity<EntidadBancariaMonedaDTO> agregarMonedaAEntidadBancaria(@PathVariable Integer idEntidad, @PathVariable String idMoneda) {
        return ResponseEntity.ok(servicio.agregarMonedaAEntidadBancaria(idEntidad, idMoneda));
    }

    @PatchMapping("/relaciones/{idRelacion}/estado")
    @Operation(summary = "Cambiar estado a la moneda de una entidad", description = "Cambia el estado (ACTIVO/INACTIVO) de la relación entre una entidad y una moneda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    })
    public ResponseEntity<EntidadBancariaMonedaDTO> cambiarEstadoMonedaDeEntidad(@PathVariable Integer idRelacion, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(servicio.cambiarEstadoMonedaDeEntidad(idRelacion, nuevoEstado));
    }

    @GetMapping("/{idEntidad}/monedas/activas")
    @Operation(summary = "Obtener monedas activas de una entidad", description = "Devuelve una lista de todas las monedas activas para una entidad bancaria específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado exitoso"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada")
    })
    public ResponseEntity<List<MonedaDTO>> obtenerMonedasActivasDeEntidad(@PathVariable Integer idEntidad) {
        return ResponseEntity.ok(servicio.obtenerMonedasActivasDeEntidad(idEntidad));
    }
}
