package com.penaestrada.dao;

public class ServicoDaoFactory {

    private ServicoDaoFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static ServicoDao create() {
        return new ServicoDaoImpl();
    }
}
