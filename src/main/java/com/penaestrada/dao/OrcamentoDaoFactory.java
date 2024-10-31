package com.penaestrada.dao;

public class OrcamentoDaoFactory {

    private OrcamentoDaoFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static OrcamentoDao create() {
        return new OrcamentoDaoImpl();
    }
}
