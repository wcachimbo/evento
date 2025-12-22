package com.evento.model;

import java.time.LocalDateTime;

public record Stock(
        Long idStock,
        String company,
        Long productId,
        String productName,
        Integer quantity,
        LocalDateTime dateCreate,
        LocalDateTime dateUpdate,
        String description
) {
}
