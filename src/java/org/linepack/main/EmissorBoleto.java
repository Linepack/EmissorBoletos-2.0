/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.main;

import java.math.BigDecimal;
import java.util.Arrays;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Banco;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.CodigoDeCompensacaoBACEN;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.linepack.dao.CedenteDAO;
import org.linepack.dao.SacadoDAO;
import org.linepack.dao.TituloDAO;
import org.linepack.util.BancoBrasilUtil;
import org.linepack.util.BancoSicrediUtil;
import org.linepack.util.StringUtil;

/**
 *
 * @author Leandro
 */
public class EmissorBoleto {

    public Boleto getBoletoStream(Integer tituloId) {
        org.linepack.model.Titulo tituloModel = this.getTituloModelById(tituloId);
        org.linepack.model.Cedente cedenteModel = this.getCedenteModelById(tituloModel.getCedente().getId());
        org.linepack.model.Sacado sacadoModel = this.getSacadoModelById(tituloModel.getSacado().getId());

        if (tituloModel.getSacadorAvalista() != null) {
            org.linepack.model.Sacado sacadorAvalista = this.getSacadoModelById(tituloModel.getSacadorAvalista().getId());
        }

        Boleto boleto = this.createBoleto(cedenteModel, sacadoModel, sacadoModel, tituloModel);        
        return boleto;
    }

    private org.linepack.model.Titulo getTituloModelById(Integer id) {
        org.linepack.model.Titulo tituloModel = new org.linepack.model.Titulo();
        TituloDAO tituloDAO = new TituloDAO();
        tituloModel = tituloDAO.getByID(id);
        return tituloModel;
    }

    private org.linepack.model.Cedente getCedenteModelById(Integer id) {
        org.linepack.model.Cedente cedenteModel = new org.linepack.model.Cedente();
        CedenteDAO cedenteDAO = new CedenteDAO();
        cedenteModel = cedenteDAO.getByID(id);
        return cedenteModel;
    }

    private org.linepack.model.Sacado getSacadoModelById(Integer id) {
        org.linepack.model.Sacado sacadoModel = new org.linepack.model.Sacado();
        SacadoDAO sacadoDAO = new SacadoDAO();
        sacadoModel = sacadoDAO.getByID(id);
        return sacadoModel;
    }

    private Boleto createBoleto(
            org.linepack.model.Cedente cedenteModel,
            org.linepack.model.Sacado sacadoModel,
            org.linepack.model.Sacado sacadorAvalistaModel,
            org.linepack.model.Titulo tituloModel) {

        Cedente cedente = new Cedente(cedenteModel.getNome(), cedenteModel.getCnpj());

        Sacado sacado = new Sacado(sacadoModel.getNome(), sacadoModel.getCpfOuCnpj());
        sacado.addEndereco(this.getEnderecoBySacadoModel(sacadoModel));

        SacadorAvalista sacadorAvalista = null;
        if (sacadorAvalistaModel != null) {
            sacadorAvalista = new SacadorAvalista(sacadorAvalistaModel.getNome(), sacadorAvalistaModel.getCpfOuCnpj());
            sacadorAvalista.addEndereco(this.getEnderecoBySacadoModel(sacadorAvalistaModel));
        }

        CodigoDeCompensacaoBACEN codigoDeCompensacaoBACENCedente = new CodigoDeCompensacaoBACEN(cedenteModel.getConta().getBanco());
        Banco bancoCedente = new Banco(codigoDeCompensacaoBACENCedente, "");
        ContaBancaria contaCedente = new ContaBancaria(bancoCedente);
        contaCedente.setNumeroDaConta(new NumeroDaConta(Integer.parseInt(cedenteModel.getConta().getConta())));
        contaCedente.setCarteira(new Carteira(Integer.parseInt(cedenteModel.getConta().getCarteira())));
        contaCedente.setAgencia(new Agencia(Integer.parseInt(cedenteModel.getConta().getAgencia()), cedenteModel.getConta().getDigitoAgencia()));

        Titulo titulo;
        if (sacadorAvalistaModel != null) {
            titulo = new Titulo(contaCedente, sacado, cedente, sacadorAvalista);
        } else {
            titulo = new Titulo(contaCedente, sacado, cedente);
        }
        titulo.setNumeroDoDocumento(String.valueOf(tituloModel.getNumero() + "/"
                + StringUtil.lpad(String.valueOf(tituloModel.getParcela()), "0", 2)));

        if (bancoCedente.getCodigoDeCompensacaoBACEN().getCodigo() == Integer.parseInt(BancosSuportados.BANCO_DO_BRASIL.getCodigoDeCompensacao())) {
            titulo = BancoBrasilUtil.setPropertiesTitulo(titulo, tituloModel, cedenteModel);
        } else if (bancoCedente.getCodigoDeCompensacaoBACEN().getCodigo() == Integer.parseInt(BancosSuportados.BANCO_SICREDI.getCodigoDeCompensacao())) {
            titulo = BancoSicrediUtil.setPropertiesTitulo(titulo, tituloModel, cedenteModel);
        }

        titulo.setValor(BigDecimal.valueOf(tituloModel.getValor()));
        titulo.setDataDoDocumento(tituloModel.getDataEmissao());
        titulo.setDataDoVencimento(tituloModel.getDataVencimento());
        titulo.setTipoDeDocumento(TipoDeTitulo.DS_DUPLICATA_DE_SERVICO);
        titulo.setAceite(Titulo.Aceite.N);

        Boleto boleto = new Boleto(titulo);
        boleto.setInstrucao1(tituloModel.getInstrucoes());
        boleto.setLocalPagamento(tituloModel.getLocalPagamento());
        return boleto;
    }

    private Endereco getEnderecoBySacadoModel(org.linepack.model.Sacado sacado) {
        Endereco enderecoSacado = new Endereco();
        enderecoSacado.setUF(UnidadeFederativa.valueOfSigla(sacado.getUf()));
        enderecoSacado.setLocalidade(sacado.getCidade());
        enderecoSacado.setCep(new CEP(sacado.getCep()));
        enderecoSacado.setBairro(sacado.getBairro());
        enderecoSacado.setLogradouro(sacado.getLogradouro());
        enderecoSacado.setNumero(sacado.getNumero());
        return enderecoSacado;
    }

}
