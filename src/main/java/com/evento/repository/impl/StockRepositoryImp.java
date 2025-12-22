package com.evento.repository.impl;

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
}
