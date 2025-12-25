package com.evento.controller;

import com.evento.model.ApiResponse;
import com.evento.model.OrdenDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/orden")
public interface OrdenController {
    /**
     * Crea una nueva orden de compra.
     * Este endpoint recibe la información de la orden, valida los datos de entrada
     * y delega el proceso de creación al servicio correspondiente.
     * En caso de éxito, retorna una respuesta con el identificador de la orden creada
     * y los datos relevantes del pedido.
     * @param req objeto {@link OrdenDTO} que contiene la información necesaria
     *            para la creación de la orden. Los campos son validados mediante
     *            anotaciones de Bean Validation.
     *
     * @return {@link ResponseEntity} que envuelve un {@link ApiResponse} con:
     */
    @PostMapping("/createOrden")
    ResponseEntity<ApiResponse> createOrden(@Valid @RequestBody OrdenDTO req);
}
