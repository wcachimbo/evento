package com.evento.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdenDetail(
        // ORDEN
        Long idOrden,
        Long company,
        Integer ordenDate,
        String address,
        String nameClient,
        String status,
        String phone,
        BigDecimal subtotal,
        BigDecimal total,
        String description,

        // DETAIL_ORDEN
        Long idDetail,
        int productId,
        Integer quantity,
        BigDecimal priceUnit,
        BigDecimal amount,

        // PRODUCT
        String nombreProducto,
        String productDescription
) {
}