package com.banquito.core.general.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EstructuraGeograficaCreacionDTO {

    @NotNull(message = "El ID del país no puede ser nulo")
    @Size(max = 2, message = "El ID del país debe tener máximo 2 caracteres")
    private String idPais;

    @NotNull(message = "El código de nivel no puede ser nulo")
    private Integer codigoNivel;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(max = 25, message = "El nombre debe tener máximo 25 caracteres")
    private String nombre;
} 