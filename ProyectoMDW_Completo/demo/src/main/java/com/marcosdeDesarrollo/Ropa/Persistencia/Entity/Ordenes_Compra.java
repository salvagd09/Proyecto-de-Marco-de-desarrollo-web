
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
    private Integer idOrden;
    @Column(name="id_usuario")
    private Integer idUsuario;
    @Column(name="id_proveedor")
    private Integer idProveedor;
    @ManyToOne
    @JoinColumn(name="id_proveedor",nullable = false,insertable = false,updatable = false)
    private Proveedores ordenP;
    @ManyToOne
    @JoinColumn(name="id_usuario",nullable=false,insertable = false,updatable = false)
    private Usuario usuariosOC;
    @OneToMany(mappedBy="ordenesCompras")
    private List<DetalleOrden> ordenesDetalles;
    @Enumerated(EnumType.STRING)
    private estado estado_Compra;
    @Value("${total:0}")
    private Double total;
    private enum estado{
        Pendiente,Aprobada,Recibida,Cancelada
    }
    public Integer getId_orden() {
        return idOrden;
    }
    public void setId_orden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public Proveedores getOrdenP() {
        return ordenP;
    }

    public void setOrdenP(Proveedores ordenP) {
        this.ordenP = ordenP;
    }

    public Usuario getUsuariosOC() {
        return usuariosOC;
    }

    public void setUsuariosOC(Usuario usuariosOC) {
        this.usuariosOC = usuariosOC;
    }

    public List<DetalleOrden> getOrdenesDetalles() {
        return ordenesDetalles;
    }

    public void setOrdenesDetalles(List<DetalleOrden> ordenesDetalles) {
        this.ordenesDetalles = ordenesDetalles;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
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
