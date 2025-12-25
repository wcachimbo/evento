package com.evento.service.impl;

import com.evento.exception.EventoException;
import com.evento.model.OrdenCreate;
import com.evento.model.OrdenDTO;
import com.evento.model.ProductDTO;
import com.evento.repository.OrdenRepository;
import com.evento.repository.DetailOrdenRepository;
import com.evento.service.OrdenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.evento.ulti.Status.CONFIRMADO;
import static com.evento.ulti.Status.PENDIENTE;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final DetailOrdenRepository detailOrdenRepository;

    @Override
    public OrdenCreate createOrden(OrdenDTO req) {
        try {
            /// Crear la orden
            Long ordenId = ordenRepository.insertOrden(
                    req.getCompany(),
                    req.getDate(),
                    req.getAddress(),
                    req.getSubTotal().compareTo(BigDecimal.ZERO) > 0
                            ? CONFIRMADO.getDescripcion()
                            : PENDIENTE.getDescripcion(),
                    req.getPhone(),
                    req.getSubTotal(),
                    req.getTotal(),
                    req.getDescription()
            ).orElseThrow(() ->
                    new EventoException("0003", "No se pudo crear el pedido, intente nuevamente")
            );

            /// Insertar detalle por cada producto
            for (ProductDTO prod : req.getProducts()) {

                var amount = prod.getUnitPrice().multiply(BigDecimal.valueOf(prod.getUnitValue()));

                detailOrdenRepository.insertOrdenProduct(
                        req.getCompany(),
                        ordenId.intValue(),
                        prod.getIdProducto(),
                        prod.getUnitValue(),
                        amount,
                        prod.getUnitPrice()

                );
            }

            return new OrdenCreate(
                    ordenId,
                    req.getPhone(),
                    req.getName(),
                    req.getProducts()
            );

        } catch (Exception e) {
            log.error("Error creando la orden {} - {}", req, e.getMessage(), e);
            throw new EventoException("9999", "Error creando el pedido");
        }
    }
}
