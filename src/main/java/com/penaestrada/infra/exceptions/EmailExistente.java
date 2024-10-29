package com.penaestrada.infra.exceptions;

public class EmailExistente extends Exception {
    public EmailExistente(String message) {
        super(message);
    }
}
