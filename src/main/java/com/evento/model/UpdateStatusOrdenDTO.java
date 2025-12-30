package com.evento.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateStatusOrdenDTO {

    @NotNull(message = "La empresa no puede estar vacía")
    private int company;

    @NotNull(message = "El id del pedido no puede estar vacío")
    private Long idOrden;

    @NotBlank(message = "El estado no puede estar vacío")
    @Pattern(
            regexp = "E|X",
            message = "El estado del pedido debe ser 'E' (Entregado) o 'X' (Cancelado)"
    )
    private String status;

}
