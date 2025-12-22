package com.evento.repository;

import com.evento.model.Product;

import java.util.List;

public interface ProductRepository {
    /**
     * Repositorio encargado de obtener de la tabla de productos los productos por empresa
     * @param company identificación de la empresa
     * @return Listado de productos
     */
    List<Product> getAllProducts(Long company);
}
