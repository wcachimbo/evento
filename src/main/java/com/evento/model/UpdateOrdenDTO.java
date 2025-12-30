package com.evento.model;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateOrdenDTO {

    @NotNull(message = "La empresa no puede estar vacía")
    private int company;

    @NotNull(message = "El id del pedido no puede estar vacío")
    private Long idOrden;

    @NotNull(message = "El teléfono del pedido no puede estar vacío")
    private String phone;

    private boolean changeProduct;

    private Integer date;
    private String name;
    private String address;
    private BigDecimal total;
    private BigDecimal subTotal;
    private String description;
    private List<ProductDTO> products;


}
