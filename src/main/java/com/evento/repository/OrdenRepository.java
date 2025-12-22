package com.evento.repository;

import com.evento.model.Product;

import java.util.List;

public interface OrdenRepository {
    /**
     * Obtener los el total de los productos alquilado por fecha
     * @param company Identificación unica de la empresa
     * @param date fecha actual
     * @return Listado de producto con el total ya alquilado
     */
    List<Product> getOrdenProduct(Long company, Long date);
}
