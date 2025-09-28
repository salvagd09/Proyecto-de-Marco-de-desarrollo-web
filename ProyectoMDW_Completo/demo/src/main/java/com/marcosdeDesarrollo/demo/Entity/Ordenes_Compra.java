package com.marcosdeDesarrollo.demo.Entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name="ordenes_compra")
public class Ordenes_Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer id_orden;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false)
    private Integer id_usuario;
    @ManyToOne
    @JoinColumn(name="id_proveedor",nullable=false)
    private Integer id_proveedor;
    @Enumerated(EnumType.STRING)
    private estado estado_Compra;
    @Value("${total:0}")
    private double total;
    private enum estado{
        Pendiente,Aprobada,Recibida,Cancelada
    }
    public Integer getId_orden() {
        return id_orden;
    }
    public void setId_orden(Integer id_orden) {
        this.id_orden = id_orden;
    }
    public Integer getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    public Integer getId_proveedor() {
        return id_proveedor;
    }
    public void setId_proveedor(Integer id_proveedor) {
        this.id_proveedor = id_proveedor;
    }
    public estado getEstado_Compra() {
        return estado_Compra;
    }
    public void setEstado_Compra(estado estado_Compra) {
        this.estado_Compra = estado_Compra;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}
