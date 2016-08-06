/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.emissorboleto.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Leandro
 */
@Entity
@Table(name = "bol_titulo")
public class Titulo extends BaseModel {

    @Column(nullable = false)
    private Integer numero;
    @Column(nullable = false)
    private Integer parcela;
    @Column(nullable = false)
    private Double valor;
    @Column(columnDefinition = "date", name = "data_emissao", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataEmissao;
    @Column(columnDefinition = "date", name = "data_vencimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataVencimento;
    @Column(precision = 4000)
    private String instrucoes;
    @Column(name = "local_pagamento", precision = 4000)
    private String localPagamento;
    @Column(name = "nr_ano_abreviado", precision = 2)
    private String ano;
    @Lob
    @Column
    private byte[] boleto;
    @OneToOne
    private Cedente cedente;
    @OneToOne
    private Sacado sacado;
    @OneToOne
    private Sacado sacadorAvalista;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(String instrucoes) {
        this.instrucoes = instrucoes;
    }

    public String getLocalPagamento() {
        return localPagamento;
    }

    public void setLocalPagamento(String localPagamento) {
        this.localPagamento = localPagamento;
    }

    public Cedente getCedente() {
        return cedente;
    }

    public void setCedente(Cedente cedente) {
        this.cedente = cedente;
    }

    public Sacado getSacado() {
        return sacado;
    }

    public void setSacado(Sacado sacado) {
        this.sacado = sacado;
    }

    public Sacado getSacadorAvalista() {
        return sacadorAvalista;
    }

    public void setSacadorAvalista(Sacado sacadorAvalista) {
        this.sacadorAvalista = sacadorAvalista;
    }

    public byte[] getBoleto() {
        return boleto;
    }

    public void setBoleto(byte[] boleto) {
        this.boleto = boleto;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

}
