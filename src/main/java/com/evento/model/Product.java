package com.evento.model;

import java.math.BigDecimal;

public record Product(

        Long idProduct,
        Long company,
        String nombre,
        BigDecimal price,
        Boolean status

) {
}
