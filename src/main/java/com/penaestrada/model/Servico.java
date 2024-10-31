package com.penaestrada.model;

import java.time.LocalDateTime;

public class Servico {
    private Long id;
    private Orcamento orcamento;
    private String descricao;
    private Double valorMaoDeObra, valorPeca;
    private Peca peca;
    private LocalDateTime dataCriacao;

    public Servico(String descricao, Double valorMaoDeObra, LocalDateTime dataCriacao) { // SEM PEÇA
        this.descricao = descricao;
        this.valorMaoDeObra = valorMaoDeObra;
        this.dataCriacao = dataCriacao;
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

    public Double getValorMaoDeObra() {
        return valorMaoDeObra;
    }

    public void setValorMaoDeObra(Double valorMaoDeObra) {
        this.valorMaoDeObra = valorMaoDeObra;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }
}
