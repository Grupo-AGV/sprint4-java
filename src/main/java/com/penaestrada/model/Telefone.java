package com.penaestrada.model;

public class Telefone {

    private Long id;
    private Usuario usuario;
    private Integer ddi;
    private Integer ddd;
    private Integer numero;

    public Telefone(Usuario usuario, Integer ddi, Integer ddd, Integer numero) {
        this.usuario = usuario;
        setDdi(ddi);
        setDdd(ddd);
        setNumero(numero);
    }

    public String getNumeroCompleto() {
        return String.format("+%d(%d)%d", getDdi(), getDdd(), getNumero());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDdi() {
        return ddi;
    }

    public void setDdi(Integer ddi) {
        this.ddi = ddi;
    }

    public Integer getDdd() {
        return ddd;
    }

    public void setDdd(Integer ddd) {
        this.ddd = ddd;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

}
