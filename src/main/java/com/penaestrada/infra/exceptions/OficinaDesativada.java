package com.penaestrada.infra.exceptions;

public class OficinaDesativada extends RuntimeException {
    public OficinaDesativada(String message) {
        super(message);
    }
}
