package com.evento.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class OrdenQuerys {
    private Long idOrden;
    private Long company;
    private Integer date;
    private String phone;
    private String address;
    private String nameClient;
    private BigDecimal total;
    private BigDecimal subTotal;
    private String description;
    private String status;
    private List<ProductDTO> products;
}