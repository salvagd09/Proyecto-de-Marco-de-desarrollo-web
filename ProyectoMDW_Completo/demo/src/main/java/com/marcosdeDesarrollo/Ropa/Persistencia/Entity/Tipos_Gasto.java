
package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tipos_gasto")
public class Tipos_Gasto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_tipo")
    private Integer idTipo;
    @Column(name="nombre_tipo",nullable=false)
    private String nombreTipo;
    @OneToMany(mappedBy="gasto")
    private List<Gastos> gastos;
    public Integer getId_tipo() {
        return idTipo;
    }

    public void setId_tipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre_tipo() {
        return nombreTipo;
    }

    public void setNombre_tipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
    public List<Gastos> getGastos() {
        return gastos;
    }
    public void setGastos(List<Gastos> gastos) {
        this.gastos = gastos;
    }
}