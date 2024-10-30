package com.penaestrada.dao;

public class EnderecoDaoFactory {

    private EnderecoDaoFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static EnderecoDao create() {
        return new EnderecoDaoImpl();
    }
}
