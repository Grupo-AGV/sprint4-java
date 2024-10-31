package com.penaestrada.service;

public class OrcamentoServiceFactory {

    private OrcamentoServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static OrcamentoService create() {
        return new OrcamentoServiceImpl();
    }
}
