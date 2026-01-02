package com.evento.ulti;

import lombok.Getter;

@Getter
public enum EventoError {
    ORDEN_NO_CREADA("0001", "No se pudo crear el pedido, intente nuevamente"),
    STOCK_NOT_EXIST("0002", "No existe stock para la empresa"),
    CLIENTE_NO_VALIDO("0003", "El cliente no pudo crear o validar el cliente"),
    ORDEN_EMPTY("0004", "No hay pedidos pendientes por entregar"),
    ERROR_PRODUCT_CREATE("0005", "No hay pedidos pendientes por entregar"),
    ERROR_UPDATE_STATUS("0006", "No se realizo la actualización del estado del pedido"),
    CLIENT_NOT_FOUND("0007", "Cliente no encontrado"),
    ERROR_UPDATE_ORDEN("0008", "No se pudo actualizar la orden"),
    ERROR_UPDATE_CLIENT("0009", "No se pudo actualizar el cliente"),
    ERROR_PRODUCT_ORDEN("0010", "No se pudo eliminar los productos del pedido "),
    ERROR_INF_ORDEN("0011", "No se pudo obtener la información del pedido"),
    ERROR_INF_CLIENT("0012", "No se pudo obtener la información del cliente"),
    ERROR_STATUS_ORDEN("0013", "No se puede editar una orden entregada o cancelada"),
    ERROR_GET_PRODUCT("0014", "No se puede obtener la información de los productos"),
    ORDEN_EMPTY_COLLECT("0015", "No hay pedidos pendientes por recoger"),
    PRODUCT_QUANTITY_IS_BIGGER("0016", "La cantidad enviada de los productos es mayor a la disponible"),
    ERROR_INTERNO("9999", "Error interno del sistema"),
    ;

    private final String code;
    private final String message;

    EventoError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
