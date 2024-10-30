package com.penaestrada.dao;

public class UsuarioDaoFactory {

    private UsuarioDaoFactory()  {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static UsuarioDao create() {
        return new UsuarioDaoImpl();
    }
}
