package com.evento.service.impl;

import com.evento.model.Product;
import com.evento.repository.ProdcutRepository;
import com.evento.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProdcutRepository prodcutRepository;

    @Override
    public List<Product> getAllProduct(Long company) {
        try {

            return prodcutRepository.getAllProducts(company);

        } catch (Exception e) {
            log.error("Error obteniendo los producto de la empresa {} {}", company, e.getMessage());
            return List.of(); // nunca null
        }
    }
}
