package com.evento.controller.impl;

import com.evento.controller.ProductController;
import com.evento.model.ApiResponse;
import com.evento.model.ProductListDTO;
import com.evento.service.ProductService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ProductControllerImpl implements ProductController {


    private final ProductService productService;

    @Override
    public ResponseEntity<@NonNull ApiResponse> getAllProducts(@RequestParam Long company) {

        var products = productService.getAllProduct(company);
        var resp = new ApiResponse<>(
                "0000",
                "Consulta exitosa",
                products
        );
        log.info("Consulta de productos exitosa");

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<@NonNull ApiResponse> getProduct(ProductListDTO req) {

        var products = productService.getListProduct(req.getProducts(), req.getCompany(), req.getDate());
        var resp = new ApiResponse<>(
                "0000",
                "Consulta listado del productos exitosa",
                products
        );
        log.info("Consulta listado del productos exitosa");

        return ResponseEntity.ok(resp);
    }
}
