package com.penaestrada.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.penaestrada.model.Usuario;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

class TokenServiceImpl implements TokenService {

    private final String secret = "89367347c8f76a9195544a11a28b34325742028983394c8918996dc5ea899b23";

    @Override
    public String genToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Pe Na Estrada API")
                    .withSubject(usuario.getEmail())
                    .withClaim("role", usuario.getCargo().getDescricao())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("JWT Token: Generate token", e.getCause());
        }
    }

    @Override
    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Pe Na Estrada API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("JWT Token: Invalid/Expired!");
        }
    }

    @Override
    public String getCargo(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Pe Na Estrada API")
                    .build();

            return verifier.verify(token).getClaim("role").asString();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("JWT Token: Inválido ou expirado!");
        }
    }

    @Override
    public Instant expirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }
}
