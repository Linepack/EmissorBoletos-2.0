/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.emissorboleto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Leandro
 */
@Entity
@Table(name = "bol_sacado")
public class Sacado extends BaseModel {

    @Column(nullable = false, name = "nm_nome")
    private String nome;
    @Column(name = "nr_cpf_cnpj", nullable = false)
    private String cpfOuCnpj;
    @Column(nullable = false, name = "sg_uf")
    private String uf;
    @Column(nullable = false, name = "nm_cidade")
    private String cidade;
    @Column(nullable = false, name = "ds_cep")
    private String cep;
    @Column(nullable = false, name = "ds_bairro")
    private String bairro;
    @Column(nullable = false, name = "ds_logradouro")
    private String logradouro;
    @Column(nullable = false, name = "ds_numero_logradouro")
    private String numero;
    @OneToOne
    private Conta conta;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

}
