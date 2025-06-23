package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class FeriadoUpdateDTO {

    @NotNull(message = "La fecha no puede ser nula")
    private Date fecha;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;
} 