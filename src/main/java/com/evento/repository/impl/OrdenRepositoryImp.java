package com.evento.repository.impl;

import com.evento.exception.EventoException;
import com.evento.model.OrdenDetail;
import com.evento.model.OrdenQuerys;
import com.evento.model.Product;
import com.evento.model.UpdateOrdenDTO;
import com.evento.repository.OrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

import static com.evento.ulti.EventoError.*;

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
                                COALESCE(SUM(d.quantity) FILTER (WHERE o.orden_date = :date), 0) AS total_quantity,
                                (s.quantity - COALESCE(SUM(d.quantity) FILTER (WHERE o.orden_date = :date),0)) AS available
                            FROM stock s
                            JOIN product p
                                ON p.id_product = s.product_id
                            LEFT JOIN detail_orden d
                                ON d.product_id = p.id_product
                            LEFT JOIN "orden" o
                                ON o.id_orden = d.orden_id
                               AND o.company = :company
                            
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

    @Override
    public Optional<Long> insertOrden(
            int company,
            Long clientId,
            Integer ordenDate,
            String status,
            BigDecimal subtotal,
            BigDecimal total,
            String description
    ) {
        try {
            return jdbcClient.sql("""
                            INSERT INTO "orden"
                            (company, client_id, orden_date, status, subtotal, total, description)
                            VALUES
                            (:company, :clientId,:ordenDate, :status, :subtotal, :total, :description)
                            RETURNING id_orden
                            """)
                    .param("company", company)
                    .param("clientId", clientId)
                    .param("ordenDate", ordenDate)
                    .param("status", status)
                    .param("subtotal", subtotal)
                    .param("total", total)
                    .param("description", description)
                    .query(Long.class)
                    .optional();
        } catch (Exception e) {
            log.error("Error insertando la orden - {}", e.getMessage(), e);
            throw new EventoException(ORDEN_NO_CREADA, e.getMessage());
        }
    }

    @Override
    public List<OrdenDetail> getOrdenPending(Long company) {
        try {
            return jdbcClient.sql("""
                            SELECT
                                /* ORDEN */
                                o.id_orden          AS idOrden,
                                o.company           AS company,
                                o.orden_date        AS ordenDate,
                                c.address           AS address,
                                c.name_client       AS nameClient,
                                o.status            AS status,
                                c.phone             AS phone,
                                o.subtotal          AS subtotal,
                                o.total             AS total,
                                o.description       AS description,
                            
                                /* DETAIL_ORDEN */
                                d.id_detail         AS idDetail,
                                d.product_id        AS productId,
                                d.quantity          AS quantity,
                                d.priceunit         AS priceUnit,
                                d.amount            AS amount,
                            
                                /* PRODUCT */
                                p.nombre            AS nombreProducto,
                                p.description       AS productDescription
                            
                            FROM public."orden" o
                            JOIN detail_orden d
                                ON d.orden_id = o.id_orden
                               AND d.company  = o.company
                            JOIN client c
                                ON c.id_client = d.client_id
                               AND c.company   = d.company
                            JOIN product p
                                ON p.id_product = d.product_id
                               AND p.company    = d.company
                            WHERE o.status IN ('C','P','G')
                              AND o.company = :company
                            ORDER BY o.create_date DESC, o.id_orden
                            """)
                    .param("company", company)
                    .query(OrdenDetail.class)
                    .list();
        } catch (Exception e) {
            log.error("Error obteniendo pedidos", e);
            throw new EventoException("9999", "Error  obteniendo pedidos");
        }
    }

    public Optional<Boolean> updateStatus(int company, String status, Long idOrden) {
        try {
            int rowsUpdated = jdbcClient.sql("""
                            UPDATE orden
                            SET status = :status
                            WHERE id_orden = :idOrden
                              AND company = :company
                            """)
                    .param("status", status)
                    .param("idOrden", idOrden)
                    .param("company", company)
                    .update();

            return rowsUpdated > 0
                    ? Optional.of(Boolean.TRUE)
                    : Optional.empty();

        } catch (Exception e) {
            log.error("Error actualizando el estado del pedido [company={}, idOrden={}]",
                    company, idOrden, e);
            return Optional.of(false);
        }
    }

    @Override
    public Optional<OrdenQuerys> getOrdenID(int company, Long idOrden) {
        try {
            return jdbcClient.sql("""
                            SELECT
                                o.id_orden        AS idOrden,
                                o.company,
                                o.orden_date      AS date,
                                o.status,
                                o.description,
                                o.subtotal,
                                o.total,
                                c.name_client     AS clientName,
                                c.phone,
                                c.address
                            FROM orden o
                            JOIN client c
                                ON c.id_client = o.client_id
                            WHERE o.id_orden = :idOrden
                              AND o.company  = :company;
                            """)
                    .param("idOrden", idOrden)
                    .param("company", company)
                    .query(OrdenQuerys.class)
                    .optional();

        } catch (Exception e) {
            log.error("Error actualizando el estado del pedido [company={}, idOrden={}]",
                    company, idOrden, e);
            throw new EventoException("", "");
        }
    }

    @Override
    public Optional<Boolean> updateOrdenStatus(OrdenQuerys detailOld, UpdateOrdenDTO detailNew) {
        try {
            StringBuilder sql = new StringBuilder("UPDATE orden SET ");
            Map<String, Object> params = new HashMap<>();

            boolean hasChanges = false;

            // Fecha
            if (!Objects.equals(detailOld.getDate(), detailNew.getDate())) {
                sql.append("orden_date = :ordenDate, ");
                params.put("ordenDate", detailNew.getDate());
                hasChanges = true;
            }

            // Descripción
            if (!Objects.equals(detailOld.getDescription(), detailNew.getDescription())) {
                sql.append("description = :description, ");
                params.put("description", detailNew.getDescription());
                hasChanges = true;
            }

            // Subtotal
            if (detailOld.getSubTotal().compareTo(detailNew.getSubTotal()) != 0) {
                sql.append("subtotal = :subtotal, ");
                params.put("subtotal", detailNew.getSubTotal());
                hasChanges = true;
            }

            // Total
            if (detailOld.getTotal().compareTo(detailNew.getTotal()) != 0) {
                sql.append("total = :total, ");
                params.put("total", detailNew.getTotal());
                hasChanges = true;
            }

            // Si no hubo cambios
            if (!hasChanges) {
                return Optional.of(Boolean.TRUE);
            }

            // Quitar última coma
            sql.setLength(sql.length() - 2);

            sql.append(" WHERE id_orden = :idOrden AND company = :company");

            params.put("idOrden", detailOld.getIdOrden());
            params.put("company", detailOld.getCompany());

            int rowsUpdated = jdbcClient.sql(sql.toString())
                    .params(params)
                    .update();

            return Optional.of(rowsUpdated > 0);
        } catch (Exception e) {
            log.error(
                    "Error actualizando pedido [company={}, idOrden={}]",
                    detailOld.getIdOrden(), detailOld.getCompany(), e);
            throw new EventoException(ERROR_UPDATE_ORDEN);
        }
    }

    @Override
    public List<OrdenDetail> getOrdenCollect(Long company) {
        try {
            return jdbcClient.sql("""
                            SELECT
                                 o.id_orden          AS idOrden,
                                 o.company           AS company,
                                 o.orden_date        AS ordenDate,
                                 c.address           AS address,
                                 c.name_client       AS nameClient,
                                 o.status            AS status,
                                 c.phone             AS phone,
                                 o.subtotal          AS subtotal,
                                 o.total             AS total,
                                 o.description       AS description,
                                 d.id_detail         AS idDetail,
                                 d.product_id        AS productId,
                                 d.quantity          AS quantity,
                                 d.priceunit         AS priceUnit,
                                 d.amount            AS amount,
                                 p.nombre            AS nombreProducto,
                                 p.description       AS productDescription
                             FROM public."orden" o
                             JOIN detail_orden d
                                 ON d.orden_id = o.id_orden
                                AND d.company  = o.company
                             JOIN client c
                                 ON c.id_client = d.client_id
                                AND c.company   = d.company
                             JOIN product p
                                 ON p.id_product = d.product_id
                                AND p.company    = d.company
                             WHERE o.status = 'E'
                               AND o.company = :company
                             ORDER BY o.create_date DESC, o.id_orden;
                            """)
                    .param("company", company)
                    .query(OrdenDetail.class)
                    .list();
        } catch (Exception e) {
            log.error("Error obteniendo pedidos", e);
            throw new EventoException("9999", "Error  obteniendo pedidos");
        }
    }

}