package com.banquito.core.general.controlador;

import com.banquito.core.general.dto.MonedaDTO;
import com.banquito.core.general.dto.MonedaUpdateDTO;
import com.banquito.core.general.dto.PaisDTO;
import com.banquito.core.general.dto.PaisUpdateDTO;
import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.servicio.PaisMonedaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paisesMonedas")
@Tag(name = "PaisMoneda", description = "Operaciones relacionadas con Países y sus Monedas")
public class PaisMonedaController {

    private final PaisMonedaServicio paisMonedaServicio;

    public PaisMonedaController(PaisMonedaServicio paisMonedaServicio) {
        this.paisMonedaServicio = paisMonedaServicio;
    }

    @PostMapping("/pais/crearPais")
    @Operation(summary = "Crear un nuevo país", description = "Crea un nuevo país en el sistema. Verifica que el nombre no esté duplicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o nombre de país duplicado")
    })
    public ResponseEntity<PaisDTO> crearPais(@Valid @RequestBody PaisDTO paisDTO) {
        return ResponseEntity.ok(paisMonedaServicio.crearPais(paisDTO));
    }

    @GetMapping("/pais/activos")
    @Operation(summary = "Listar todos los países activos", description = "Devuelve una lista de todos los países con estado ACTIVO, ordenados por nombre.")
    public ResponseEntity<List<PaisDTO>> listarPaisesActivos() {
        return ResponseEntity.ok(paisMonedaServicio.listarPaisesActivos());
    }

    @PatchMapping("/paises/{idPais}")
    @Operation(summary = "Actualización parcial de un país", description = "Aplica cambios parciales a un país. Solo los campos incluidos en el body serán modificados. Los campos no incluidos o nulos serán ignorados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "País no encontrado")
    })
    public ResponseEntity<PaisDTO> actualizarParcialmentePais(@PathVariable String idPais, @RequestBody PaisUpdateDTO paisUpdateDTO) {
        return ResponseEntity.ok(paisMonedaServicio.actualizarParcialmentePais(idPais, paisUpdateDTO));
    }

    @PatchMapping("/paises/{idPais}/estado")
    @Operation(summary = "Cambiar estado de un país", description = "Cambia el estado de un país. Si se inactiva, también se inactivan sus monedas, feriados y relaciones asociadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del país cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "País no encontrado")
    })
    public ResponseEntity<PaisDTO> cambiarEstadoPais(@PathVariable String idPais, @RequestParam EstadoGeneralEnum nuevoEstado) {
        return ResponseEntity.ok(paisMonedaServicio.cambiarEstadoPais(idPais, nuevoEstado));
    }

    @PostMapping("/pais/{idPais}/monedas")
    @Operation(summary = "Crear una moneda para un país", description = "Crea una nueva moneda y la asocia a un país. Verifica que el país esté activo y que el nombre de la moneda no esté duplicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moneda creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, país inactivo o nombre de moneda duplicado"),
            @ApiResponse(responseCode = "404", description = "País no encontrado")
    })
    public ResponseEntity<MonedaDTO> crearMonedaPorPais(@PathVariable String idPais, @Valid @RequestBody MonedaDTO monedaDTO) {
        return ResponseEntity.ok(paisMonedaServicio.crearMonedaPorPais(idPais, monedaDTO));
    }

    @GetMapping("/monedas/activas")
    @Operation(summary = "Listar todas las monedas activas", description = "Devuelve una lista de todas las monedas con estado ACTIVO.")
    public ResponseEntity<List<MonedaDTO>> listarMonedasActivas() {
        return ResponseEntity.ok(paisMonedaServicio.listarMonedasActivas());
    }

    @PatchMapping("/monedas/{idMoneda}")
    @Operation(summary = "Actualización parcial de una moneda", description = "Aplica cambios parciales a una moneda. Solo los campos incluidos en el body serán modificados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moneda actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Moneda no encontrada")
    })
    public ResponseEntity<MonedaDTO> actualizarParcialmenteMoneda(@PathVariable String idMoneda, @RequestBody MonedaUpdateDTO monedaDTO) {
        return ResponseEntity.ok(paisMonedaServicio.actualizarParcialmenteMoneda(idMoneda, monedaDTO));
    }

    @PatchMapping("/monedas/{idMoneda}/estado")
    @Operation(summary = "Cambiar estado de una moneda", description = "Cambia el estado de una moneda. Si se inactiva, también se inactivan sus relaciones con entidades bancarias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de la moneda cambiado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Moneda no encontrada")
    })
    public ResponseEntity<MonedaDTO> cambiarEstadoMoneda(@PathVariable String idMoneda, @RequestParam EstadoGeneralEnum nuevoEstado) {
        return ResponseEntity.ok(paisMonedaServicio.cambiarEstadoMoneda(idMoneda, nuevoEstado));
    }
}

