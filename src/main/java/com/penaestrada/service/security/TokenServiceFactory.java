package com.penaestrada.service.security;

public class TokenServiceFactory {

    private TokenServiceFactory() {
        throw new UnsupportedOperationException("Classe factory");
    }

    public static TokenService create() {
        return new TokenServiceImpl();
    }
}
