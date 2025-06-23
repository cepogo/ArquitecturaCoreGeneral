package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.EstructuraGeograficaCreacionDTO;
import com.banquito.core.general.dto.EstructuraGeograficaDTO;
import com.banquito.core.general.dto.EstructuraGeograficaUpdateDTO;
import com.banquito.core.general.dto.LocacionGeograficaCreacionDTO;
import com.banquito.core.general.dto.LocacionGeograficaDTO;
import com.banquito.core.general.dto.LocacionGeograficaUpdateDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
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
    public ResponseEntity<EstructuraGeograficaDTO> crearEstructura(@Valid @RequestBody EstructuraGeograficaCreacionDTO dto) {
        return ResponseEntity.ok(servicio.crearEstructuraGeografica(dto));
    }

    @PutMapping("/estructura/{idPais}/{codigoNivel}")
    @Operation(summary = "Modificar estructura geográfica", description = "Modifica una estructura geográfica existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estructura modificada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estructura no encontrada")
    })
    public ResponseEntity<EstructuraGeograficaDTO> modificarEstructura(
            @PathVariable String idPais,
            @PathVariable Integer codigoNivel,
            @Valid @RequestBody EstructuraGeograficaUpdateDTO dto) {
        return ResponseEntity.ok(servicio.modificarEstructuraGeografica(idPais, codigoNivel, dto));
    }

    @PatchMapping("/estructura/{idPais}/{codigoNivel}/estado")
    @Operation(summary = "Cambiar estado de estructura geográfica", description = "Cambia el estado (ACTIVO/INACTIVO) de una estructura geográfica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estructura no encontrada")
    })
    public ResponseEntity<Void> cambiarEstadoEstructura(
            @PathVariable String idPais,
            @PathVariable Integer codigoNivel,
            @RequestParam EstadoGeneralEnum nuevoEstado) {
        servicio.cambiarEstadoEstructuraGeografica(idPais, codigoNivel, nuevoEstado);
        return ResponseEntity.ok().build();
    }

    // --- Locación Geográfica ---
    @PostMapping("/locacion")
    @Operation(summary = "Crear locación geográfica", description = "Crea una nueva locación geográfica. El idLocacionPadre se asigna automáticamente: " +
            "si codigoNivel=1 será null, si codigoNivel=2 usará idProvinciaPadre si se proporciona, o buscará la primera provincia del país.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Estructura geográfica o locación padre no encontrada")
    })
    public ResponseEntity<LocacionGeograficaDTO> crearLocacion(@Valid @RequestBody LocacionGeograficaCreacionDTO dto) {
        return ResponseEntity.ok(servicio.crearLocacionGeografica(dto));
    }

    @PutMapping("/locacion/{idLocacion}")
    @Operation(summary = "Modificar locación geográfica", description = "Modifica una locación geográfica existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locación modificada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Locación no encontrada")
    })
    public ResponseEntity<LocacionGeograficaDTO> modificarLocacion(
            @PathVariable Integer idLocacion,
            @Valid @RequestBody LocacionGeograficaUpdateDTO dto) {
        return ResponseEntity.ok(servicio.modificarLocacionGeografica(idLocacion, dto));
    }

    @PatchMapping("/locacion/{idLocacion}/estado")
    @Operation(summary = "Cambiar estado de locación geográfica", description = "Cambia el estado (ACTIVO/INACTIVO/MANTENIMIENTO) de una locación geográfica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Locación no encontrada")
    })
    public ResponseEntity<Void> cambiarEstadoLocacion(
            @PathVariable Integer idLocacion,
            @RequestParam EstadoLocacionesGeograficasEnum nuevoEstado) {
        servicio.cambiarEstadoLocacionGeografica(idLocacion, nuevoEstado);
        return ResponseEntity.ok().build();
    }

    // --- Listar locaciones activas por nivel ---
    @GetMapping("/locaciones/activas")
    @Operation(summary = "Listar locaciones activas por nivel", description = "Devuelve una lista de locaciones activas para un país y nivel.")
    public ResponseEntity<List<LocacionGeograficaDTO>> listarLocacionesActivasPorNivel(
            @RequestParam String idPais,
            @RequestParam Integer codigoNivel) {
        return ResponseEntity.ok(servicio.listarLocacionesActivasPorNivel(idPais, codigoNivel));
    }

    // --- Listar provincias de un país ---
    @GetMapping("/provincias")
    @Operation(summary = "Listar todas las provincias", description = "Devuelve una lista de todas las provincias activas de un país.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de provincias obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "País no encontrado")
    })
    public ResponseEntity<List<LocacionGeograficaDTO>> listarProvincias(
            @RequestParam String idPais) {
        return ResponseEntity.ok(servicio.listarProvincias(idPais));
    }

    // --- Listar cantones de una provincia específica ---
    @GetMapping("/cantones/{nombreProvincia}")
    @Operation(summary = "Listar cantones de una provincia", description = "Devuelve una lista de todos los cantones activos de una provincia específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cantones obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Provincia no encontrada")
    })
    public ResponseEntity<List<LocacionGeograficaDTO>> listarCantonesPorProvincia(
            @RequestParam String idPais,
            @PathVariable String nombreProvincia) {
        return ResponseEntity.ok(servicio.listarCantonesPorProvincia(idPais, nombreProvincia));
    }
} 