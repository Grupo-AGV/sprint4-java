package com.penaestrada.controller;

import com.penaestrada.dto.LoginDto;
import com.penaestrada.infra.exceptions.LoginInvalido;
import com.penaestrada.model.Usuario;
import com.penaestrada.service.UsuarioService;
import com.penaestrada.service.UsuarioServiceFactory;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Map;

@Path("/auth")
public class AutenticacaoController {

    private final TokenService tokenService = TokenServiceFactory.create();
    private final UsuarioService usuarioService = UsuarioServiceFactory.create();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDto dto) {
        try {
            Usuario usuario = usuarioService.logarUsuario(dto.email(), dto.password());
            usuario.setEmail(dto.email());
            String token = tokenService.genToken(usuario);
            NewCookie cookie = new NewCookie("pe_access_token", token, "/", null, null, tokenService.expirationDate().getNano(), true, true);
            return Response.status(Response.Status.OK).entity(Map.of("token", token)).cookie(cookie).build();
        } catch (LoginInvalido e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("error", e.getMessage())).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
