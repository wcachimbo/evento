package com.evento.model;

import java.util.List;

public record OrdenCreate(
        Long idOrden,
        String phone,
        String name,
        List<ProductDTO> products

) {
}
