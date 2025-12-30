package com.evento.service;

import com.evento.model.*;


public interface OrdenService {
    /**
     * Crea un nuevo pedido en el sistema.
     * <p>
     * El proceso incluye:
     * <ul>
     *   <li>Creación del pedido principal.</li>
     *   <li>Validación o creación del cliente asociado.</li>
     *   <li>Registro del detalle de productos del pedido.</li>
     * </ul>
     * <p>
     * La operación es transaccional; si ocurre un error en cualquiera
     * de los pasos, se realiza rollback de toda la operación.
     *
     * @param req objeto {@link OrdenDTO} que contiene la información
     *            del pedido, cliente y productos
     * @return {@link OrdenCreate} con el identificador del pedido
     * creado y la información relevante de respuesta
     * @throws com.evento.exception.EventoException si ocurre un error
     *                                              durante la creación del pedido
     */
    OrdenCreate createOrden(OrdenDTO req);

    /**
     * Obtiene los pedidos pendientes por entregar de una empresa.
     * <p>
     * Los pedidos son clasificados en:
     * <ul>
     *   <li>Pedidos para hoy</li>
     *   <li>Pedidos para mañana</li>
     *   <li>Pedidos para fechas posteriores</li>
     * </ul>
     *
     * @param company identificador de la empresa
     * @return {@link OrdeClassify} con la lista de pedidos clasificados
     * @throws com.evento.exception.EventoException si no existen pedidos
     *                                              pendientes o si ocurre un error en la consulta
     */
    OrdeClassify getPendingOrden(Long company);

    /**
     * Actualiza el estado de un pedido.
     * <p>
     * El estado permitido puede ser:
     * <ul>
     *   <li><b>E</b> - Entregado</li>
     *   <li><b>X</b> - Cancelado</li>
     * </ul>
     *
     * @param req objeto {@link UpdateStatusOrdenDTO} que contiene
     *            el identificador del pedido, la empresa y el nuevo estado
     * @return {@code true} si el estado fue actualizado correctamente,
     * {@code false} en caso contrario
     * @throws com.evento.exception.EventoException si ocurre un error
     *                                              durante la actualización del estado
     */
    boolean statusOrden(UpdateStatusOrdenDTO req);

    /**
     * Actualiza la información de un pedido existente.
     * <p>
     * Permite modificar los datos generales del pedido como fecha, datos del cliente,
     * totales y descripción. Opcionalmente, puede actualizar los productos asociados
     * al pedido cuando el indicador {@code changeProduct} está habilitado.
     * </p>
     *
     * @param req objeto {@link UpdateOrdenDTO} que contiene la información necesaria
     *            para actualizar el pedido. Debe incluir la empresa, el identificador
     *            del pedido y el teléfono del cliente. Los campos no nulos serán
     *            considerados para la actualización.
     * @return {@code true} si el pedido fue actualizado correctamente;
     * {@code false} si no se realizaron cambios o la actualización falló
     */
    boolean updateOrden(UpdateOrdenDTO req);
}
