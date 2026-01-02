package com.evento.controller;

import com.evento.model.ApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/client")

public interface ClientController {
    /**
     * Obtiene la información de un cliente a partir de su número de teléfono.
     * <p>
     * El número de teléfono es considerado único dentro del contexto
     * de una empresa. Este endpoint es utilizado para validar la existencia
     * del cliente y recuperar sus datos principales.
     * </p>
     *
     * @param company identificador de la empresa
     * @param phone   número de teléfono del cliente
     * @return {@link ResponseEntity} que contiene un {@link ApiResponse}
     * con la información del cliente y el estado HTTP correspondiente
     * @apiNote Endpoint de solo lectura (GET)
     */
    @GetMapping("/getUserInfo")
    ResponseEntity<ApiResponse> getInfoClient(@RequestParam @NotNull int company, @RequestParam @NotNull String phone);
}
