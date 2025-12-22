package com.evento.repository.impl;

import com.evento.model.Product;
import com.evento.repository.OrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}