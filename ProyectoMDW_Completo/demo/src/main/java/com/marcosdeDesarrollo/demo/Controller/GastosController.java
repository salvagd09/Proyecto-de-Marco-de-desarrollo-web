package com.marcosdeDesarrollo.demo.Controller;

import com.marcosdeDesarrollo.demo.Entity.Gastos;
import com.marcosdeDesarrollo.demo.Service.GastosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
@CrossOrigin(origins = "*")
public class GastosController {
    private GastosService gastoService;
    public GastosController(GastosService gastoService) {
        this.gastoService = gastoService;
    }
    @GetMapping
    public List<Gastos> listarGastos(){
        return gastoService.listarGastos();
    }
    @PostMapping
    public Gastos guardarGastos(@RequestBody Gastos gasto){
        return gastoService.guardarGasto(gasto);
    }
}
