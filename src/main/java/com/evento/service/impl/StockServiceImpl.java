package com.evento.service.impl;

import com.evento.exception.EventoException;
import com.evento.model.Product;
import com.evento.repository.OrdenRepository;
import com.evento.repository.StockRepository;
import com.evento.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final OrdenRepository ordenRepository;

    @Override
    public List<Product> stockAllProduct(Long company) {
        try {
            Long date = Long.parseLong(
                    LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
            );
            // Stock real
            var stockList = stockRepository.getAllStockProduct(company);

            if (stockList.isEmpty()) {
                throw new EventoException("0001", "No existe stock para la empresa");
            }

            // Productos ocupados en la fecha
            var ordenList = ordenRepository.getOrdenProduct(company, date);

            // Convertimos ocupados a MAP para cruce rápido
            Map<Long, Product> productMap = ordenList.stream()
                    .collect(Collectors.toMap(
                            Product::id_product,
                            p -> p
                    ));

            // Cruce final
            return stockList.stream()
                    .map(stock -> {

                        Product product = productMap.get(stock.productId());

                        int ocupado = product != null ? product.total_quantity() : 0;
                        int disponible = stock.quantity() - ocupado;

                        return new Product(
                                stock.productId(),
                                stock.productName(),
                                company,
                                product != null ? product.price() : BigDecimal.ZERO,
                                product != null ? product.status() : Boolean.FALSE,
                                product != null ? product.color() : null,
                                product != null ? product.imagen() : null,
                                ocupado,
                                Math.max(disponible, 0)
                        );
                    })
                    .toList();

        } catch (Exception e) {
            log.error("Error obteniendo los producto de la empresa {} {}", company, e.getMessage());
            throw new EventoException("9999", "Error obteniendo el stock de los productos");
        }
    }
}
