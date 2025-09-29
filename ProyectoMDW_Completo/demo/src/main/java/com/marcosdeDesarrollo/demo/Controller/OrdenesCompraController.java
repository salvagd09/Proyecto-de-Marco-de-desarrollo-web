package com.marcosdeDesarrollo.demo.Controller;

import com.marcosdeDesarrollo.demo.Entity.Gastos;
import com.marcosdeDesarrollo.demo.Entity.Ordenes_Compra;
import com.marcosdeDesarrollo.demo.Repository.GastosRepository;
import com.marcosdeDesarrollo.demo.Repository.OrdenesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class OrdenesCompraController {
    private OrdenesRepository ComprasRepository;

    // Obtener todos los gastos
    @GetMapping
    public List<Ordenes_Compra> listarOrdenes() {
        return ComprasRepository.findAll();
    }

    // Crear un nuevo gasto
    @PostMapping
    public Ordenes_Compra crearOrden(@RequestBody Ordenes_Compra orden) {
        return ComprasRepository.save(orden);
    }
    // Actualizar un gasto
    @PutMapping("/{id}")
    public Ordenes_Compra actualizarOrden(@PathVariable Integer id, @RequestBody Ordenes_Compra orden) {
        orden.setId_orden(id); // aseguramos que actualice el correcto
        return ComprasRepository.save(orden);
    }
    // Eliminar un gasto
    @DeleteMapping("/{id}")
    public void eliminarGasto(@PathVariable Integer id) {
        ComprasRepository.deleteById(id);
    }
}
