package com.evento.repository.impl;


import com.evento.exception.EventoException;
import com.evento.repository.DetailOrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

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
            int quantity,
            BigDecimal amount,
            BigDecimal priceUnit) {
        try {
             jdbcClient.sql("""
                            INSERT INTO detail_orden
                            (company, orden_id, product_id, quantity, amount, priceunit)
                            VALUES (:company, :ordenId, :productId, :quantity, :amount, :priceunit)
                            """)
                    .param("company", company)
                    .param("ordenId", idOrden)
                    .param("productId", idProduct)
                    .param("quantity", quantity)
                    .param("amount", amount)
                    .param("priceunit", priceUnit)
                    .update();
        } catch (Exception e) {
            log.error("Error insertando los producto de la orden - {}", e.getMessage(), e);
            throw new EventoException("0003","No se pudo insertar los productos de la orden");
        }
    }

}