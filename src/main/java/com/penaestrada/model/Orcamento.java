package com.penaestrada.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Orcamento {
    private Oficina oficina;
    private Veiculo veiculo;
    private LocalDate dataInicio, dataEntrega;

    private LocalDate dataFinalizacao;
    private Double valorTotal, valorDesconto;
    private List<Diagnostico> diagnosticos = new ArrayList<>();

    public Orcamento(Oficina oficina, Veiculo veiculo, String dataEntrega, Diagnostico diagnostico) {
        if (isOficinaAtiva(oficina)) {
            this.oficina = oficina;
            this.veiculo = veiculo;
            this.dataInicio = LocalDate.now();
            this.dataEntrega = LocalDate.parse(dataEntrega);
            addDiagnostico(diagnostico);
        } else {
            throw new RuntimeException("Esta oficina esta desativada ou indisponível para reparos!");
        }
    }

    private boolean isOficinaAtiva(Oficina oficina) {
        return oficina.getStatus().equals('A'); // A de Ativo
    }

    public Orcamento() {
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Cliente getUsuario() {
        return veiculo.getCliente();
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void finalizarOrcamento() {
        this.dataFinalizacao = LocalDate.now();
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public List<Diagnostico> getDiagnosticos() {
        return diagnosticos;
    }

    public void addDiagnostico(Diagnostico diagnostico) {
        this.diagnosticos.add(diagnostico);
        diagnostico.setOrcamento(this);
    }
}
