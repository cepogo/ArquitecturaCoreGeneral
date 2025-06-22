package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.FeriadoDTO;
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
    @Operation(summary = "Crear feriado", description = "Crea un nuevo feriado por país o locación geográfica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feriado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<FeriadoDTO> crearFeriado(@Valid @RequestBody FeriadoDTO dto) {
        return ResponseEntity.ok(feriadoServicio.crearFeriado(dto));
    }

    @PutMapping
    @Operation(summary = "Modificar feriado", description = "Modifica solo la fecha y el nombre de un feriado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feriado modificado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Feriado no encontrado")
    })
    public ResponseEntity<FeriadoDTO> modificarFeriado(@Valid @RequestBody FeriadoDTO dto) {
        return ResponseEntity.ok(feriadoServicio.modificarFeriado(dto));
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
} 