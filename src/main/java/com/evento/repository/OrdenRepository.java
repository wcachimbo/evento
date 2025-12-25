package com.evento.repository;

import com.evento.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrdenRepository {
    /**
     * Obtener los el total de los productos alquilado por fecha
     * @param company Identificación unica de la empresa
     * @param date fecha actual
     * @return Listado de producto con el total ya alquilado
     */
    List<Product> getOrdenProduct(Long company, Long date);

    /**
     * Crea un nuevo pedido (orden) en el sistema y retorna el identificador generado automáticamente.
     * <p>
     * Este método inserta un registro en la tabla {@code orden}, calculando previamente los valores
     * monetarios del pedido (subtotal y total). El campo {@code id_orden} es generado por la base de
     * datos de forma automática (BIGSERIAL).
     * </p>
     *
     * <p>
     * La fecha de creación ({@code create_date}) se asigna automáticamente en la base de datos
     * usando {@code CURRENT_TIMESTAMP}.
     * </p>
     *
     * @param company     Identificador de la empresa a la que pertenece el pedido.
     * @param ordenDate   Fecha lógica del pedido en formato {@code yyyymmdd} (ej. 20251222).
     * @param address     Dirección de entrega del pedido.
     * @param status      Estado actual del pedido (ej. CONFIRMADO, PENDIENTE, CANCELADO).
     * @param phone       Número de teléfono de contacto del cliente.
     * @param subtotal    Subtotal del pedido (suma de precios sin cargos adicionales).
     * @param total       Total final del pedido (subtotal + cargos o ajustes).
     * @param description Descripción u observaciones adicionales del pedido (opcional).
     *
     * @return Identificador único ({@code id_orden}) generado automáticamente para el pedido creado.
     *
     * @throws RuntimeException si ocurre un error al insertar la orden en la base de datos.
     */
    Optional<Long> insertOrden(
            int company,
            Integer ordenDate,
            String address,
            String status,
            String phone,
            BigDecimal subtotal,
            BigDecimal total,
            String description
    );
}
