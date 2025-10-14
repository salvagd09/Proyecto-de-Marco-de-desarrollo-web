
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tipos_gasto")
public class Tipos_Gasto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_tipo")
    private Integer id_tipo;
    @Column(name="nombre_tipo",nullable=false)
    private String nombre_tipo;
    @OneToMany(mappedBy="gasto")
    private List<Gastos> gastos;
    public Integer getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(Integer id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }
    public List<Gastos> getGastos() {
        return gastos;
    }
    public void setGastos(List<Gastos> gastos) {
        this.gastos = gastos;
    }
}