package com.penaestrada.controller;

import com.penaestrada.dto.CriarTelefoneDto;
import com.penaestrada.infra.CookieName;
import com.penaestrada.infra.exceptions.ExclusaoTelefoneUnico;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.infra.exceptions.TelefoneNotFound;
import com.penaestrada.model.Usuario;
import com.penaestrada.service.TelefoneService;
import com.penaestrada.service.TelefoneServiceFactory;
import com.penaestrada.service.UsuarioService;
import com.penaestrada.service.UsuarioServiceFactory;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/contact")
public class TelefoneController {

    private final TokenService tokenService = TokenServiceFactory.create();
    private final UsuarioService usuarioService = UsuarioServiceFactory.create();
    private final TelefoneService telefoneService = TelefoneServiceFactory.create();

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarTelefone(@CookieParam(CookieName.TOKEN) String token, CriarTelefoneDto dto) {
        try {
            String login = tokenService.getSubject(token);
            Usuario usuario = usuarioService.findByLogin(login);
            telefoneService.adicionarTelefone(usuario, dto);
            return Response.status(Response.Status.CREATED).build();
        } catch (LoginNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @PUT
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarTelefone(@CookieParam(CookieName.TOKEN) String token, @QueryParam("id") Long id, CriarTelefoneDto dto) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Id do telefone não informado.")).build();
        }
        try {
            String login = tokenService.getSubject(token);
            Usuario usuario = usuarioService.findByLogin(login);
            telefoneService.atualizarTelefone(usuario, id, dto);
            return Response.status(Response.Status.OK).build();
        } catch (LoginNotFound | TelefoneNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletarTelefone(@CookieParam(CookieName.TOKEN) String token, @QueryParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Id do veículo não informado")).build();
        }
        try {
            String login = tokenService.getSubject(token);
            Usuario usuario = usuarioService.findByLogin(login);
            telefoneService.removeTelefoneDoUsuario(usuario, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (TelefoneNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (ExclusaoTelefoneUnico e) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("error", "Você não pode excluir seu uníco telefone.")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
