/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS.SeverityEnum;

import java.io.Serializable;

/**
 *
 * @author Fioravanti_F
 */
public class VoceDiErrore implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4124405579586404634L;

    public enum ResponsabilitaErrore {
        UNI_DOC
    }

    //
    public enum TipiEsitoErrore {
        NEGATIVO, WARNING
    }

    //
    private ResponsabilitaErrore elementoResponsabile;
    //
    private IRispostaWS.SeverityEnum severity;
    private TipiEsitoErrore codiceEsito;
    private String errorCode;
    private String errorMessage;
    private String descElementoErr;
    //
    private boolean elementoPrincipale;

    public SeverityEnum getSeverity() {
        return severity;
    }

    public void setSeverity(SeverityEnum severity) {
        this.severity = severity;
    }

    public ResponsabilitaErrore getElementoResponsabile() {
        return elementoResponsabile;
    }

    public void setElementoResponsabile(ResponsabilitaErrore elementoResp) {
        this.elementoResponsabile = elementoResp;
    }

    public TipiEsitoErrore getCodiceEsito() {
        return codiceEsito;
    }

    public void setCodiceEsito(TipiEsitoErrore severity) {
        this.codiceEsito = severity;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDescElementoErr() {
        return descElementoErr;
    }

    public void setDescElementoErr(String descElementoErr) {
        this.descElementoErr = descElementoErr;
    }

    public boolean isElementoPrincipale() {
        return elementoPrincipale;
    }

    public void setElementoPrincipale(boolean elementoPrincipale) {
        this.elementoPrincipale = elementoPrincipale;
    }
}
