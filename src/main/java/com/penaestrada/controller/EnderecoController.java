package com.penaestrada.controller;

import com.penaestrada.dto.CriarEndereco;
import com.penaestrada.dto.DetalhesEndereco;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.model.Usuario;
import com.penaestrada.service.EnderecoService;
import com.penaestrada.service.EnderecoServiceFactory;
import com.penaestrada.service.UsuarioService;
import com.penaestrada.service.UsuarioServiceFactory;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/address")
public class EnderecoController {

    private TokenService tokenService = TokenServiceFactory.create();
    private UsuarioService usuarioService = UsuarioServiceFactory.create();
    private EnderecoService enderecoService = EnderecoServiceFactory.create();

    @POST
    @Path("")
    public Response criarEndereco(@CookieParam("pe_access_token") String token, CriarEndereco dto) {
        try {
            String login = tokenService.getSubject(token);
            Usuario usuario = usuarioService.findByLogin(login);
            enderecoService.adicionarEndereco(usuario, dto);
            return Response.status(Response.Status.CREATED).build();
        } catch (LoginNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarTodosOsEnderecos(@CookieParam("pe_access_token") String token) {
        try {
            String login = tokenService.getSubject(token);
            Usuario usuario = usuarioService.findByLogin(login);
            List<DetalhesEndereco> enderecos = enderecoService.listarEnderecosPorUsuario(usuario);
            return Response.status(Response.Status.OK).entity(enderecos).build();
        } catch (LoginNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
