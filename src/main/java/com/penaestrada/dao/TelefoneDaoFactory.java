package com.penaestrada.dao;

public class TelefoneDaoFactory {

    private TelefoneDaoFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static TelefoneDao create() {
        return new TelefoneDaoImpl();
    }
}
