package com.penaestrada.service;

public class OficinaServiceFactory {

    private OficinaServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static OficinaService create() {
        return new OficinaServiceImpl();
    }
}
