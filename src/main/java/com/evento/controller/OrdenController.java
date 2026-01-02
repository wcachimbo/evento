package com.evento.controller;

import com.evento.model.ApiResponse;
import com.evento.model.OrdenDTO;
import com.evento.model.UpdateOrdenDTO;
import com.evento.model.UpdateStatusOrdenDTO;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/orden")
public interface OrdenController {
    /**
     * Crea una nueva orden de compra.
     * Este endpoint recibe la información de la orden, valida los datos de entrada
     * y delega el proceso de creación al servicio correspondiente.
     * En caso de éxito, retorna una respuesta con el identificador de la orden creada
     * y los datos relevantes del pedido.
     *
     * @param req objeto {@link OrdenDTO} que contiene la información necesaria
     *            para la creación de la orden. Los campos son validados mediante
     *            anotaciones de Bean Validation.
     * @return {@link ResponseEntity} que envuelve un {@link ApiResponse} con:
     */
    @PostMapping("/createOrden")
    ResponseEntity<ApiResponse> createOrden(@Valid @RequestBody OrdenDTO req);

    /**
     * Obtiene el listado de pedidos pendientes por entregar de una empresa.
     * <p>
     * Los pedidos se clasifican en:
     * <ul>
     *   <li>Pedidos para hoy</li>
     *   <li>Pedidos para mañana</li>
     *   <li>Pedidos para fechas posteriores</li>
     * </ul>
     *
     * @param company identificador de la empresa
     * @return {@link ResponseEntity} con un {@link ApiResponse} que contiene
     * los pedidos clasificados
     */
    @GetMapping("/getOrdenPending")
    ResponseEntity<ApiResponse> getOrdenPending(@RequestParam Long company);

    /**
     * Actualiza el estado de un pedido.
     * <p>
     * Estados permitidos:
     * <ul>
     *   <li><b>E</b> - Entregado</li>
     *   <li><b>X</b> - Cancelado</li>
     * </ul>
     *
     * @param req objeto {@link UpdateStatusOrdenDTO} con la información
     *            necesaria para actualizar el estado del pedido
     * @return {@link ResponseEntity} con un {@link ApiResponse} indicando
     * si la operación fue exitosa
     */
    @PutMapping("/updateStatus")
    ResponseEntity<@NonNull ApiResponse> updateStatusOrden(@Valid @RequestBody UpdateStatusOrdenDTO req);

    /**
     * Actualiza parcialmente la información de un pedido existente.
     * <p>
     * Este método permite modificar uno o varios campos de un pedido ya registrado,
     * según los valores proporcionados en el objeto {@link UpdateOrdenDTO}.
     * <br><br>
     * El pedido es identificado por su identificador único y solo los campos
     * presentes en la solicitud serán actualizados.
     *
     * @param req objeto que contiene los datos a actualizar del pedido.
     *            Debe incluir el identificador del pedido y los campos que
     *            se desean modificar.
     * @return {@link ResponseEntity} con un {@link ApiResponse} que indica
     * el resultado de la operación, incluyendo información de éxito
     * o error según corresponda.
     * @throws IllegalArgumentException si la información suministrada es inválida.
     */
    @PatchMapping("/updateOrden")
    ResponseEntity<@NonNull ApiResponse> updateOrden(@Valid @RequestBody UpdateOrdenDTO req);
}
