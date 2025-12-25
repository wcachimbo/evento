package com.evento.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductDTO {
    private int idProducto;
    private String name;
    private int unitValue;
    private BigDecimal unitPrice;
}
