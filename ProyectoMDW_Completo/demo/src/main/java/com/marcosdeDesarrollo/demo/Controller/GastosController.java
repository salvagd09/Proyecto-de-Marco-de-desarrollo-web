package com.marcosdeDesarrollo.demo.Controller;

import com.marcosdeDesarrollo.demo.Entity.Gastos;
import com.marcosdeDesarrollo.demo.Repository.GastosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gastos")
@CrossOrigin(origins="*")
public class GastosController {
    @Autowired
    private GastosRepository gastosRepository;

    // Obtener todos los gastos
    @GetMapping
    public List<Gastos> listarGastos() {
        return gastosRepository.findAll();
    }

    // Crear un nuevo gasto
    @PostMapping
    public Gastos crearGasto(@RequestBody Gastos gasto) {
        return gastosRepository.save(gasto);
    }
    // Actualizar un gasto
    @PutMapping("/{id}")
    public Gastos actualizarGasto(@PathVariable Integer id, @RequestBody Gastos gasto) {
        gasto.setId_gasto(id); // aseguramos que actualice el correcto
        return gastosRepository.save(gasto);
    }
    // Eliminar un gasto
    @DeleteMapping("/{id}")
    public void eliminarGasto(@PathVariable Integer id) {
        gastosRepository.deleteById(id);
    }
}
