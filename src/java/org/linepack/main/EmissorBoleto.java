/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.linepack.model.Titulo;

/**
 *
 * @author Leandro
 */
public class EmissorBoleto {

    public static void run() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("homologa");
        EntityManager em = emf.createEntityManager();
        Titulo tit = new Titulo();
        tit = em.find(Titulo.class, 1);
    }
}
