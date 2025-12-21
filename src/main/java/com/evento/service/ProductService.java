package com.evento.service;

import com.evento.model.Product;

import java.util.List;

public interface ProductService {
    /**
     *
     * @param company
     * @return
     */
    List<Product> getAllProduct(Long company);
}
