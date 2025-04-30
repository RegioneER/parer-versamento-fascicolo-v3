/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto;

import it.eng.parer.fascicolo.beans.dto.base.IRestWSBase;
import it.eng.parer.fascicolo.beans.security.User;

import java.io.Serializable;

/**
 *
 * @author Fioravanti_F
 */
public interface IVersFascicoliExt extends Serializable, IRestWSBase {

    StrutturaVersFascicolo getStrutturaComponenti();

    void setStrutturaComponenti(StrutturaVersFascicolo strutturaComponenti);

    public boolean isSimulaScrittura();

    public void setSimulaScrittura(boolean simulaScrittura);

    // necessario a gestire l'EJB come stateless
    public User getUtente();

    public void setUtente(User utente);
}
