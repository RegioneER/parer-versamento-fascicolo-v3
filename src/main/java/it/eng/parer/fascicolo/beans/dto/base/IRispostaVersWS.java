/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

/**
 *
 * @author Fioravanti_F
 */
public interface IRispostaVersWS extends IRispostaWS {

    boolean isErroreElementoDoppio();

    void setErroreElementoDoppio(boolean valore);

    long getIdElementoDoppio();

    void setIdElementoDoppio(long id);
}
