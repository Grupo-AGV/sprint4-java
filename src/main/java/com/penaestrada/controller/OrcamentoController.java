package com.penaestrada.controller;

import com.penaestrada.dto.CriarOrcamentoDto;
import com.penaestrada.dto.DetalhesOrcamentoDto;
import com.penaestrada.infra.CookieName;
import com.penaestrada.infra.exceptions.CpfInvalido;
import com.penaestrada.infra.exceptions.LoginNotFound;
import com.penaestrada.infra.exceptions.OrcamentoNotFound;
import com.penaestrada.infra.exceptions.VeiculoNotFound;
import com.penaestrada.infra.security.OficinaNotFound;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Usuario;
import com.penaestrada.service.OrcamentoService;
import com.penaestrada.service.OrcamentoServiceFactory;
import com.penaestrada.service.UsuarioService;
import com.penaestrada.service.UsuarioServiceFactory;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/estimate")
public class OrcamentoController {

    private final OrcamentoService orcamentoService = OrcamentoServiceFactory.create();
    private final TokenService tokenService = TokenServiceFactory.create();
    private final UsuarioService usuarioService = UsuarioServiceFactory.create();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agendarOrcamento(@CookieParam(CookieName.TOKEN) String token, CriarOrcamentoDto dto)    {
        try {
            Cargo cargo = Cargo.valueOf(tokenService.getCargo(token));
            if (cargo != Cargo.CLIENTE) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            String login = tokenService.getSubject(token);
            Cliente cliente = (Cliente) usuarioService.findByLogin(login);
            orcamentoService.agendarOrcamento(cliente, dto);
            return Response.status(Response.Status.CREATED).build();
        } catch (LoginNotFound | VeiculoNotFound | OficinaNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response detalhesOrcamento(@CookieParam(CookieName.TOKEN) String token, @QueryParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Id do orçamento não informado.")).build();
        }
        try {
            String login = tokenService.getSubject(token);
            Usuario usuario = usuarioService.findByLogin(login);
            DetalhesOrcamentoDto orcamento = orcamentoService.findByIdEUsuario(usuario, id);
            return Response.ok().entity(orcamento).build();
        } catch (LoginNotFound | OrcamentoNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (CpfInvalido e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}