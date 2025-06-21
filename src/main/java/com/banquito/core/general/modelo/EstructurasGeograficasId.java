package com.banquito.core.general.modelo;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.util.Objects;

public class EstructurasGeograficasId {

    @Column(name = "id_pais", nullable = false, length = 2)
    private String idPais;

    @Column(name = "codigo_nivel", nullable = false, precision = 1)
    private BigDecimal codigoNivel;

    public EstructurasGeograficasId() {
    }

    public EstructurasGeograficasId(String idPais, BigDecimal codigoNivel) {
        this.idPais = idPais;
        this.codigoNivel = codigoNivel;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public BigDecimal getCodigoNivel() {
        return codigoNivel;
    }

    public void setCodigoNivel(BigDecimal codigoNivel) {
        this.codigoNivel = codigoNivel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EstructurasGeograficasId that = (EstructurasGeograficasId) o;
        return Objects.equals(idPais, that.idPais) && Objects.equals(codigoNivel, that.codigoNivel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPais, codigoNivel);
    }
}
