package com.penaestrada.controller;

import com.penaestrada.dto.CriarOrcamentoDto;
import com.penaestrada.infra.CookieName;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Cliente;
import com.penaestrada.service.OrcamentoService;
import com.penaestrada.service.OrcamentoServiceFactory;
import com.penaestrada.service.UsuarioService;
import com.penaestrada.service.UsuarioServiceFactory;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/estimate")
public class OrcamentoController {

    private final OrcamentoService orcamentoService = OrcamentoServiceFactory.create();
    private final TokenService tokenService = TokenServiceFactory.create();
    private final UsuarioService usuarioService = UsuarioServiceFactory.create();

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agendarOrcamento(@CookieParam(CookieName.TOKEN) String token, CriarOrcamentoDto dto) {
        try {
            Cargo cargo = Cargo.valueOf(tokenService.getCargo(token));
            if (cargo != Cargo.CLIENTE) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            String login = tokenService.getSubject(token);
            Cliente cliente = (Cliente) usuarioService.findByLogin(login);
            orcamentoService.agendarOrcamento(cliente, dto);
            return Response.status(Response.Status.CREATED).build();
        } catch (LoginNotFound e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
