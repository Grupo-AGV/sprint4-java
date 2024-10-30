package com.penaestrada.dao;

public class OficinaDaoFactory {

    private OficinaDaoFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static OficinaDao create() {
        return new OficinaDaoImpl();
    }
}
