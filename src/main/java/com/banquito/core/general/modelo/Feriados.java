package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoGeneralEnum;
import com.banquito.core.general.enums.TipoFeriadosEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "feriados")
public class Feriados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feriado", nullable = false)
    private Integer idFeriado;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "id_pais", referencedColumnName = "id_pais")
    private Paises idPais;

    @ManyToOne
    @JoinColumn(name = "id_locacion", referencedColumnName = "id_locacion")
    private LocacionesGeograficas idLocacion;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 15)
    private TipoFeriadosEnum tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoGeneralEnum estado = EstadoGeneralEnum.ACTIVO;

    @ColumnDefault("0")
    @Column(name = "version", nullable = false, precision = 9)
    private BigDecimal version;

    public Feriados() {
    }

    public Feriados(Integer idFeriado) {
        this.idFeriado = idFeriado;
    }

    public Integer getIdFeriado() {
        return idFeriado;
    }

    public void setIdFeriado(Integer idFeriado) {
        this.idFeriado = idFeriado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Paises getIdPais() {
        return idPais;
    }

    public void setIdPais(Paises idPais) {
        this.idPais = idPais;
    }

    public LocacionesGeograficas getIdLocacion() {
        return idLocacion;
    }

    public void setIdLocacion(LocacionesGeograficas idLocacion) {
        this.idLocacion = idLocacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoFeriadosEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoFeriadosEnum tipo) {
        this.tipo = tipo;
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
        Feriados feriados = (Feriados) o;
        return Objects.equals(idFeriado, feriados.idFeriado);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idFeriado);
    }

    @Override
    public String toString() {
        return "Feriados{" +
                "idFeriado=" + idFeriado +
                ", fecha=" + fecha +
                ", idPais=" + idPais +
                ", idLocacion=" + idLocacion +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", estado=" + estado +
                ", version=" + version +
                '}';
    }
}
