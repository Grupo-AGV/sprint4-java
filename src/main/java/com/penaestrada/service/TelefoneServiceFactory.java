package com.penaestrada.service;

public class TelefoneServiceFactory {

    private TelefoneServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static TelefoneService create() {
        return new TelefoneServiceImpl();
    }
}
