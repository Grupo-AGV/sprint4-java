package com.penaestrada.service;

public class ClienteServiceFactory {

    private ClienteServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static ClienteService create() {
        return new ClienteServiceImpl();
    }
}
