package com.banquito.core.general.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "entidades_bancarias_monedas")
public class EntidadBancariaMoneda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entidad_bancaria_moneda", nullable = false)
    private Integer idEntidadBancariaMoneda;

    @ManyToOne
    @JoinColumn(name = "id_entidad_bancaria", referencedColumnName = "id_entidad_bancaria")
    private EntidadBancaria idEntidadBancaria;

    @Column(name = "id_moneda", nullable = false, length = 3)
    private String idMoneda;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @Column(name = "version", nullable = false, precision = 9)
    private BigDecimal version;

    // Getters y Setters
    public Integer getIdEntidadBancariaMoneda() {
        return idEntidadBancariaMoneda;
    }

    public void setIdEntidadBancariaMoneda(Integer idEntidadBancariaMoneda) {
        this.idEntidadBancariaMoneda = idEntidadBancariaMoneda;
    }

    public EntidadBancaria getIdEntidadBancaria() {
        return idEntidadBancaria;
    }

    public void setIdEntidadBancaria(EntidadBancaria idEntidadBancaria) {
        this.idEntidadBancaria = idEntidadBancaria;
    }

    public String getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(String idMoneda) {
        this.idMoneda = idMoneda;
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
        result = prime * result + ((idEntidadBancariaMoneda == null) ? 0 : idEntidadBancariaMoneda.hashCode());
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
        EntidadBancariaMoneda other = (EntidadBancariaMoneda) obj;
        if (idEntidadBancariaMoneda == null) {
            if (other.idEntidadBancariaMoneda != null)
                return false;
        } else if (!idEntidadBancariaMoneda.equals(other.idEntidadBancariaMoneda))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EntidadesBancariasMonedas [idEntidadBancariaMoneda=" + idEntidadBancariaMoneda + ", idEntidadBancaria="
                + idEntidadBancaria + ", idMoneda=" + idMoneda + ", estado=" + estado + ", version=" + version + "]";
    }

} 