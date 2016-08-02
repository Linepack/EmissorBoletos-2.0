/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.linepack.util.EntityManagerUtil;

/**
 *
 * @author Leandro
 * @param <T>
 */
public class BaseDAO<T> {

    protected EntityManager entityManager;
    private final Class<T> type;

    public BaseDAO(Class<T> type) {
        this.entityManager = new EntityManagerUtil().getEntityManager();
        this.type = type;
    }

    public String insert(T object) throws IOException {
        try {
            if (object != null) {
                entityManager.persist(object);
                entityManager.getTransaction().commit();
                entityManager.close();
                return null;
            }
            String erro = "Erro ao inserir, objeto Nulo!";
            return erro;
        } catch (Exception e) {
            String erro = "Erro ao tentar inserir: " + e.toString();
            return erro;
        }
    }

    public String update(T object) {
        if (object != null) {
            entityManager.merge(object);
            entityManager.getTransaction().commit();
            entityManager.close();
            return null;
        }
        return "Erro ao atualizar, objeto Nulo.";
    }

    public T getByID(Integer id) {
        /*Query query = this.entityManager.createQuery(""
                + "select a"
                + "  from " + modelClassName + " a "
                + " where a.id = " + id);
        Object object = new Object();
        object = query.getSingleResult();*/
        Object object = new Object();
        object = this.entityManager.find(this.type, id);
        this.entityManager.close();
        return (T) object;
    }

    public T getByNamedQuery(String namedQueryName) {
        Query query = this.entityManager.createNamedQuery(namedQueryName);
        Object object = new Object();
        object = query.getSingleResult();
        this.entityManager.close();
        return (T) object;

    }

    public <T> List<T> getListByNamedQuery(String namedQueryName) throws IllegalAccessException {
        Query query = this.entityManager.createNamedQuery(namedQueryName);
        List objectList = new ArrayList<>();
        objectList = query.getResultList();
        this.entityManager.close();
        return objectList;
    }
}
