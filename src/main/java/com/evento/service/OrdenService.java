package com.evento.service;

import com.evento.model.OrdenCreate;
import com.evento.model.OrdenDTO;


public interface OrdenService {
    /**
     *
     * @param req
     * @return
     */
    OrdenCreate createOrden(OrdenDTO req);
}
