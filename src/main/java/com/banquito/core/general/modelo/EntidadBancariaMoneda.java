package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    @ManyToOne
    @JoinColumn(name = "id_moneda", referencedColumnName = "id_moneda")
    private Moneda idMoneda;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoGeneralEnum estado;

    @Column(name = "version", nullable = false, precision = 9)
    private Long version;

    public EntidadBancariaMoneda() {
    }

    public EntidadBancariaMoneda(Integer idEntidadBancariaMoneda) {
        this.idEntidadBancariaMoneda = idEntidadBancariaMoneda;
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