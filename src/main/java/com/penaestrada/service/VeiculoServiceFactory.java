package com.penaestrada.service;

public class VeiculoServiceFactory {

    private VeiculoServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static VeiculoService create() {
        return new VeiculoServiceImpl();
    }
}
