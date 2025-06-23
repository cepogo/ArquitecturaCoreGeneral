package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SucursalUpdateDTO {
    @NotNull
    @Size(max = 10)
    private String codigo;

    @Size(max = 30)
    private String nombre;

    @Size(max = 40)
    private String correoElectronico;

    @Size(max = 10)
    private String telefono;

    @Size(max = 150)
    private String direccionLinea1;

    @Size(max = 150)
    private String direccionLinea2;

    private BigDecimal latitud;
    private BigDecimal longitud;
} 