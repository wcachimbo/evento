package com.evento.model;

import java.util.List;

public record OrdenCreate(
        Long idOrden,
        String phone,
        String name,
        String whatsAppLink,
        List<ProductDTO> products

) {
}
