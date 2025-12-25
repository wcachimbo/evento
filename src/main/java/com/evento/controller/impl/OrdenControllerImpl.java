package com.evento.controller.impl;

import com.evento.controller.OrdenController;
import com.evento.model.ApiResponse;
import com.evento.model.OrdenDTO;
import com.evento.service.OrdenService;
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
                "Consulta exitosa",
                orden
        );
        log.info("Se creo pedido exitosamente");

        return ResponseEntity.ok(resp);
    }
}
