package com.evento.repository;

import java.math.BigDecimal;
import java.util.Optional;

public interface DetailOrdenRepository {
    /**
     * Inserta un producto dentro del detalle de una orden.
     * <p>
     * Este método registra la relación entre una orden y un producto,
     * especificando la cantidad solicitada y el monto total asociado.
     * El identificador del detalle (id_detail) es autogenerado por la base de datos.
     * @param company   identificador de la empresa propietaria de la orden
     * @param idOrden   identificador de la orden asociada
     * @param idProduct identificador del producto incluido en la orden
     * @param quantity  cantidad del producto solicitada (debe ser mayor a cero)
     * @param amount    monto total correspondiente al producto (quantity * precio unitario)
     *
     */
    void insertOrdenProduct(
            int company,
            int idOrden,
            int idProduct,
            int quantity,
            BigDecimal amount,
            BigDecimal priceUnit);
}
