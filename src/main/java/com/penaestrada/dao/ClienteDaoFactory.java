package com.penaestrada.dao;

public class ClienteDaoFactory {

    private ClienteDaoFactory()  {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static ClienteDao create() {
        return new ClienteDaoImpl();
    }
}
