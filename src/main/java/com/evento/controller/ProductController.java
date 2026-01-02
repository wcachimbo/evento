package com.evento.controller;

import com.evento.model.ApiResponse;
import com.evento.model.ProductListDTO;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
public interface ProductController {

    /**
     * Este punto de entrada te permite obtener todo los producto por empresa
     *
     * @param company identificación de la empresa
     * @return Listado de productos
     */
    @GetMapping("/getProduct")
    ResponseEntity<@NonNull ApiResponse> getAllProducts(@RequestParam Long company);

    /**
     * Punto de entrada REST que permite obtener el listado de productos
     * junto con su disponibilidad para una fecha específica.
     * El endpoint recibe un objeto {@link ProductListDTO} que contiene
     * los identificadores de los productos, la empresa y la fecha a consultar.
     * A partir de esta información, el controlador delega al servicio
     * correspondiente el cálculo de la disponibilidad de cada producto.
     * La respuesta se devuelve envuelta en un {@link ApiResponse}, manteniendo
     * un formato estándar para las respuestas de la API.
     * En caso de que la solicitud sea inválida o no existan productos disponibles,
     * el endpoint puede retornar un código de error HTTP apropiado junto con
     * el mensaje correspondiente.
     *
     * @param req Objeto de tipo {@link ProductListDTO} que contiene los parámetros
     *            necesarios para la consulta:
     *            <ul>
     *              <li>Listado de identificadores de productos</li>
     *              <li>Identificador de la empresa</li>
     *              <li>Fecha de consulta</li>
     *            </ul>
     * @return {@link ResponseEntity} que contiene un {@link ApiResponse} con el
     * listado de productos y su disponibilidad, o un mensaje de error
     * en caso de que la operación no pueda completarse.
     */
    @PostMapping("/available")
    ResponseEntity<@NonNull ApiResponse> getProduct(@Valid @RequestBody ProductListDTO req);
}
