package com.evento.repository.impl;

import com.evento.exception.EventoException;
import com.evento.model.ClientInfo;
import com.evento.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.evento.ulti.EventoError.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ClientRepositoryImp implements ClientRepository {

    private final JdbcClient jdbcClient;


    @Override
    public Optional<Long> clientOrden(int company, String phone, String name, String address) {
        try {
            return jdbcClient.sql("""
                            WITH ins AS (
                                INSERT INTO client (
                                    company,
                                    name_client,
                                    phone,
                                    address,
                                    alias,
                                    description
                                )
                                VALUES (
                                    :company,
                                    :name_client,
                                    :phone,
                                    :address,
                                    :alias,
                                    :description
                                )
                                ON CONFLICT (company, phone)
                                DO NOTHING
                                RETURNING id_client
                            )
                            SELECT id_client
                            FROM ins
                            UNION ALL
                            SELECT id_client
                            FROM client
                            WHERE company = :company
                              AND phone = :phone
                            LIMIT 1
                            """)
                    .param("company", company)
                    .param("phone", phone)
                    .param("name_client", name)
                    .param("address", address)
                    .param("alias", name)
                    .param("description", name)
                    .query(Long.class)
                    .optional();
        } catch (Exception e) {
            log.error("Error insertando el cliente - {}", e.getMessage(), e);
            throw new EventoException(CLIENTE_NO_VALIDO);
        }
    }

    @Override
    public Optional<ClientInfo> infoClient(int company, String phone) {
        try {
            return jdbcClient.sql("""
                            SELECT id_client, company, name_client, phone, address, alias, description
                            FROM client
                            WHERE id_client=: idClient AND phone=: phone
                            """)
                    .param("company", company)
                    .param("phone", phone)
                    .query(ClientInfo.class)
                    .optional();
        } catch (Exception e) {
            log.error("Error insertando el cliente - {}", e.getMessage(), e);
            throw new EventoException(CLIENT_NOT_FOUND);
        }
    }


    @Override
    public Optional<Boolean> updateClient(int company, String phone, String oldName, String oldAddress, String newName, String newAddress) {
        try {
            StringBuilder sql = new StringBuilder("UPDATE client SET ");
            Map<String, Object> params = new HashMap<>();

            boolean hasChanges = false;

            // Nombre del cliente
            if (!Objects.equals(oldName, newName)) {
                sql.append("newName = :newName, ");
                params.put("newName", newName);
                hasChanges = true;
            }
            // Dirección del cliente
            if (!Objects.equals(oldAddress, newAddress)) {
                sql.append("newAddress = :newAddress, ");
                params.put("newAddress", newAddress);
                hasChanges = true;
            }

            // Si no hubo cambios
            if (!hasChanges) {
                return Optional.of(Boolean.TRUE);
            }

            // Quitar última coma
            sql.setLength(sql.length() - 2);

            sql.append(" WHERE phone = :phone AND company = :company");

            params.put("phone", phone);
            params.put("company", company);

            int rowsUpdated = jdbcClient.sql(sql.toString())
                    .params(params)
                    .update();

            return Optional.of(rowsUpdated > 0);
        } catch (Exception e) {
            log.error(
                    "Error actualizando cliente [company={}, idOrden={}]",phone, company, e);
            throw new EventoException(ERROR_UPDATE_CLIENT);
        }
    }


}