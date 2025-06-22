package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonedaDTO {
    @NotNull(message = "El ID de la moneda no puede ser nulo")
    @Size(max = 3, message = "El ID de la moneda debe tener máximo 3 caracteres")
    private String idMoneda;

    @Size(max = 2, message = "El ID del país debe tener máximo 2 caracteres")
    private String idPais;

    @NotNull(message = "El nombre de la moneda no puede ser nulo")
    @Size(max = 50, message = "El nombre de la moneda debe tener máximo 50 caracteres")
    private String nombre;

    @NotNull(message = "El símbolo no puede ser nulo")
    @Size(max = 5, message = "El símbolo debe tener máximo 5 caracteres")
    private String simbolo;

    @NotNull(message = "El estado no puede ser nulo")
    @Size(max = 15, message = "El estado debe tener máximo 15 caracteres")
    private EstadoGeneralEnum estado;

    private BigDecimal version;
}
