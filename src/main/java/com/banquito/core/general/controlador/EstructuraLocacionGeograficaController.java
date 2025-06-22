package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.EstructuraGeograficaDTO;
import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.servicio.EstructuraLocacionGeograficaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estructuras-locaciones")
@Tag(name = "EstructuraLocacionGeografica", description = "Operaciones para estructuras y locaciones geográficas")
public class EstructuraLocacionGeograficaController {

    private final EstructuraLocacionGeograficaServicio servicio;

    public EstructuraLocacionGeograficaController(EstructuraLocacionGeograficaServicio servicio) {
        this.servicio = servicio;
    }

    // --- Estructura Geográfica ---
    @PostMapping("/estructura")
    @Operation(summary = "Crear estructura geográfica", description = "Crea una nueva estructura geográfica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estructura creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EstructuraGeograficaDTO> crearEstructura(@Valid @RequestBody EstructuraGeograficaDTO dto) {
        return ResponseEntity.ok(servicio.crearEstructuraGeografica(dto));
    }

    @PutMapping("/estructura")
    @Operation(summary = "Modificar estructura geográfica", description = "Modifica una estructura geográfica existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estructura modificada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estructura no encontrada")
    })
    public ResponseEntity<EstructuraGeograficaDTO> modificarEstructura(@Valid @RequestBody EstructuraGeograficaDTO dto) {
        return ResponseEntity.ok(servicio.modificarEstructuraGeografica(dto));
    }

    @DeleteMapping("/estructura/{idPais}/{codigoNivel}")
    @Operation(summary = "Eliminación lógica de estructura geográfica", description = "Cambia el estado de la estructura a INACTIVO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estructura eliminada lógicamente"),
            @ApiResponse(responseCode = "404", description = "Estructura no encontrada")
    })
    public ResponseEntity<Void> eliminarEstructura(@PathVariable String idPais, @PathVariable Integer codigoNivel) {
        servicio.eliminarLogicoEstructuraGeografica(idPais, codigoNivel);
        return ResponseEntity.noContent().build();
    }

    // --- Locación Geográfica ---
    @PostMapping("/locacion")
    @Operation(summary = "Crear locación geográfica", description = "Crea una nueva locación geográfica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<LocacionGeograficaDTO> crearLocacion(@Valid @RequestBody LocacionGeograficaDTO dto) {
        return ResponseEntity.ok(servicio.crearLocacionGeografica(dto));
    }

    @PutMapping("/locacion")
    @Operation(summary = "Modificar locación geográfica", description = "Modifica una locación geográfica existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locación modificada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Locación no encontrada")
    })
    public ResponseEntity<LocacionGeograficaDTO> modificarLocacion(@Valid @RequestBody LocacionGeograficaDTO dto) {
        return ResponseEntity.ok(servicio.modificarLocacionGeografica(dto));
    }

    @DeleteMapping("/locacion/{idLocacion}")
    @Operation(summary = "Eliminación lógica de locación geográfica", description = "Cambia el estado de la locación a INACTIVO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Locación eliminada lógicamente"),
            @ApiResponse(responseCode = "404", description = "Locación no encontrada")
    })
    public ResponseEntity<Void> eliminarLocacion(@PathVariable Integer idLocacion) {
        servicio.eliminarLogicoLocacionGeografica(idLocacion);
        return ResponseEntity.noContent().build();
    }

    // --- Listar locaciones activas por nivel ---
    @GetMapping("/locaciones/activas")
    @Operation(summary = "Listar locaciones activas por nivel", description = "Devuelve una lista de locaciones activas para un país y nivel.")
    public ResponseEntity<List<LocacionGeograficaDTO>> listarLocacionesActivasPorNivel(
            @RequestParam String idPais,
            @RequestParam Integer codigoNivel) {
        return ResponseEntity.ok(servicio.listarLocacionesActivasPorNivel(idPais, codigoNivel));
    }
} 