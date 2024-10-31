package com.penaestrada.model;

import com.penaestrada.infra.exceptions.OficinaDesativada;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Orcamento {
    private Oficina oficina;
    private Veiculo veiculo;
    private String diagnosticoInicial;
    private LocalDateTime dataAgendamento, dataCriacao, dataFinalizacao;
    private Double valorFinal;

    private List<Servico> servicos = new ArrayList<>();

    public Orcamento(Oficina oficina, Veiculo veiculo, String diagnosticoInicial, LocalDateTime dataAgendamento) {
        if (isOficinaAtiva(oficina)) {
            this.oficina = oficina;
            this.veiculo = veiculo;
            this.diagnosticoInicial = diagnosticoInicial;
            this.dataCriacao = LocalDateTime.now();
            this.dataAgendamento = dataAgendamento;
        } else {
            throw new OficinaDesativada("Esta oficina esta desativada ou indisponível para reparos!");
        }
    }

    public Orcamento(Veiculo veiculo, String diagnosticoInicial, LocalDateTime dataAgendamento, LocalDateTime dataCriacao, LocalDateTime dataFinalizacao, Double valorFinal) {
        this.veiculo = veiculo;
        this.diagnosticoInicial = diagnosticoInicial;
        this.dataAgendamento = dataAgendamento;
        this.dataCriacao = dataCriacao;
        this.dataFinalizacao = dataFinalizacao;
        this.valorFinal = valorFinal;
    }

    private boolean isOficinaAtiva(Oficina oficina) {
        return oficina.getStatus().equals('A'); // A de Ativo
    }

    public Cliente getUsuario() {
        return veiculo.getCliente();
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public String getDiagnosticoInicial() {
        return diagnosticoInicial;
    }

    public void setDiagnosticoInicial(String diagnosticoInicial) {
        this.diagnosticoInicial = diagnosticoInicial;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
}
