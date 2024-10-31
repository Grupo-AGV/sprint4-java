package com.penaestrada.model;

import java.util.ArrayList;
import java.util.List;

public class Diagnostico {
    private Long id;
    private Orcamento orcamento;
    private String descricao;
    private List<Servico> servicosListados = new ArrayList<>();

    public Diagnostico(Orcamento orcamento, String descricao) {
        this.orcamento = orcamento;
        this.descricao = descricao;
    }

    public void adicionarServico(Servico servico) {
        this.servicosListados.add(servico);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
