package com.penaestrada.service;

public class ServicoServiceFactory {

    private ServicoServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static ServicoService create() {
        return new ServicoServiceImpl();
    }
}
