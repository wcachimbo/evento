package com.evento.service;

import com.evento.model.ClientInfo;

public interface ClientService {
    /**
     * Obtiene la información de un cliente a partir de su número de teléfono.
     * <p>
     * El número de teléfono se considera único dentro del contexto de una empresa.
     * Si el cliente no existe, la implementación puede retornar {@code null}
     * o lanzar una excepción de negocio según la lógica definida.
     * </p>
     *
     * @param company identificador de la empresa asociada al cliente
     * @param phone   número de teléfono único del cliente
     * @return {@link ClientInfo} con la información del cliente,
     * o {@code null} si no se encuentra
     * @throws IllegalArgumentException si {@code phone} es nulo o vacío
     */
    ClientInfo getInfoClient(int company, String phone);
}
