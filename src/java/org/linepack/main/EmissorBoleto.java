/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.main;
      
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.linepack.dao.CedenteDAO;
import org.linepack.dao.TituloDAO;


/**
 *
 * @author Leandro
 */
public class EmissorBoleto {

    public String getBoletoStream(Integer tituloId) {                
        org.linepack.model.Titulo tituloModel = this.getTituloModelById(tituloId);                
        Cedente cedenteBoleto = this.getCedenteBoletoByTituloModel(tituloModel);
        
        return cedenteBoleto.getNome();
    }
    
    private Cedente getCedenteBoletoByTituloModel(org.linepack.model.Titulo titulo){
        org.linepack.model.Cedente cedenteModel = new org.linepack.model.Cedente();
        CedenteDAO cedenteDAO = new CedenteDAO();
        cedenteModel = cedenteDAO.getByID(titulo.getCedente().getId());
        Cedente cedenteBoleto = new Cedente(cedenteModel.getNome(), cedenteModel.getCnpj());
        return cedenteBoleto;        
    }
    
    private org.linepack.model.Titulo getTituloModelById(Integer id){
        org.linepack.model.Titulo tituloModel = new org.linepack.model.Titulo();
        TituloDAO tituloDAO = new TituloDAO();
        tituloModel = tituloDAO.getByID(id);
        return tituloModel;
    }
}
