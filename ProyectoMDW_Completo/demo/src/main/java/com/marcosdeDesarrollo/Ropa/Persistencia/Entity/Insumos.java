
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Entity
@Table(name="insumos")
public class Insumos {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_insumo")
    private Integer idInsumo;
    @Column(name="nombre",nullable=false)
    private String nombre;
    private String Descripcion;
    @Value("{stock_Actual:0}")
    private Double stock_Actual;
    private String unidad_medida;
    @Enumerated(EnumType.STRING)
    private estado estado;
    private enum estado{
        Activo,Desactivo
    }
    @OneToMany(mappedBy="insumos")
    private List<Producto_Insumos> insumosP;
    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void IdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Double getStock_Actual() {
        return stock_Actual;
    }

    public void setStock_Actual(Double stock_Actual) {
        this.stock_Actual = stock_Actual;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public estado getEstado() {
        return estado;
    }

    public void setEstado(estado estado) {
        this.estado = estado;
    }
}