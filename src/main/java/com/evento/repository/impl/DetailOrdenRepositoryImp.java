package com.evento.repository.impl;


import com.evento.exception.EventoException;
import com.evento.repository.DetailOrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.evento.ulti.EventoError.ERROR_PRODUCT_CREATE;
import static com.evento.ulti.EventoError.ERROR_PRODUCT_ORDEN;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DetailOrdenRepositoryImp implements DetailOrdenRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void insertOrdenProduct(
            int company,
            int idOrden,
            int idProduct,
            int idClient,
            int quantity,
            BigDecimal amount,
            BigDecimal priceUnit) {
        try {
            jdbcClient.sql("""
                            INSERT INTO detail_orden
                            (company, orden_id, product_id, client_id, quantity, amount, priceunit)
                            VALUES (:company, :ordenId, :productId, :clientId, :quantity, :amount, :priceunit)
                            """)
                    .param("company", company)
                    .param("ordenId", idOrden)
                    .param("productId", idProduct)
                    .param("clientId", idClient)
                    .param("quantity", quantity)
                    .param("amount", amount)
                    .param("priceunit", priceUnit)
                    .update();
        } catch (Exception e) {
            log.error("Error insertando los producto de la orden - {}", e.getMessage(), e);
            throw new EventoException(ERROR_PRODUCT_CREATE);
        }
    }

    @Override
    public void deleteOrdenProduct(int company, Long idOrden) {
        try {
            jdbcClient.sql("""
                            DELETE FROM detail_orden
                            WHERE company =:company AND orden_id =:ordenId;
                            """)
                    .param("company", company)
                    .param("ordenId", idOrden)
                    .update();
        } catch (Exception e) {
            log.error("Error eliminando los productos de la orden - {}", e.getMessage(), e);
            throw new EventoException(ERROR_PRODUCT_ORDEN);
        }
    }


}