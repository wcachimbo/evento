package com.evento.exception;

import lombok.Getter;

@Getter
public class EventoException extends RuntimeException {

    private final String code;

    public EventoException(String code, String message) {
        super(message);
        this.code = code;
    }
}
