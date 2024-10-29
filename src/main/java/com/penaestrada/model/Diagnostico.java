package com.penaestrada.model;

import java.util.ArrayList;
import java.util.List;

public class Diagnostico {
    private List<Servico> servicosListados = new ArrayList<>();
    private String descricao;
    private Orcamento orcamento;

    public Diagnostico(String descricao) {
        this.descricao = descricao;
    }

    public Diagnostico(String descricao, Servico servico) {
        this.descricao = descricao;
        adicionarServico(servico);
    }

    public void adicionarServico(Servico servico) {
        this.servicosListados.add(servico);
        servico.setDiagnostico(this);
    }

    public Diagnostico() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Servico> getServicosListados() {
        return servicosListados;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }
}
