/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.eng.parer.fascicolo.beans.dto.base.IErroriMultipli;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.VoceDiErrore;
import it.eng.parer.fascicolo.beans.security.User;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.ws.xml.versfascicolorespV3.ECErroreType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECErroriUlterioriType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECWarningType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECWarningUlterioriType;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoGeneraleType;

/**
 *
 * @author fioravanti_f
 */
public abstract class AbsVersFascicoloExt implements IVersFascicoliExt, IErroriMultipli {

    /**
     *
     */
    private static final long serialVersionUID = 4394458854053057889L;
    private User utente;
    private String versioneWsChiamata;
    private String loginName;

    private StrutturaVersFascicolo strutturaComponenti;
    private List<VoceDiErrore> erroriTrovati = new ArrayList<>();
    //
    private int numErrori = 0;
    private int numWarning = 0;
    //
    private Map<String, String> xmlDefaults;
    //
    private Map<String, String> wsVersions;

    @Override
    public String getLoginName() {
        return loginName;
    }

    @Override
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public User getUtente() {
        return utente;
    }

    @Override
    public void setUtente(User utente) {
        this.utente = utente;
    }

    @Override
    public String getVersioneWsChiamata() {
        return versioneWsChiamata;
    }

    @Override
    public void setVersioneWsChiamata(String versioneWsChiamata) {
        this.versioneWsChiamata = versioneWsChiamata;
    }

    public Map<String, String> getXmlDefaults() {
        return xmlDefaults;
    }

    public void setXmlDefaults(Map<String, String> xmlDefaults) {
        this.xmlDefaults = xmlDefaults;
    }

    @Override
    public StrutturaVersFascicolo getStrutturaComponenti() {
        return strutturaComponenti;
    }

    @Override
    public void setStrutturaComponenti(StrutturaVersFascicolo strutturaComponenti) {
        this.strutturaComponenti = strutturaComponenti;
    }

    @Override
    public List<VoceDiErrore> getErroriTrovati() {
        return erroriTrovati;
    }

    @Override
    public boolean isTrovatiErrori() {
        return numErrori > 0;
    }

    @Override
    public boolean isTrovatiWarning() {
        return numWarning > 0;
    }

    @Override
    public VoceDiErrore calcolaErrorePrincipale() {
        numErrori = 0;
        numWarning = 0;
        VoceDiErrore tmpVdEErr = null;
        VoceDiErrore tmpVdEWarn = null;
        for (VoceDiErrore tmpVdE : erroriTrovati) {
            if (tmpVdE.getCodiceEsito() == VoceDiErrore.TipiEsitoErrore.NEGATIVO) {
                tmpVdEErr = tmpVdE;
                numErrori++;
            }
            if (tmpVdE.getCodiceEsito() == VoceDiErrore.TipiEsitoErrore.WARNING) {
                tmpVdEWarn = tmpVdE;
                numWarning++;
            }
        }
        if (tmpVdEErr != null) {
            this.impostaErrorePrincipale(tmpVdEErr);
            return tmpVdEErr;
        } else if (tmpVdEWarn != null) {
            this.impostaErrorePrincipale(tmpVdEWarn);
            return tmpVdEWarn;
        } else {
            return null;
        }
    }

    public void aggiungErroreFatale(EsitoGeneraleType esito) {
        VoceDiErrore tmpErrore;
        tmpErrore = this.addError("", esito.getCodiceErrore(), esito.getMessaggioErrore());
        this.impostaErrorePrincipale(tmpErrore);
    }

    @Override
    public void aggiungErroreFatale(String errCode, String errMessage) {
        VoceDiErrore tmpErrore;
        tmpErrore = this.addError("", errCode, errMessage);
        this.impostaErrorePrincipale(tmpErrore);
    }

    @Override
    public void listErrAddWarning(String descElemento, String errCode, Object... params) {
        this.addWarning(descElemento, errCode, MessaggiWSBundle.getString(errCode, params));
    }

    @Override
    public void listErrAddWarning(String descElemento, String errCode) {
        this.addWarning(descElemento, errCode, MessaggiWSBundle.getString(errCode));
    }

