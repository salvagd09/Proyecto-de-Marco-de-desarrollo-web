package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="categoria")
public class Categoria {
    @Id
    @OneToMany
    @JoinColumn(name="id_categoria",nullable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id_categoria;
    private String nombre_categoria;
    private String descripcion;

    public Integer getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(Integer id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
