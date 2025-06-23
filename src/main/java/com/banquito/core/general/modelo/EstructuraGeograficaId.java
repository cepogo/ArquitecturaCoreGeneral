package com.banquito.core.general.modelo;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class EstructuraGeograficaId {

    private String idPais;

    @Column(name = "codigo_nivel", nullable = false, precision = 1)
    private BigDecimal codigoNivel;

    public EstructuraGeograficaId() {
    }

    public EstructuraGeograficaId(String idPais, BigDecimal codigoNivel) {
        this.idPais = idPais;
        this.codigoNivel = codigoNivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstructuraGeograficaId that = (EstructuraGeograficaId) o;
        return Objects.equals(idPais, that.idPais) && Objects.equals(codigoNivel, that.codigoNivel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPais, codigoNivel);
    }
}
