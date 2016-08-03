/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.util;

import org.jrimum.bopepo.parametro.ParametroBancoSicredi;
import org.jrimum.domkee.financeiro.banco.ParametroBancario;
import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.texgit.type.component.Field;

/**
 *
 * @author Leandro
 */
public class BancoSicrediUtil {

    public static Titulo setPropertiesTitulo(Titulo titulo, org.linepack.model.Titulo tituloModel, org.linepack.model.Cedente cedenteModel) {
        Titulo newTitulo = titulo;                
        newTitulo.setNossoNumero(geraNossoNumero(tituloModel, cedenteModel));
        newTitulo.setDigitoDoNossoNumero(geraDVNossoNumero(tituloModel, cedenteModel));                                                
        ParametroBancario<ParametroBancoSicredi> postoDaAgencia = ParametroBancoSicredi.POSTO_DA_AGENCIA;
        newTitulo.setParametrosBancarios(new ParametrosBancariosMap(postoDaAgencia,Integer.parseInt(cedenteModel.getConta().getPostoAgencia())));
        return newTitulo;
    }

    private static String geraNossoNumero(
            org.linepack.model.Titulo tituloModel,
            org.linepack.model.Cedente cedenteModel) {
        String ano = new Field<>(tituloModel.getAno(), 2).write();
        String numeroFormatado = StringUtil.lpad(String.valueOf(tituloModel.getNumero()), "0", cedenteModel.getConta().getTamanhoNossoNumero());
        String sequencia = new Field<>(numeroFormatado, 5).write();
        String retorno = ano + cedenteModel.getConta().getByteCedente() + sequencia;
        return retorno;
    }

    private static String geraDVNossoNumero(
            org.linepack.model.Titulo tituloModel,
            org.linepack.model.Cedente cedenteModel) {
        String agencia = new Field<>(cedenteModel.getConta().getAgencia(), 4).write();
        String posto = new Field<>(cedenteModel.getConta().getPostoAgencia(), 2).write();
        String cedente = new Field<>(cedenteModel.getConta().getConta(), 5).write();
        String ano = new Field<>(tituloModel.getAno(), 2).write();
        String numeroFormatado = StringUtil.lpad(String.valueOf(tituloModel.getNumero()), "0", cedenteModel.getConta().getTamanhoNossoNumero());
        String sequencia = new Field<>(numeroFormatado, 5).write();
        String retorno = agencia + posto + cedente + ano + cedenteModel.getConta().getByteCedente() + sequencia;

        int x = calculeDVModulo11(retorno);
        String dv = String.valueOf(x);
        return dv;
    }

    private static int calculeDVModulo11(String numero) {
        int dv = 0;
        int[] values = new int[numero.length()];
        for (int i = 0; i < numero.length(); i++) {
            values[i] = Integer.parseInt(numero.charAt(i) + "");
        }
        int soma = 0;
        int vetpos = numero.length() - 1;
        while (vetpos >= 0) {
            for (int i = 2; i < 10; i++) {
                soma += values[vetpos] * i;
                vetpos--;
                if (vetpos < 0) {
                    break;
                }
            }
        }

        if (soma < 11) {
            dv = soma - 11;
        } else {
            int resto = soma % 11;
            dv = 11 - resto;
        }

        if (dv > 9) {
            dv = 0;
        }

        return dv;
    }

}
