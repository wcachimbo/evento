package com.evento.exception;

import com.evento.ulti.EventoError;
import lombok.Getter;

@Getter
public class EventoException extends RuntimeException {

    private final String code;

    public EventoException(String code, String message) {
        super(message);
        this.code = code;
    }

    public EventoException(EventoError error, String customMessage) {
        super(customMessage);
        this.code = error.getCode();
    }

    public EventoException(EventoError err) {
        super(err.getMessage());
        this.code = err.getCode();
    }
}
