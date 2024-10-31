package com.penaestrada.model;

public class Servico {
    private Long id;
    private String descricao;
    private Double valorMaoDeObra, valorPeca;
    private Peca peca;
    private Diagnostico diagnostico;

    public Servico(String descricao, Double valorMaoDeObra) { // SEM PEÇA
        this.descricao = descricao;
        this.valorMaoDeObra = valorMaoDeObra;
    }

    public Servico() {
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

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }
}
