package com.marcosdeDesarrollo.Ropa.Persistencia.Entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Entity
@Table(name="ordenes_compra")
public class Ordenes_Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer id_orden;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false)
    private Usuario ordenUsuario;
    @ManyToOne
    @JoinColumn(name="id_proveedor",nullable=false)
    private Proveedores proveedor;
    @OneToMany(mappedBy="idOrden")
    private List<DetalleOrden> DetalleOrden;
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
    public Usuario getIdUsuario() {
        return ordenUsuario;
    }
    public void setIdUsuario(Usuario ordenUsuario) {
        this.ordenUsuario = ordenUsuario;
    }
    public Proveedores getProveedor() {
        return proveedor;
    }
    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }
    public List<DetalleOrden> getDetalleOrden() {
        return DetalleOrden;
    }
    public void setDetalleOrden(List<DetalleOrden> detalleOrden) {
        DetalleOrden = detalleOrden;
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