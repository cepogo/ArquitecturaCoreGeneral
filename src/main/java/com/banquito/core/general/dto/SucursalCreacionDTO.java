package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SucursalCreacionDTO {
    @NotNull
    @Size(max = 10)
    private String codigo;

    @NotNull
    private Integer idEntidadBancaria;

    @NotNull
    private Integer idLocacion;

    @NotNull
    @Size(max = 30)
    private String nombre;

    @NotNull
    private Date fechaCreacion;

    @NotNull
    @Size(max = 40)
    private String correoElectronico;

    @NotNull
    @Size(max = 10)
    private String telefono;

    @NotNull
    @Size(max = 150)
    private String direccionLinea1;

    @Size(max = 150)
    private String direccionLinea2;

    @NotNull
    private BigDecimal latitud;

    @NotNull
    private BigDecimal longitud;
} 