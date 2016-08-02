/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.main;

import org.linepack.dao.CedenteDAO;
import org.linepack.dao.SacadoDAO;
import org.linepack.dao.TituloDAO;

/**
 *
 * @author Leandro
 */
public class EmissorBoleto {

    public String getBoletoStream(Integer tituloId) {
        org.linepack.model.Titulo tituloModel = this.getTituloModelById(tituloId);
        org.linepack.model.Cedente cedenteModel = this.getCedenteModelById(tituloModel.getCedente().getId());
        org.linepack.model.Sacado sacadoModel = this.getSacadoModelById(tituloModel.getSacado().getId());

        if (tituloModel.getSacadorAvalista() != null) {
            org.linepack.model.Sacado sacadorAvalista = this.getSacadoModelById(tituloModel.getSacadorAvalista().getId());
        }

        return cedenteModel.getNome();
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

    private String getBoleto() {

        return "";
    }
}
