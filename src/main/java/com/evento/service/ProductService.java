package com.evento.service;

import com.evento.model.Product;

import java.util.List;

public interface ProductService {
    /**
     * Servicio encargado de realizar la logica del negocio para obtener los productos
     * @param company identificación de la empresa
     * @return Listado de productos
     */
    List<Product> getAllProduct(Long company);
}
