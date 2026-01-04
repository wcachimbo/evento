package com.evento.repository.impl;

import com.evento.model.Product;
import com.evento.model.Stock;
import com.evento.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StockRepositoryImp implements StockRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<Stock> getAllStockProduct(Long company) {
        try {
            return jdbcClient
                    .sql("""
                            SELECT id_stock,
                            company,
                            product_id,
                            product_name,
                            quantity,
                            date_create,
                            date_update,
                            description
                            FROM stock
                            WHERE company =:company
                            """)
                    .param("company", company)
                    .query(Stock.class)
                    .list();
        } catch (Exception e) {
            log.error("Error obteniendo el stock para los producto de la empresa: {} {}", company, e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Product> getAvailableProducts(
            int company,
            Integer date,
            List<Integer> productIds
    ) {
        try {
            return jdbcClient.sql("""
                            SELECT
                                p.id_product,
                                p.nombre,
                                p.company,
                                p.price,
                                p.status,
                                COALESCE(o.total_quantity, 0) AS total_quantity,
                                (s.quantity - COALESCE(o.total_quantity, 0)) AS available
                            FROM stock s
                            JOIN product p
                                ON p.id_product = s.product_id
                            LEFT JOIN (
                                SELECT
                                    d.product_id,
                                    SUM(d.quantity) AS total_quantity
                                FROM detail_orden d
                                JOIN "orden" o
                                    ON o.id_orden = d.orden_id
                                WHERE o.company = :company
                                  AND o.orden_date = :date
                                  AND o.status IN ('P','C','G','E')
                                GROUP BY d.product_id
                            ) o ON o.product_id = p.id_product
                            WHERE s.company = :company
                              AND p.id_product IN (:productIds)
                            """)
                    .param("company", company)
                    .param("date", date)
                    .param("productIds", productIds)
                    .query((rs, rowNum) -> new Product(
                            rs.getLong("id_product"),
                            rs.getString("nombre"),
                            rs.getLong("company"),
                            rs.getBigDecimal("price"),
                            rs.getBoolean("status"),
                            null,
                            null,
                            rs.getInt("total_quantity"),
                            rs.getInt("available")
                    ))
                    .list();

        } catch (Exception e) {
            log.error("Error obteniendo stock disponible {}", e.getMessage(), e);
            return List.of();
        }
    }

}
