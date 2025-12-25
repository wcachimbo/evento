package com.evento.repository.impl;

import com.evento.model.Product;
import com.evento.repository.OrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrdenRepositoryImp implements OrdenRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<Product> getOrdenProduct(Long company, Long date) {
        try {
            return jdbcClient.sql("""
                            SELECT
                                p.id_product,
                                p.nombre,
                                p.company,
                                p.price,
                                p.status,
                                p.color,
                                p.image_base64 AS imagen,
                                COALESCE(SUM(d.quantity), 0) AS total_quantity,
                                (s.quantity - COALESCE(SUM(d.quantity), 0)) AS available
                            FROM stock s
                            JOIN product p
                                ON p.id_product = s.product_id
                            LEFT JOIN detail_orden d
                                ON d.product_id = p.id_product
                            LEFT JOIN "orden" o
                                ON o.id_orden = d.orden_id
                               AND o.company = :company
                               AND o.orden_date = :date
                            WHERE s.company = :company
                            GROUP BY
                                p.id_product,
                                p.nombre,
                                p.company,
                                p.price,
                                p.status,
                                p.color,
                                p.image_base64,
                                s.quantity
                            ORDER BY total_quantity DESC;
                            """)
                    .param("company", company)
                    .param("date", date)
                    .query(Product.class)
                    .list();
        } catch (Exception e) {
            log.error("Error obteniendo los productos de los pedidos de la fecha {} {}", date, e.getMessage());
            return List.of();
        }
    }

    @Override
    public Optional<Long> insertOrden(
            int company,
            Integer ordenDate,
            String address,
            String status,
            String phone,
            BigDecimal subtotal,
            BigDecimal total,
            String description
    ) {
        try {
            return jdbcClient.sql("""
                            INSERT INTO "orden"
                            (company, orden_date, address, status, phone, subtotal, total, description)
                            VALUES
                            (:company, :ordenDate, :address, :status, :phone, :subtotal, :total, :description)
                            RETURNING id_orden
                            """)
                    .param("company", company)
                    .param("ordenDate", ordenDate)
                    .param("address", address)
                    .param("status", status)
                    .param("phone", phone)
                    .param("subtotal", subtotal)
                    .param("total", total)
                    .param("description", description)
                    .query(Long.class)
                    .optional();
        } catch (Exception e) {
            log.error("Error insertando la orden - {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear la orden", e);
        }
    }

}