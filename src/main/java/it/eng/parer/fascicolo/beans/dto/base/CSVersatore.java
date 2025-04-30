/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

/**
 *
 * @author Fioravanti_F
 */
public class CSVersatore implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String sistemaConservazione;
    private String ambiente;
    private String ente;
    private String struttura;
    private String user;

    public String getSistemaConservazione() {
        return sistemaConservazione;
    }

    public void setSistemaConservazione(String sistemaConservazione) {
        this.sistemaConservazione = sistemaConservazione;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getEnte() {
        return ente;
    }

    public void setEnte(String ente) {
        this.ente = ente;
    }

    public String getStruttura() {
        return struttura;
    }

    public void setStruttura(String struttura) {
        this.struttura = struttura;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
