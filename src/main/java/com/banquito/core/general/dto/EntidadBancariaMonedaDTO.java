package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class EntidadBancariaMonedaDTO {

    private Integer idEntidadBancariaMoneda;

    @Setter
    @Getter
    @NotNull(message = "El ID de la entidad bancaria no puede ser nulo")
    private Integer idEntidadBancaria;

    @NotNull(message = "El ID de la moneda no puede ser nulo")
    @Size(max = 3, message = "El ID de la moneda debe tener máximo 3 caracteres")
    private String idMoneda;

    @NotNull(message = "El estado no puede ser nulo")
    @Size(max = 15, message = "El estado debe tener máximo 15 caracteres")
    private EstadoGeneralEnum estado;

}
