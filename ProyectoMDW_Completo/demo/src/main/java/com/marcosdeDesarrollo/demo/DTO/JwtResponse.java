package com.marcosdeDesarrollo.demo.DTO;

// Esta será la estructura del JSON de respuesta exitosa.
public record JwtResponse(String token, Integer id, String email, String rol) {
}