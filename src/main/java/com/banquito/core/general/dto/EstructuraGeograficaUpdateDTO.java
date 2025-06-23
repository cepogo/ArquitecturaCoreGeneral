package com.banquito.core.general.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EstructuraGeograficaUpdateDTO {

    @Size(max = 25, message = "El nombre debe tener máximo 25 caracteres")
    private String nombre;
} 