    @Override
    public void listErrAddError(String descElemento, String errCode, Object... params) {
        this.addError(descElemento, errCode, MessaggiWSBundle.getString(errCode, params));
    }

    @Override
    public void listErrAddError(String descElemento, String errCode) {
        this.addError(descElemento, errCode, MessaggiWSBundle.getString(errCode));
    }

    @Override
    public VoceDiErrore addWarning(String descElemento, String errCode, String errMessage) {
        VoceDiErrore tmpErrore = new VoceDiErrore();
        tmpErrore.setSeverity(IRispostaWS.SeverityEnum.WARNING);
        tmpErrore.setCodiceEsito(VoceDiErrore.TipiEsitoErrore.WARNING);
        tmpErrore.setElementoResponsabile(VoceDiErrore.ResponsabilitaErrore.UNI_DOC);
        tmpErrore.setErrorCode(errCode);
        tmpErrore.setErrorMessage(errMessage);
        tmpErrore.setDescElementoErr(descElemento);
        erroriTrovati.add(tmpErrore);
        numWarning++;
        return tmpErrore;
    }

    @Override
    public VoceDiErrore addError(String descElemento, String errCode, String errMessage) {
        VoceDiErrore tmpErrore = new VoceDiErrore();
        tmpErrore.setSeverity(IRispostaWS.SeverityEnum.ERROR);
        tmpErrore.setCodiceEsito(VoceDiErrore.TipiEsitoErrore.NEGATIVO);
        tmpErrore.setElementoResponsabile(VoceDiErrore.ResponsabilitaErrore.UNI_DOC);
        tmpErrore.setErrorCode(errCode);
        tmpErrore.setErrorMessage(errMessage);
        tmpErrore.setDescElementoErr(descElemento);
        erroriTrovati.add(tmpErrore);
        numErrori++;
        return tmpErrore;
    }

    @Override
    public ECErroriUlterioriType produciEsitoErroriSec() {
        ECErroriUlterioriType erroriSecondariType = null;
        if (numErrori > 1) {
            erroriSecondariType = new ECErroriUlterioriType();
            for (VoceDiErrore tmpVdE : erroriTrovati) {
                if (tmpVdE.getCodiceEsito() == VoceDiErrore.TipiEsitoErrore.NEGATIVO
                        && !tmpVdE.isElementoPrincipale()) {
                    ECErroreType tmpErrore = new ECErroreType();
                    tmpErrore.setCodiceErrore(tmpVdE.getErrorCode());
                    tmpErrore.setMessaggioErrore(tmpVdE.getErrorMessage());
                    erroriSecondariType.getErrore().add(tmpErrore);
                }
            }
        }

        return erroriSecondariType;
    }

    @Override
    public ECWarningUlterioriType produciEsitoWarningSec() {
        ECWarningUlterioriType warningSecondariType = null;
        if ((numErrori > 0 && numWarning > 0) || (numWarning > 1)) {
            warningSecondariType = new ECWarningUlterioriType();
            for (VoceDiErrore tmpVdE : erroriTrovati) {
                if (tmpVdE.getCodiceEsito() == VoceDiErrore.TipiEsitoErrore.WARNING && !tmpVdE.isElementoPrincipale()) {
                    ECWarningType tmpWarning = new ECWarningType();
                    tmpWarning.setCodiceWarning(tmpVdE.getErrorCode());
                    tmpWarning.setMessaggioWarning(tmpVdE.getErrorMessage());
                    warningSecondariType.getWarning().add(tmpWarning);
                }
            }
        }

        return warningSecondariType;
    }

    private void impostaErrorePrincipale(VoceDiErrore vde) {
        for (VoceDiErrore tmpVdE : erroriTrovati) {
            tmpVdE.setElementoPrincipale(false);
        }
        vde.setElementoPrincipale(true);
    }

    public Map<String, String> getWsVersions() {
        return wsVersions;
    }

    public void setWsVersions(Map<String, String> wsVersions) {
        this.wsVersions = wsVersions;
    }
}
