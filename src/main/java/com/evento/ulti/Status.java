package com.evento.ulti;

import lombok.Getter;

@Getter
public enum Status {

    PENDIENTE("P", "Pendiente"),
    CONFIRMADO("C", "Confirmado"),
    PAGADO("G", "Pagado"),
    ENTREGADO("E", "Entregado"),
    CANCELADO("X", "Cancelado"),
    FINALIZADO("F", "Finalizado"),
    ;

    private final String codigo;
    private final String descripcion;

    Status(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
}