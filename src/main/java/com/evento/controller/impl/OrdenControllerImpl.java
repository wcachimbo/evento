package com.evento.controller.impl;

import com.evento.controller.OrdenController;
import com.evento.model.ApiResponse;
import com.evento.model.OrdenDTO;
import com.evento.model.UpdateOrdenDTO;
import com.evento.model.UpdateStatusOrdenDTO;
import com.evento.service.OrdenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OrdenControllerImpl implements OrdenController {


    private final OrdenService ordenService;

    @Override
    public ResponseEntity<ApiResponse> createOrden(OrdenDTO req) {

        var orden = ordenService.createOrden(req);
        var resp = new ApiResponse<>(
                "0000",
                "Se creo pedido exitoso",
                orden
        );
        log.info("Se creo pedido exitosamente");

        return ResponseEntity.ok(resp);
    }


    @Override
    public ResponseEntity<ApiResponse> getOrdenPending(Long company) {

        var orden = ordenService.getPendingOrden(company);
        var resp = new ApiResponse<>(
                "0000",
                "Consulta de pedidos exitosa",
                orden
        );
        log.info("Consulta de pedidos exitosa");

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<@NonNull ApiResponse> updateStatusOrden(UpdateStatusOrdenDTO req) {

        var orden = ordenService.statusOrden(req);
        var resp = new ApiResponse<>(
                "0000",
                "Actualización de pedido correctamente",
                orden
        );
        log.info("Actualización de pedido correctamente");

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<@NonNull ApiResponse> updateOrden(UpdateOrdenDTO req) {

        var orden = ordenService.updateOrden(req);
        var resp = new ApiResponse<>(
                "0000",
                "Actualización de pedido correctamente",
                orden
        );
        log.info("Actualización de los datos del pedido correctamente");

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<@NonNull ApiResponse> getOrdenCollect(Long company) {

        var orden = ordenService.getCollectOrden(company);
        var resp = new ApiResponse<>(
                "0000",
                "Consulta exitosa de pedidos pendientes por entregar ",
                orden
        );
        log.info("Consulta exitosa de pedidos pendientes por entregar");

        return ResponseEntity.ok(resp);
    }

}
