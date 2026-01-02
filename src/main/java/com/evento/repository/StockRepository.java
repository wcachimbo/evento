package com.evento.repository;

import com.evento.model.Product;
import com.evento.model.Stock;

import java.util.List;

public interface StockRepository {
    /**
     * Obtener el stock de los todo los productos por empresa
     * @param company Identificador unico de la empresa
     * @return Listado de producto con la cantidad de productos disponible
     */
    List<Stock> getAllStockProduct(Long company);

    /**
     * Obtiene el listado de productos con su disponibilidad para una fecha específica.
     * El cálculo de disponibilidad se realiza a partir del stock real registrado
     * y las cantidades ya ocupadas en órdenes asociadas a la empresa y a la fecha indicada.
     * La disponibilidad de cada producto se calcula con la siguiente fórmula:
     * disponibilidad = stock_total - cantidad_ocupada_en_ordenes
     * El método devuelve únicamente los productos cuyos identificadores estén incluidos
     * en la lista {@code productIds}. En caso de que un producto no tenga órdenes para
     * la fecha indicada, su cantidad ocupada será considerada como cero.
     * @param company    Identificador de la empresa a la que pertenecen los productos.
     * @param date       Fecha para la cual se desea consultar la disponibilidad del producto,
     *                   representada como timestamp (epoch).
     * @param productIds Lista de identificadores de productos a consultar.
     *
     * @return Lista de {@link Product} con la cantidad total ocupada y la cantidad
     *         disponible calculada para la fecha indicada. Si no existen registros,
     *         se retorna una lista vacía.
     */
    List<Product> getAvailableProducts(
            int company,
            Integer date,
            List<Integer> productIds
    );
}
