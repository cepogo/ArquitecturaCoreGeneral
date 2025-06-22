package com.banquito.core.general.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "monedas")
public class Moneda {
    @Id
    @Column(name = "id_moneda", nullable = false, length = 3)
    private String idMoneda;

    @Column(name = "id_pais", length = 2)
    private String idPais;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "simbolo", nullable = false, length = 5)
    private String simbolo;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "version", nullable = false, precision = 9)
    private BigDecimal version;

    // Getters y Setters
    public String getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(String idMoneda) {
        this.idMoneda = idMoneda;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idMoneda == null) ? 0 : idMoneda.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Moneda other = (Moneda) obj;
        if (idMoneda == null) {
            if (other.idMoneda != null)
                return false;
        } else if (!idMoneda.equals(other.idMoneda))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Monedas [idMoneda=" + idMoneda + ", idPais=" + idPais + ", nombre=" + nombre + ", simbolo=" + simbolo
                + ", estado=" + estado + ", version=" + version + "]";
    }
} 