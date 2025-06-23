package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.FeriadoCreacionDTO;
import com.banquito.core.general.dto.FeriadoDTO;
import com.banquito.core.general.dto.FeriadoUpdateDTO;
import com.banquito.core.general.servicio.FeriadoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feriados")
@Tag(name = "Feriado", description = "Operaciones para feriados")
public class FeriadoController {

    private final FeriadoServicio feriadoServicio;

    public FeriadoController(FeriadoServicio feriadoServicio) {
        this.feriadoServicio = feriadoServicio;
    }

    @PostMapping
    @Operation(summary = "Crear feriado", description = "Crea un nuevo feriado. Para feriados nacionales no enviar idLocacion, para locales es obligatorio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feriado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "País o locación no encontrada")
    })
    public ResponseEntity<FeriadoDTO> crearFeriado(@Valid @RequestBody FeriadoCreacionDTO dto) {
        return ResponseEntity.ok(feriadoServicio.crearFeriado(dto));
    }

    @PutMapping("/{idFeriado}")
    @Operation(summary = "Modificar feriado", description = "Modifica solo la fecha y el nombre de un feriado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feriado modificado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Feriado no encontrado")
    })
    public ResponseEntity<FeriadoDTO> modificarFeriado(
            @PathVariable Integer idFeriado,
            @Valid @RequestBody FeriadoUpdateDTO dto) {
        return ResponseEntity.ok(feriadoServicio.modificarFeriado(idFeriado, dto));
    }

    @DeleteMapping("/{idFeriado}")
    @Operation(summary = "Eliminación lógica de feriado", description = "Cambia el estado del feriado a INACTIVO.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Feriado eliminado lógicamente"),
            @ApiResponse(responseCode = "404", description = "Feriado no encontrado")
    })
    public ResponseEntity<Void> eliminarFeriado(@PathVariable Integer idFeriado) {
        feriadoServicio.eliminarLogicoFeriado(idFeriado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar feriados activos por año y locación", description = "Devuelve una lista de feriados activos para un año y locación específica.")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosActivosPorAnioYLocacion(
            @RequestParam int anio,
            @RequestParam Integer idLocacion) {
        return ResponseEntity.ok(feriadoServicio.listarFeriadosActivosPorAnioYLocacion(anio, idLocacion));
    }

    // Listar todos los feriados (nacionales y locales) de un año
    @GetMapping
    @Operation(summary = "Listar todos los feriados de un año", description = "Devuelve todos los feriados (nacionales y locales) activos de un año.")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosPorAnio(@RequestParam int anio) {
        return ResponseEntity.ok(feriadoServicio.listarFeriadosPorAnio(anio));
    }

    // Listar solo feriados nacionales de un año
    @GetMapping("/nacionales")
    @Operation(summary = "Listar feriados nacionales de un año", description = "Devuelve solo los feriados nacionales activos de un año.")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosNacionalesPorAnio(@RequestParam int anio) {
        return ResponseEntity.ok(feriadoServicio.listarFeriadosNacionalesPorAnio(anio));
    }

    // Listar solo feriados locales de un año, opcionalmente filtrados por locación
    @GetMapping("/locales")
    @Operation(summary = "Listar feriados locales de un año", description = "Devuelve solo los feriados locales activos de un año. Puede filtrar por idLocacion.")
    public ResponseEntity<List<FeriadoDTO>> listarFeriadosLocalesPorAnio(
            @RequestParam int anio,
            @RequestParam(required = false) Integer idLocacion) {
        return ResponseEntity.ok(feriadoServicio.listarFeriadosLocalesPorAnio(anio, idLocacion));
    }
} 