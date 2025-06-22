package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EntidadBancariaUpdateDTO {

    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @Size(max = 6, message = "El código local debe tener máximo 6 caracteres")
    private String codigoLocal;

    @Size(max = 20, message = "El código internacional debe tener máximo 20 caracteres")
    private String codigoInternacional;

    @Size(max = 15, message = "El estado debe tener máximo 15 caracteres")
    private EstadoGeneralEnum estado;
}
