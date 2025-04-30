/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.utils.messages;

import java.util.List;

import it.eng.parer.fascicolo.jpa.entity.DecErrSacer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@SuppressWarnings("unchecked")
@ApplicationScoped
@Transactional
public class MessaggiWSHelper {

    @Inject
    EntityManager entityManager;

    public List<DecErrSacer> caricaListaErrori() {
        final String queryStr = "SELECT e FROM DecErrSacer e ";
        Query query = entityManager.createQuery(queryStr);

        return query.getResultList();
    }

    @Transactional(value = TxType.REQUIRED)
    public DecErrSacer caricaDecErrore(String cdErrore) {
        final String queryStr = "SELECT e FROM DecErrSacer e " + "where e.cdErr = :cdErr";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("cdErr", cdErrore);
        return (DecErrSacer) query.getSingleResult();
    }

}
