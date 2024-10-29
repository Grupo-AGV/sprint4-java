package com.penaestrada.service.security;

import com.penaestrada.model.Usuario;

import java.time.Instant;

public interface TokenService {

    String genToken(Usuario usuario);

    String getSubject(String token);

    String getCargo(String token);

    Instant expirationDate();

}
