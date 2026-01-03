package com.evento.controller.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {
    /**
     * Endpoint para verificar el estado del servicio.
     *
     * @return Mensaje indicando que el servicio está activo.
     */
    @GetMapping("/status/service")
    public String health() {
        log.info("Health check endpoint called.");
        return "Service Up!!!!";
    }
}
