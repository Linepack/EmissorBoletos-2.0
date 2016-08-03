/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.util;

import org.jrimum.domkee.financeiro.banco.febraban.Titulo;

/**
 *
 * @author Leandro
 */
public class BancoBrasilUtil {

    public static Titulo setPropertiesTitulo(Titulo titulo, org.linepack.model.Titulo tituloModel, org.linepack.model.Cedente cedenteModel) {
        Titulo newTitulo = titulo;
        newTitulo.setNossoNumero(StringUtil.lpad(String.valueOf(tituloModel.getNumero())
                + StringUtil.lpad(String.valueOf(tituloModel.getParcela()), "0", 2), "0", cedenteModel.getConta().getTamanhoNossoNumero()));
        return newTitulo;
    }

}
