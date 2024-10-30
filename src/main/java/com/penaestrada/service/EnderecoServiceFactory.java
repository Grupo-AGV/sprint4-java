package com.penaestrada.service;

public class EnderecoServiceFactory {

    private EnderecoServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static EnderecoService create() {
        return new EnderecoServiceImpl();
    }
}
