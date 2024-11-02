package com.penaestrada.controller;

import com.penaestrada.config.DatabaseConnectionFactory;
import com.penaestrada.dto.ClienteDashboardDto;
import com.penaestrada.dto.CriarClienteDto;
import com.penaestrada.dto.CriarVeiculoDto;
import com.penaestrada.dto.IniciarChatBot;
import com.penaestrada.infra.CookieName;
import com.penaestrada.infra.exceptions.*;
import com.penaestrada.model.Cargo;
import com.penaestrada.model.Cliente;
import com.penaestrada.model.Veiculo;
import com.penaestrada.service.*;
import com.penaestrada.service.security.TokenService;
import com.penaestrada.service.security.TokenServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Path("/client")
public class ClienteController {

    private final ClienteService clienteService = ClienteServiceFactory.create();
    private final UsuarioService usuarioService = UsuarioServiceFactory.create();
    private final VeiculoService veiculoService = VeiculoServiceFactory.create();
    private final TokenService tokenService = TokenServiceFactory.create();

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(CriarClienteDto dto) {
        try {
            Cliente cliente = new Cliente(dto.name(), dto.cpf(), dto.birthDate(), dto.login().email(), dto.login().password(), Cargo.CLIENTE);
            CriarVeiculoDto veiculoDto = dto.vehicle();
            Veiculo veiculo = new Veiculo(cliente, veiculoDto.brand(), veiculoDto.model(), veiculoDto.licensePlate(), Integer.valueOf(veiculoDto.year()));
            cliente.getVeiculos().add(veiculo);
            clienteService.create(cliente);
            return Response.status(Response.Status.CREATED).build();
        } catch (EmailExistente | VeiculoExistente | CpfExistente e) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("error", e.getMessage())).build();
        } catch (CpfInvalido e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            try (Connection connection = DatabaseConnectionFactory.create()) {
                connection.rollback();
            } catch (SQLException ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("error", "Erro ao realizar rollback")).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("/dashboard")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dashboard(@CookieParam(CookieName.TOKEN) String token) {
        try {
            String login = tokenService.getSubject(token);
            ClienteDashboardDto dashboard = clienteService.dashboard(login);
            return Response.ok().entity(dashboard).build();
        } catch (ClienteNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/vehicle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVehicle(@CookieParam(CookieName.TOKEN) String token, CriarVeiculoDto dto) {
        try {
            String login = tokenService.getSubject(token);
            Cliente cliente = (Cliente) usuarioService.findByLogin(login);
            Veiculo veiculo = new Veiculo(cliente, dto.brand(), dto.model(), dto.licensePlate(), Integer.valueOf(dto.year()));
            veiculoService.adionarVeiculoAoCliente(veiculo);
            return Response.status(Response.Status.CREATED).build();
        } catch (VeiculoExistente e) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVehicle(@CookieParam(CookieName.TOKEN) String token, @QueryParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Id do veículo não informado")).build();
        }
        try {
            String login = tokenService.getSubject(token);
            Cliente cliente = (Cliente) usuarioService.findByLogin(login);
            veiculoService.removerVeiculoDoCliente(cliente, id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (VeiculoNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (ExclusaoVeiculoUnico e) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("error", "Você não pode excluir seu uníco veículo.")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("/chatbot/init")
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarChat(@CookieParam(CookieName.TOKEN) String token) {
        try {
            Cargo cargo = Cargo.valueOf(tokenService.getCargo(token));
            if (cargo != Cargo.CLIENTE) {
                return Response.status(Response.Status.FORBIDDEN).entity(Map.of("error", "Acesso negado.")).build();
            }
            String login = tokenService.getSubject(token);
            Cliente cliente = (Cliente) usuarioService.findByLogin(login);
            IniciarChatBot response = clienteService.iniciarChatBot(cliente);
            return Response.ok().entity(response).build();
        }  catch (LoginNotFound e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }
}
