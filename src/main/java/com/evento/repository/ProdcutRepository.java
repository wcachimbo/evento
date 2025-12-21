package com.evento.repository;

import com.evento.model.Product;

import java.util.List;

public interface ProdcutRepository {
    /**
     *
     * @param company
     * @return
     */
    List<Product> getAllProducts(Long company);
}
