package com.banquito.core.general.modelo;

import com.banquito.core.general.enums.EstadoLocacionesGeograficasEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "locaciones_geograficas")
public class LocacionGeografica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_locacion", nullable = false)
    private Integer idLocacion;

    @ManyToOne
    @JoinColumn(name = "id_locacion_padre", referencedColumnName = "id_locacion")
    private LocacionGeografica idLocacionPadre;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "id_pais", referencedColumnName = "id_pais", nullable = false),
            @JoinColumn(name = "codigo_nivel", referencedColumnName = "codigo_nivel", nullable = false)
    })
    private EstructuraGeografica estructuraGeografica;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "codigo_telefono_area", length = 3, nullable = false)
    private String codigoTelefonoArea;

    @Column(name = "codigo_geografico", length = 20, nullable = false)
    private String codigoGeografico;

    @Column(name = "codigo_postal", length = 6, nullable = false)
    private String codigoPostal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 15, nullable = false)
    private EstadoLocacionesGeograficasEnum estado;

    @Column(name = "version", nullable = false)
    private Long version;

    public LocacionGeografica() {
    }

    public LocacionGeografica(Integer idLocacion) {
        this.idLocacion = idLocacion;
    }

    public Integer getIdLocacion() {
        return idLocacion;
    }

    public void setIdLocacion(Integer idLocacion) {
        this.idLocacion = idLocacion;
    }

    public LocacionGeografica getIdLocacionPadre() {
        return idLocacionPadre;
    }

    public void setIdLocacionPadre(LocacionGeografica idLocacionPadre) {
        this.idLocacionPadre = idLocacionPadre;
    }

    public EstructuraGeografica getEstructuraGeografica() {
        return estructuraGeografica;
    }

    public void setEstructuraGeografica(EstructuraGeografica estructuraGeografica) {
        this.estructuraGeografica = estructuraGeografica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoTelefonoArea() {
        return codigoTelefonoArea;
    }

    public void setCodigoTelefonoArea(String codigoTelefonoArea) {
        this.codigoTelefonoArea = codigoTelefonoArea;
    }

    public String getCodigoGeografico() {
        return codigoGeografico;
    }

    public void setCodigoGeografico(String codigoGeografico) {
        this.codigoGeografico = codigoGeografico;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public EstadoLocacionesGeograficasEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoLocacionesGeograficasEnum estado) {
        this.estado = estado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocacionGeografica that = (LocacionGeografica) o;
        return Objects.equals(idLocacion, that.idLocacion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idLocacion);
    }

    @Override
    public String toString() {
        return "LocacionGeografica{" +
                "idLocacion=" + idLocacion +
                ", idLocacionPadre=" + idLocacionPadre +
                ", estructuraGeografica=" + estructuraGeografica +
                ", nombre='" + nombre + '\'' +
                ", codigoTelefonoArea='" + codigoTelefonoArea + '\'' +
                ", codigoGeografico='" + codigoGeografico + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", estado=" + estado +
                ", version=" + version +
                '}';
    }
}
