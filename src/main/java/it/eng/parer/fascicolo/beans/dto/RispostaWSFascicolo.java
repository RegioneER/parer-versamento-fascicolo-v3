/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto;

import it.eng.parer.fascicolo.beans.dto.base.IRispostaVersWS;
import it.eng.parer.fascicolo.beans.utils.AvanzamentoWs;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.ws.xml.versfascicolorespV3.CodiceEsitoType;

/**
 *
 * @author fioravanti_f
 */
public class RispostaWSFascicolo implements IRispostaVersWS {

    private static final long serialVersionUID = 5904891240038140592L;
    private SeverityEnum severity = SeverityEnum.OK;
    private ErrorTypeEnum errorType = ErrorTypeEnum.NOERROR;
    private String errorMessage;
    private String errorCode;
    private AvanzamentoWs avanzamento;

    transient CompRapportoVersFascicolo compRapportoVersFascicolo;

    private boolean erroreElementoDoppio;
    private long idElementoDoppio;

    private boolean erroreStrutturaNonPartizionata;

    private StatiSessioneVersEnum statoSessioneVersamento = StatiSessioneVersEnum.ASSENTE;

    public StatiSessioneVersEnum getStatoSessioneVersamento() {
        return statoSessioneVersamento;
    }

    public void setStatoSessioneVersamento(StatiSessioneVersEnum statoSessioneVersamento) {
        this.statoSessioneVersamento = statoSessioneVersamento;
    }

    @Override
    public SeverityEnum getSeverity() {
        return severity;
    }

    @Override
    public void setSeverity(SeverityEnum severity) {
        this.severity = severity;
    }

    @Override
    public ErrorTypeEnum getErrorType() {
        return errorType;
    }

    @Override
    public void setErrorType(ErrorTypeEnum errorType) {
        this.errorType = errorType;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public AvanzamentoWs getAvanzamento() {
        return avanzamento;
    }

    @Override
    public void setAvanzamento(AvanzamentoWs avanzamento) {
        this.avanzamento = avanzamento;
    }
    //

    public CompRapportoVersFascicolo getCompRapportoVersFascicolo() {
        return compRapportoVersFascicolo;
    }

    public void setCompRapportoVersFascicolo(CompRapportoVersFascicolo compRapportoVersFascicolo) {
        this.compRapportoVersFascicolo = compRapportoVersFascicolo;
    }

    //
    @Override
    public void setEsitoWsErrBundle(String errCode, Object... params) {
        // aggiorno entrambe le tipologie di rapporto di versamento. Alle fine restituirò quella più adatta
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceEsito(CodiceEsitoType.NEGATIVO);
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceErrore(errCode);
        compRapportoVersFascicolo.getEsitoGenerale().setMessaggioErrore(MessaggiWSBundle.getString(errCode, params));
    }

    @Override
    public void setEsitoWsErrBundle(String errCode) {
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceEsito(CodiceEsitoType.NEGATIVO);
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceErrore(errCode);
        compRapportoVersFascicolo.getEsitoGenerale().setMessaggioErrore(MessaggiWSBundle.getString(errCode));
    }

    @Override
    public void setEsitoWsWarnBundle(String errCode, Object... params) {
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceEsito(CodiceEsitoType.WARNING);
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceErrore(errCode);
        compRapportoVersFascicolo.getEsitoGenerale().setMessaggioErrore(MessaggiWSBundle.getString(errCode, params));
    }

    @Override
    public void setEsitoWsWarnBundle(String errCode) {
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceEsito(CodiceEsitoType.WARNING);
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceErrore(errCode);
        compRapportoVersFascicolo.getEsitoGenerale().setMessaggioErrore(MessaggiWSBundle.getString(errCode));
    }

    @Override
    public void setEsitoWsError(String errCode, String errMessage) {
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceEsito(CodiceEsitoType.NEGATIVO);
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceErrore(errCode);
        compRapportoVersFascicolo.getEsitoGenerale().setMessaggioErrore(errMessage);
    }

    @Override
    public void setEsitoWsWarning(String errCode, String errMessage) {
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceEsito(CodiceEsitoType.WARNING);
        compRapportoVersFascicolo.getEsitoGenerale().setCodiceErrore(errCode);
        compRapportoVersFascicolo.getEsitoGenerale().setMessaggioErrore(errMessage);
    }

    @Override
    public boolean isErroreElementoDoppio() {
        return erroreElementoDoppio;
    }

    @Override
    public void setErroreElementoDoppio(boolean erroreElementoDoppio) {
        this.erroreElementoDoppio = erroreElementoDoppio;
    }

    @Override
    public long getIdElementoDoppio() {
        return idElementoDoppio;
    }

    @Override
    public void setIdElementoDoppio(long idElementoDoppio) {
        this.idElementoDoppio = idElementoDoppio;
    }

    public boolean isErroreStrutturaNonPartizionata() {
        return erroreStrutturaNonPartizionata;
    }

    public void setErroreStrutturaNonPartizionata(boolean erroreStrutturaNonPartizionata) {
        this.erroreStrutturaNonPartizionata = erroreStrutturaNonPartizionata;
    }

}
