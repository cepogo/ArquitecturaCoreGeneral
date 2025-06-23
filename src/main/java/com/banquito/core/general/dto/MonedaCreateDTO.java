package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class MonedaCreateDTO {

    @NotNull(message = "El ID de la moneda no puede ser nulo")
    @Size(max = 3, message = "El ID de la moneda debe tener máximo 3 caracteres")
    private String idMoneda;

    @NotNull(message = "El nombre de la moneda no puede ser nulo")
    @Size(max = 50, message = "El nombre de la moneda debe tener máximo 50 caracteres")
    private String nombre;

    @NotNull(message = "El símbolo no puede ser nulo")
    @Size(max = 5, message = "El símbolo debe tener máximo 5 caracteres")
    private String simbolo;


}
