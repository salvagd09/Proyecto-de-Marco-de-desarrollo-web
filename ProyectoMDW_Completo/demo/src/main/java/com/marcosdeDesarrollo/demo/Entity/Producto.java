package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name="productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id_producto;
    @Column(name = "nombre_producto", nullable = false)
    private String nombre_producto;
    private String descripcion;
    @Value("{stock_actual:0}")
    private int stock_actual;
    @Column(name = "precio", nullable = false)
    private double precio;
    private String talla;
    private String color;
    @Enumerated(EnumType.STRING)
    private estado estado;
    private enum estado{
        Activo,Inactivo
    }
    private String imagen;
    @ManyToOne
    @JoinColumn(name="id_categoria",nullable=false)
    private Integer id_categoria;
    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock_actual() {
        return stock_actual;
    }

    public void setStock_actual(int stock_actual) {
        this.stock_actual = stock_actual;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public estado getEstado() {
        return estado;
    }

    public void setEstado(estado estado) {
        this.estado = estado;
    }
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public Integer getId_categoria() {
        return id_categoria;
    }
    public void setId_categoria(Integer id_categoria) {
        this.id_categoria = id_categoria;
    }
}
