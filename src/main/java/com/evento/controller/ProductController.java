package com.evento.controller;

import com.evento.model.ApiResponse;
import com.evento.model.Product;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
public interface ProductController {

    /**
     * Este punto de entrada te permite obtener todo los producto por empresa
     * @param company identificación de la empresa
     * @return Listado de productos
     */
    @GetMapping("/getProduct")
    ResponseEntity<@NonNull ApiResponse> getAllProducts(@RequestParam Long company);
}
