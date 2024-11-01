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
        String numeroFormatado;
        String numero = String.valueOf(getNumero());
        if (numero.length() == 9) {
            numeroFormatado = numero.substring(0, 5) + "-" + numero.substring(5);
        } else if (numero.length() == 8) {
            numeroFormatado = numero.substring(0, 4) + "-" + numero.substring(4);
        } else {
            numeroFormatado = numero;
        }
        return String.format("+%d (%d) %s", getDdd(), getDdi(), numeroFormatado);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
