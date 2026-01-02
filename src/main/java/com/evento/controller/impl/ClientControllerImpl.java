package com.evento.controller.impl;

import com.evento.controller.OrdenController;
import com.evento.model.ApiResponse;
import com.evento.model.OrdenDTO;
import com.evento.model.UpdateOrdenDTO;
import com.evento.model.UpdateStatusOrdenDTO;
import com.evento.service.ClientService;
import com.evento.service.OrdenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ClientControllerImpl implements com.evento.controller.ClientController {


    private final ClientService clientService;

    @Override
    public ResponseEntity<ApiResponse> getInfoClient(int company, String phone) {

        var client = clientService.getInfoClient(company, phone);
        var resp = new ApiResponse<>(
                "0000",
                "Consulta de cliente exitosa",
                client
        );
        log.info("Consulta del usuario con teléfono {} realizada correctamente", phone);

        return ResponseEntity.ok(resp);
    }

}
