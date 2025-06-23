package com.banquito.core.general.dto;

import com.banquito.core.general.enums.TipoFeriadosEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class FeriadoCreacionDTO {

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo no puede ser nulo")
    private TipoFeriadosEnum tipo;

    @NotNull(message = "El ID del país no puede ser nulo")
    @Size(max = 2, message = "El ID del país debe tener máximo 2 caracteres")
    private String idPais;

    // Campo opcional para feriados locales
    private Integer idLocacion;
} 