package com.evento.service;

import com.evento.model.Product;

import java.util.List;

public interface StockService {
    /**
     * Servicio encargado de obtener la disponibilidad de los productos
     * Se encarga de realizar la disminución de los productos ya alquilados
     * @param company Identificación unica de la empresa
     * @return Un listado de productos con por la fecha actual
     */
    List<Product> stockAllProduct(Long company);
}
