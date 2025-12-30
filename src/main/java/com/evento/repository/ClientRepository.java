package com.evento.repository;

import com.evento.model.ClientInfo;

import java.util.Optional;

public interface ClientRepository {
    /**
     * Obtiene el identificador del cliente asociado a una empresa y número telefónico.
     * <p>
     * Si el cliente ya existe (validado por la restricción única {@code (company, phone)}),
     * se retorna su {@code id_client}.
     * Si no existe, se crea un nuevo registro con la información proporcionada y se retorna
     * el {@code id_client} generado.
     * </p>
     *
     * <p>
     * Este método está diseñado para ser utilizado en el proceso de creación de pedidos,
     * garantizando que siempre exista un cliente válido antes de registrar los detalles
     * del pedido.
     * </p>
     *
     * @param company identificador de la empresa a la que pertenece el cliente
     * @param phone   número telefónico del cliente (clave única por empresa)
     * @param name    nombre del cliente
     * @param address dirección del cliente
     * @return {@link Optional} que contiene el {@code id_client} si la operación fue exitosa,
     * o {@link Optional#empty()} si no fue posible obtener o crear el cliente
     * @throws org.springframework.dao.DataAccessException si ocurre un error de acceso a datos durante la operación
     */
    Optional<Long> clientOrden(int company, String phone, String name, String address);

    /**
     * Obtiene la información básica de un cliente a partir del teléfono y la empresa.
     * <p>
     * Este método permite consultar los datos del cliente asociado al número telefónico
     * proporcionado dentro de una empresa específica.
     * </p>
     *
     * @param company identificador de la empresa a la que pertenece el cliente
     * @param phone   número telefónico del cliente
     * @return un {@link Optional} que contiene la información del cliente si existe;
     * {@link Optional#empty()} si no se encuentra ningún cliente con los criterios indicados
     */
    Optional<ClientInfo> infoClient(int company, String phone);

    /**
     * Actualiza la información de un cliente previamente registrado.
     * <p>
     * El método localiza al cliente por empresa y número telefónico, valida la
     * información actual y aplica los nuevos datos proporcionados.
     * </p>
     *
     * @param company    identificador de la empresa a la que pertenece el cliente
     * @param phone      número telefónico del cliente
     * @param oldName    nombre actual del cliente registrado en el sistema
     * @param oldAddress dirección actual del cliente registrada en el sistema
     * @param newName    nuevo nombre del cliente
     * @param newAddress nueva dirección del cliente
     * @return un {@link Optional} que contiene {@code true} si la actualización se
     * realizó correctamente; {@link Optional#empty()} si no se encontraron
     * cambios o si el cliente no existe
     */
    Optional<Boolean> updateClient(int company, String phone, String oldName, String oldAddress, String newName, String newAddress);
}
