package com.evento.controller;

import com.evento.model.Product;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
public interface ProductController {

    /**
     *
     * @param company
     * @return
     */
    @GetMapping("/getProduct")
    ResponseEntity<List<Product>> getAllProducts(@RequestParam Long company);
}
