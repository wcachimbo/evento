package com.evento.repository;

import com.evento.model.Stock;

import java.util.List;

public interface StockRepository {
    /**
     * Obtener el stock de los todo los productos por empresa
     * @param company Identificador unico de la empresa
     * @return Listado de producto con la cantidad de productos disponible
     */
    List<Stock> getAllStockProduct(Long company);
}
