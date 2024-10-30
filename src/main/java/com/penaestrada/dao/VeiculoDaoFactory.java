package com.penaestrada.dao;

public class VeiculoDaoFactory {
    private VeiculoDaoFactory()  {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static VeiculoDao create() {
        return new VeiculoDaoImpl();
    }
}
