package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaisDTO {

    @NotNull(message = "El ID del país no puede ser nulo")
    @Size(max = 2, message = "El ID del país debe tener máximo 2 caracteres")
    private String idPais;

    @NotNull(message = "El nombre del país no puede ser nulo")
    @Size(max = 50, message = "El nombre del país debe tener máximo 50 caracteres")
    private String nombre;

    @NotNull(message = "El código de teléfono no puede ser nulo")
    @Size(max = 4, message = "El código de teléfono debe tener máximo 4 caracteres")
    private String codigoTelefono;

    @NotNull(message = "El estado no puede ser nulo")
    private EstadoGeneralEnum estado;

    private Long version;
}
