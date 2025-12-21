package com.evento.repository.impl;

import com.evento.model.Product;
import com.evento.repository.ProdcutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ProdcutRepositoryImp implements ProdcutRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<Product> getAllProducts(Long company) {

        return jdbcClient
                .sql("""
                            SELECT 
                                id_product      AS idProduct,
                                company,
                                nombre,
                                descripcion,
                                color,
                                price,
                                status,
                                image_base64    AS imageBase64
                            FROM product
                            WHERE company = :company
                        """)
                .param("company", company)
                .query(Product.class)
                .list();
    }
}
