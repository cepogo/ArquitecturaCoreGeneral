package com.banquito.core.general.dto;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaisUpdateDTO {

    @Size(max = 50, message = "El nombre del país debe tener máximo 50 caracteres")
    private String nombre;

    @Size(max = 4, message = "El código de teléfono debe tener máximo 4 caracteres")
    private String codigoTelefono;

}
