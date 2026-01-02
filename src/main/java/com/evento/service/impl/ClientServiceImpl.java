package com.evento.service.impl;

import com.evento.exception.EventoException;
import com.evento.model.*;
import com.evento.repository.ClientRepository;
import com.evento.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;



import static com.evento.ulti.EventoError.ORDEN_EMPTY;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public ClientInfo getInfoClient(int company, String phone) {

        return clientRepository.infoClient(company, phone)
                .orElseThrow(() -> {
                    log.info("No se encontraron pedidos en la base de datos para la empresa {}", company);
                    return new EventoException(ORDEN_EMPTY);
                });


    }
}