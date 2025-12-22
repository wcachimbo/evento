package com.evento.model;

import java.math.BigDecimal;

public record Product(
        Long id_product,
        String nombre,
        Long company,
        BigDecimal price,
        Boolean status,
        String color,
        String imagen,
        int total_quantity,
        int available

) {
}
