package com.penaestrada.model;

public class Servico {
    private String descricao;
    private Double valorMaoDeObra, valorPeca;
    private Peca peca;
    private Diagnostico diagnostico;

    public Servico(String descricao, Double valorMaoDeObra) { // SEM PEÇA
        this.descricao = descricao;
        this.valorMaoDeObra = valorMaoDeObra;
    }

    public Servico(String descricao, Double valorMaoDeObra, Double valorPeca, Peca peca) { // COM PEÇA
        this.descricao = descricao;
        this.valorMaoDeObra = valorMaoDeObra;
        this.valorPeca = valorPeca;
        this.peca = peca;
    }

    public Servico() {
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

    public Double getValorPeca() {
        return valorPeca;
    }

    public void setValorPeca(Double valorPeca) {
        this.valorPeca = valorPeca;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }
}
