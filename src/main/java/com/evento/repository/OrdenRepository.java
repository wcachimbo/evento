package com.evento.repository;

import com.evento.model.OrdenDetail;
import com.evento.model.OrdenQuerys;
import com.evento.model.Product;
import com.evento.model.UpdateOrdenDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrdenRepository {
    /**
     * Obtener los el total de los productos alquilado por fecha
     *
     * @param company Identificación unica de la empresa
     * @param date    fecha actual
     * @return Listado de producto con el total ya alquilado
     */
    List<Product> getOrdenProduct(Long company, Long date);

    /**
     * Crea un nuevo pedido (orden) en el sistema y retorna el identificador generado automáticamente.
     * <p>
     * Este método inserta un registro en la tabla {@code orden}, calculando previamente los valores
     * monetarios del pedido (subtotal y total). El campo {@code id_orden} es generado por la base de
     * datos de forma automática (BIGSERIAL).
     * </p>
     *
     * <p>
     * La fecha de creación ({@code create_date}) se asigna automáticamente en la base de datos
     * usando {@code CURRENT_TIMESTAMP}.
     * </p>
     *
     * @param company     Identificador de la empresa a la que pertenece el pedido.
     * @param ordenDate   Fecha lógica del pedido en formato {@code yyyymmdd} (ej. 20251222).
     * @param status      Estado actual del pedido (ej. CONFIRMADO, PENDIENTE, CANCELADO).
     * @param subtotal    Subtotal del pedido (suma de precios sin cargos adicionales).
     * @param total       Total final del pedido (subtotal + cargos o ajustes).
     * @param description Descripción u observaciones adicionales del pedido (opcional).
     * @return Identificador único ({@code id_orden}) generado automáticamente para el pedido creado.
     * @throws RuntimeException si ocurre un error al insertar la orden en la base de datos.
     */
    Optional<Long> insertOrden(
            int company,
            Long clientId,
            Integer ordenDate,
            String status,
            BigDecimal subtotal,
            BigDecimal total,
            String description
    );

    /**
     * Obtiene el listado de pedidos pendientes por entregar para una empresa.
     * <p>
     * Este método consulta todos los pedidos cuyo estado se encuentre
     * en condición pendiente o confirmada, junto con el detalle de los
     * productos asociados a cada pedido.
     * </p>
     *
     * <p>
     * El resultado incluye información de la orden, el cliente asociado
     * y los productos registrados en el detalle del pedido.
     * </p>
     *
     * @param company Identificador de la empresa para la cual se consultan los pedidos.
     * @return Lista de {@link OrdenDetail} que representa los pedidos pendientes.
     * <p>
     * Si ocurre un error durante la consulta o no existen pedidos pendientes.
     */
    List<OrdenDetail> getOrdenPending(Long company);

    /**
     * Actualiza el estado de un pedido específico.
     * <p>
     * Permite cambiar el estado de una orden a valores como:
     * <ul>
     *   <li><b>ENTREGADO</b></li>
     *   <li><b>CANCELADO</b></li>
     * </ul>
     * </p>
     *
     * <p>
     * La actualización se realiza únicamente sobre la orden perteneciente
     * a la empresa indicada y al identificador del pedido suministrado.
     * </p>
     *
     * @param company Identificador de la empresa propietaria del pedido.
     * @param status  Nuevo estado del pedido.
     *                Ejemplo: {@code "E"} (Entregado), {@code "X"} (Cancelado).
     * @param idOrden Identificador único del pedido a actualizar.
     * @return un boolean  de {@link Boolean} con el estado de la actualización
     *
     */
    Optional<Boolean> updateStatus(int company, String status, Long idOrden);

    /**
     * Obtiene la información completa de un pedido a partir de su identificador.
     * <p>
     * Este método consulta la orden asociada a la empresa indicada y retorna
     * tanto los datos de cabecera del pedido como el detalle de los productos
     * asociados a la orden.
     * </p>
     *
     * <p>
     * En caso de que no exista un pedido con el {@code idOrden} suministrado
     * para la empresa indicada, el método retornará un {@link Optional#empty()}.
     * </p>
     *
     * @param company Identificador de la empresa propietaria del pedido.
     * @param idOrden Identificador único del pedido a consultar.
     * @return Un {@link Optional} que contiene un objeto {@link OrdenQuerys} con
     * la información completa del pedido (cabecera y detalle),
     * o {@link Optional#empty()} si el pedido no existe.
     */
    Optional<OrdenQuerys> getOrdenID(int company, Long idOrden);

    /**
     * Actualiza el estado y/o la información de un pedido previamente almacenado.
     * <p>
     * Este método compara los datos actuales del pedido con la nueva información
     * recibida y aplica únicamente los cambios necesarios, manteniendo la
     * consistencia del registro original.
     * </p>
     *
     * @param detailOld información actual del pedido almacenado en el sistema
     * @param detailNew nuevos datos del pedido a aplicar para la actualización
     * @return un {@link Optional} que contiene {@code true} si el pedido fue
     * actualizado correctamente; {@link Optional#empty()} si no se
     * realizaron cambios o si el pedido no pudo ser actualizado
     */
    Optional<Boolean> updateOrdenStatus(OrdenQuerys detailOld, UpdateOrdenDTO detailNew);

    /**
     * Consulta que me permite obtener el listado de pedidos pendiente por recoger
     * @param company
     * @return
     */
    List<OrdenDetail> getOrdenCollect(Long company);
}
