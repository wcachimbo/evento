package com.evento.service.impl;

import com.evento.exception.EventoException;
import com.evento.model.*;
import com.evento.repository.ClientRepository;
import com.evento.repository.OrdenRepository;
import com.evento.repository.DetailOrdenRepository;
import com.evento.service.OrdenService;
import com.evento.ulti.WhatsAppMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.evento.ulti.EventoError.*;
import static com.evento.ulti.Status.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final WhatsAppMessage whatsAppMessage;
    private final ClientRepository clientRepository;
    private final DetailOrdenRepository detailOrdenRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrdenCreate createOrden(OrdenDTO req) {
        try {
            String status = getStatus(req.getSubTotal(), req.getTotal());

            /// Crear la orden
            Long ordenId = ordenRepository.insertOrden(
                    req.getCompany(),
                    req.getDate(),
                    status,
                    req.getSubTotal(),
                    req.getTotal(),
                    req.getDescription()
            ).orElseThrow(() ->
                    new EventoException(ORDEN_NO_CREADA)
            );

            ///  Crear o guardar el cliente
            Long clientID = clientRepository.clientOrden(req.getCompany(), req.getPhone(), req.getName(), req.getAddress())
                    .orElseThrow(() ->
                            new EventoException(CLIENTE_NO_VALIDO)
                    );
            /// Insertar detalle por cada producto
            InsertProduct(req.getProducts(), req.getCompany(), ordenId, clientID);

            String message = whatsAppMessage.buildMessage(req, ordenId);
            String whatsappLink = whatsAppMessage.buildWhatsAppLink(req.getPhone(), message);

            return new OrdenCreate(
                    ordenId,
                    req.getPhone(),
                    req.getName(),
                    whatsappLink,
                    req.getProducts()
            );

        } catch (EventoException e) {
            log.error("Error Creado el producto {}", req, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Error  Creado el producto {}", req, e);
            throw new EventoException(ERROR_INTERNO, e.getMessage());
        }
    }


    @Override
    public OrdeClassify getPendingOrden(Long company) {


        var resp = ordenRepository.getOrdenPending(company);

        if (resp.isEmpty()) {
            log.info("No se encontraron pedidos en la base de datos para la empresa {}", company);
            throw new EventoException(ORDEN_EMPTY);
        }
        try {

            LocalDate dateToday = LocalDate.now();
            LocalDate dateTomorrow = dateToday.plusDays(1);

            Map<Long, OrdenQuerys> orders = new LinkedHashMap<>();

            for (OrdenDetail d : resp) {

                OrdenQuerys order = orders.computeIfAbsent(d.idOrden(), id -> {
                    OrdenQuerys o = new OrdenQuerys();
                    o.setIdOrden(id);
                    o.setCompany(d.company());
                    o.setDate(d.ordenDate());
                    o.setPhone(d.phone());
                    o.setAddress(d.address());
                    o.setNameClient(d.nameClient());
                    o.setStatus(d.status());
                    o.setSubTotal(d.subtotal());
                    o.setTotal(d.total());
                    o.setDescription(d.description());
                    o.setProducts(new ArrayList<>());
                    return o;
                });

                ProductDTO product = new ProductDTO();
                product.setIdProducto(d.productId());
                product.setName(d.nombreProducto());
                product.setUnitValue(d.quantity());
                product.setUnitPrice(d.priceUnit());
                order.getProducts().add(product);
            }

            // Clasificación
            List<OrdenQuerys> today = new ArrayList<>();
            List<OrdenQuerys> tomorrow = new ArrayList<>();
            List<OrdenQuerys> orden = new ArrayList<>();

            for (OrdenQuerys o : orders.values()) {

                LocalDate ordenDate = parseOrdenDate(o.getDate());

                if (ordenDate.isEqual(dateToday)) {
                    today.add(o);
                } else if (ordenDate.isEqual(dateTomorrow)) {
                    tomorrow.add(o);
                } else if (ordenDate.isAfter(dateTomorrow)) {
                    orden.add(o);
                }
            }

            return new OrdeClassify(
                    today,
                    tomorrow,
                    orden);

        } catch (Exception e) {
            log.error("Error consultando los pedidos {}", e.getMessage(), e);
            throw new EventoException("9999", "Error consultando los pedidos, intente de nuevo");
        }

    }

    @Override
    public boolean statusOrden(UpdateStatusOrdenDTO req) {
        try {
            return ordenRepository.updateStatus(req.getCompany(), req.getStatus(), req.getIdOrden())
                    .orElse(false);

        } catch (Exception e) {
            log.error("Error actualizado el estado del pedido {}", e.getMessage(), e);
            return false;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrden(UpdateOrdenDTO req) {

        /// 1. Obtener la orden actual
        var orden = ordenRepository.getOrdenID(req.getCompany(), req.getIdOrden())
                .orElseThrow(() -> new EventoException(ERROR_INF_ORDEN, "La orden no existe: "+ req.getIdOrden()));

        /// 2. Validar estado
        if (orden.getStatus().equals("E") || orden.getStatus().equals("X")) {
            throw new EventoException(ERROR_STATUS_ORDEN," El estado no se puede actualizar: " + orden.getStatus());
        }

        ///  3. Actualizar la información de la orden
        var upd = ordenRepository.updateOrdenStatus(orden, req)
                .orElseThrow(() -> new EventoException(ERROR_UPDATE_ORDEN));

        ///  4. Obtener la información del cliente
        var client = clientRepository.infoClient(req.getCompany(), req.getPhone())
                .orElseThrow(() -> new EventoException(ERROR_INF_CLIENT, "Cliente no existe: "+req.getPhone()));

        ///  5. Obtener la información del cliente
        var updClient = clientRepository.updateClient(req.getCompany(), req.getPhone(), client.getNameClient(), client.getAddress(),
                        req.getName(), req.getAddress())
                .orElseThrow(() -> new EventoException(ERROR_UPDATE_CLIENT));

        if (req.isChangeProduct()) {

            ///  6. Elimina el detalle del pedido
            detailOrdenRepository.deleteOrdenProduct(req.getCompany(), req.getIdOrden());

            /// 7. Insertar todos los productos de nuevo
            InsertProduct(req.getProducts(), req.getCompany(), req.getIdOrden(), client.getIdClient());
        }

        return true;
    }

    private LocalDate parseOrdenDate(Integer ordenDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(String.valueOf(ordenDate), formatter);
    }

    private static String getStatus(BigDecimal subTotal, BigDecimal total) {

        if (subTotal.compareTo(total) == 0) {
            return PAGADO.getCodigo();
        }

        if (subTotal.compareTo(BigDecimal.ZERO) > 0) {
            return CONFIRMADO.getCodigo();
        }

        return PENDIENTE.getCodigo();
    }

    private void InsertProduct(List<ProductDTO> req, int company, Long ordenId, Long clientID) {
        for (ProductDTO prod : req) {

            var amount = prod.getUnitPrice().multiply(BigDecimal.valueOf(prod.getUnitValue()));

            detailOrdenRepository.insertOrdenProduct(
                    company,
                    ordenId.intValue(),
                    prod.getIdProducto(),
                    clientID.intValue(),
                    prod.getUnitValue(),
                    amount,
                    prod.getUnitPrice()

            );
        }
    }


}
