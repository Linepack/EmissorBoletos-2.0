/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.emissorboleto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Leandro
 */
@Entity
@Table(name = "bol_conta")
public class Conta extends BaseModel {

    @Column(nullable = false, name = "cd_bco")
    private Integer banco;
    @Column(nullable = false, name = "nr_agencia")
    private String agencia;
    @Column(name = "ds_dv_agencia", nullable = false)
    private String digitoAgencia;
    @Column(nullable = false, name = "nr_conta")
    private String conta;
    @Column(name = "ds_carteira")
    private String carteira;
    @Column(name = "nr_tamanho_nosso_numero")
    private Integer tamanhoNossoNumero;
    @Column(name = "ds_posto_agencia")
    private String postoAgencia;
    @Column(name = "ds_byte_cedente")
    private String byteCedente;

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getDigitoAgencia() {
        return digitoAgencia;
    }

    public void setDigitoAgencia(String digitoAgencia) {
        this.digitoAgencia = digitoAgencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public Integer getTamanhoNossoNumero() {
        return tamanhoNossoNumero;
    }

    public void setTamanhoNossoNumero(Integer tamanhoNossoNumero) {
        this.tamanhoNossoNumero = tamanhoNossoNumero;
    }

    public String getPostoAgencia() {
        return postoAgencia;
    }

    public void setPostoAgencia(String postoAgencia) {
        this.postoAgencia = postoAgencia;
    }

    public String getByteCedente() {
        return byteCedente;
    }

    public void setByteCedente(String byteCedente) {
        this.byteCedente = byteCedente;
    }
    
    
}
