package com.penaestrada.controller;

import com.penaestrada.dto.CriarOficinaDto;
import com.penaestrada.dto.DetalhesOficinaDto;
import com.penaestrada.infra.CookieName;
import com.penaestrada.infra.exceptions.EmailExistente;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.infra.exceptions.OficinaNotFound;
import com.penaestrada.model.*;
import com.penaestrada.service.OficinaService;
import com.penaestrada.service.OficinaServiceFactory;
import com.penaestrada.service.UsuarioService;
import com.penaestrada.service.UsuarioServiceFactory;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("/workshop")
public class OficinaController {

    private final TokenService tokenService = TokenServiceFactory.create();
    private final OficinaService oficinaService = OficinaServiceFactory.create();
    private final UsuarioService usuarioService = UsuarioServiceFactory.create();

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarOficina(CriarOficinaDto dto) {
        try {
            oficinaService.create(dto);
            return Response.status(Response.Status.CREATED).build();
        } catch (EmailExistente e) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarOficinas() {
        try {
            List<DetalhesOficinaDto> oficinas = oficinaService.listarOficinas();
            return Response.ok(oficinas).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("/details")
    @Produces(MediaType.APPLICATION_JSON)
    public Response detalhesOficina(@CookieParam(CookieName.TOKEN) String token) {
        try {
            Cargo cargo = Cargo.valueOf(tokenService.getCargo(token));
            if (cargo != Cargo.OFICINA) {
                return Response.status(Response.Status.FORBIDDEN).entity(Map.of("error", "Acesso negado.")).build();
            }
            String login = tokenService.getSubject(token);
            Usuario usuario = usuarioService.findByLogin(login);
            DetalhesOficinaDto oficina = oficinaService.detalhesOficinaPorUsuario(usuario);
            return Response.ok(oficina).build();
        } catch (LoginNotFound | OficinaNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
