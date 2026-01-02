package com.evento.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductListDTO {

    @NotNull(message = "La empresa no puede estar vacía")
    private int company;

    @NotNull(message = "La fecha no puede estar vacía")
    private Integer date;

    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<ProductDTO> products;
}
