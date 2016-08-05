/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.linepack.dao.SetupDAO;
import org.linepack.dao.TituloDAO;
import org.linepack.model.Setup;
import org.linepack.util.BancoBrasilUtil;
import org.linepack.util.BancoSicrediUtil;
import org.linepack.util.StringUtil;

/**
 *
 * @author Leandro
 */
public class EmissorBoleto {

    public byte[] getBoletoBytes(Integer tituloId) {
        org.linepack.model.Titulo tituloModel = new org.linepack.model.Titulo();
        tituloModel = this.getTituloModelById(tituloId);

        byte[] pdfAsBytes = null;
        if (tituloModel != null) {
            pdfAsBytes = tituloModel.getBoleto();
        } else {
            Boleto boleto = this.getBoletoStream(tituloModel);
            BoletoViewer viewer = new BoletoViewer(boleto);
            pdfAsBytes = viewer.getPdfAsByteArray();
        }
        return pdfAsBytes;
    }

    public String getBoletoByPath(Integer tituloId) throws FileNotFoundException, IOException {
        byte[] pdfAsBytes = this.getBoletoBytes(tituloId);
        Setup setupModel = this.getSetupAtivo();
        String nomeArquivo = setupModel.getPathToSaveFile() + this.getFileName();
        try (FileOutputStream fos = new FileOutputStream(new File(nomeArquivo))) {
            fos.write(pdfAsBytes);
        }
        return nomeArquivo;
    }

    private Boleto getBoletoStream(org.linepack.model.Titulo tituloModel) {
        try {
            org.linepack.model.Cedente cedenteModel = this.getCedenteModelById(tituloModel.getCedente().getId());
            org.linepack.model.Sacado sacadoModel = this.getSacadoModelById(tituloModel.getSacado().getId());

            org.linepack.model.Sacado sacadorAvalista = null;
            if (tituloModel.getSacadorAvalista() != null) {
                sacadorAvalista = this.getSacadoModelById(tituloModel.getSacadorAvalista().getId());
            }

            Boleto boleto = this.createBoleto(cedenteModel, sacadoModel, sacadorAvalista, tituloModel);
            this.updateTituloModel(tituloModel, boleto);
            return boleto;
        } catch (Exception e) {
            return new Boleto();
        }
    }

    private org.linepack.model.Setup getSetupAtivo() {
        SetupDAO setupDAO = new SetupDAO();
        return setupDAO.getByNamedQuery("setupAtivo");
    }

    private org.linepack.model.Titulo getTituloModelById(Integer id) {
        TituloDAO tituloDAO = new TituloDAO();
        return tituloDAO.getByID(id);
    }

    private org.linepack.model.Cedente getCedenteModelById(Integer id) {
        CedenteDAO cedenteDAO = new CedenteDAO();
        return cedenteDAO.getByID(id);
    }

    private org.linepack.model.Sacado getSacadoModelById(Integer id) {
        SacadoDAO sacadoDAO = new SacadoDAO();
        return sacadoDAO.getByID(id);
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

    private void updateTituloModel(org.linepack.model.Titulo titulo, Boleto boleto) throws SQLException {
        TituloDAO tituloDAO = new TituloDAO();
        titulo.setDataAlteracao(new Date());
        titulo.setNomeAlteracao("JAVA");
        BoletoViewer viewer = new BoletoViewer(boleto);
        byte[] pdfAsBytes = viewer.getPdfAsByteArray();
        titulo.setBoleto(pdfAsBytes);
        tituloDAO.update(titulo);
    }

    private String getFileName() {
        Date data = new Date();
        SimpleDateFormat dataFormatada = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String nomeArquivo = dataFormatada.format(data) + ".pdf";
        return nomeArquivo;
    }
}
