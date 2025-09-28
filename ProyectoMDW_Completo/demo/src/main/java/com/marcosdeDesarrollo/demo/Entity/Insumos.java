package com.marcosdeDesarrollo.demo.Entity;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name="insumos")
public class Insumos {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_insumo")
    private Integer id_insumo;
    @Column(name="nombre",nullable=false)
    private String nombre;
    private String Descripcion;
    @Value("{stock_Actual:0}")
    private double stock_Actual;
    private String unidad_medida;
    @Enumerated(EnumType.STRING)
    private estado estado;
    private enum estado{
        Activo,Desactivo
    }

    public Integer getId_insumo() {
        return id_insumo;
    }

    public void setId_insumo(Integer id_insumo) {
        this.id_insumo = id_insumo;
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

    public double getStock_Actual() {
        return stock_Actual;
    }

    public void setStock_Actual(double stock_Actual) {
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
