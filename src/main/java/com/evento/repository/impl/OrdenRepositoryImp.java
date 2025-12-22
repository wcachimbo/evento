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
                                    SUM(d.quantity) AS total_quantity,
                                    0 AS disponible_quantity
                                FROM "orden" o
                                JOIN detail_orden d
                                    ON d.orden_id = o.id_orden
                                JOIN "product" p
                                    ON p.id_product = d.product_id
                                WHERE o.company = :company
                                  AND o.orden_date = :date
                                GROUP BY
                                    p.id_product,
                                    p.nombre,
                                    p.company,
                                    p.price,
                                    p.status,
                                    p.color,
                                    p.image_base64
                                ORDER BY total_quantity DESC
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