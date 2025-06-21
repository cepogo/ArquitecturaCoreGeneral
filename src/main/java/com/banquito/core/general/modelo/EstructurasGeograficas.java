package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "estructuras_geograficas")
public class EstructurasGeograficas {

    @EmbeddedId
    private EstructurasGeograficasId id;

    @ManyToOne
    @JoinColumn(name = "id_pais", referencedColumnName = "id_pais", nullable = false)
    private Paises idPais;

    @Column(name = "nombre", length = 25, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15, nullable = false)
    private EstadoGeneralEnum estado = EstadoGeneralEnum.ACTIVO;

    @Column(name = "version", precision = 9, nullable = false)
    private BigDecimal version;

    public EstructurasGeograficas() {
    }

    public EstructurasGeograficas(EstructurasGeograficasId id) {
        this.id = id;
    }

    public EstructurasGeograficasId getId() {
        return id;
    }

    public void setId(EstructurasGeograficasId id) {
        this.id = id;
    }

    public Paises getIdPais() {
        return idPais;
    }

    public void setIdPais(Paises idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EstadoGeneralEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoGeneralEnum estado) {
        this.estado = estado;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EstructurasGeograficas that = (EstructurasGeograficas) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EstructurasGeograficas{" +
                "id=" + id +
                ", idPais=" + idPais +
                ", nombre='" + nombre + '\'' +
                ", estado='" + estado + '\'' +
                ", version=" + version +
                '}';
    }
}
