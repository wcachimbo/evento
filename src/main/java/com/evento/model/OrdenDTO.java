package com.evento.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdenDTO {

    @NotNull(message = "La fecha no puede estar vacía")
    private int company;

    @NotNull(message = "La fecha no puede estar vacía")
    private Integer date;

    @NotBlank(message = "El celular no puede estar vacío")
    private String phone;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @NotNull(message = "El total debe ser mayor a cero")
    private BigDecimal total;

    @NotNull(message = "El subTotal debe ser mayor a cero")
    private BigDecimal subTotal;

    private String description;

    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<ProductDTO> products;
}
