/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.impl;

import static it.eng.parer.fascicolo.beans.utils.converter.DateUtilsConverter.convert;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.fascicolo.beans.IControlliSemanticiService;
import it.eng.parer.fascicolo.beans.IControlliWsService;
import it.eng.parer.fascicolo.beans.ILogSessioneFascicoliService;
import it.eng.parer.fascicolo.beans.IRecupSessDubbieFascService;
import it.eng.parer.fascicolo.beans.ISalvataggioFascicoliService;
import it.eng.parer.fascicolo.beans.IVersFascicoloParserService;
import it.eng.parer.fascicolo.beans.IVersFascicoloService;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.WSDescVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS.SeverityEnum;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.exceptions.ParamApplicNotFoundException;
import it.eng.parer.fascicolo.beans.security.User;
import it.eng.parer.fascicolo.beans.utils.AvanzamentoWs;
import it.eng.parer.fascicolo.beans.utils.Costanti.VersioneWS;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.xml.XmlDateUtility;
import it.eng.parer.ws.xml.versfascicolorespV3.CodiceEsitoType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoChiamataWSType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoPosNegNesType;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoGeneraleType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class VersFascicoloService implements IVersFascicoloService {

    //
    private static final Logger log = LoggerFactory.getLogger(VersFascicoloService.class);
    @Inject
    IControlliWsService controlliWsService;

    @Inject
    ISalvataggioFascicoliService salvataggioFascicoliService;

    @Inject
    ILogSessioneFascicoliService logSessioneFascicoliService;

    @Inject
    IRecupSessDubbieFascService recupSessDubbieFascService;

    @Inject
    IControlliSemanticiService controlliSemanticiService;

    @Inject
    IVersFascicoloParserService versFascicoloParserService;

    @ConfigProperty(name = "quarkus.uuid")
    String instanceUUID;

    @SuppressWarnings("unchecked")
    @Override
    public AvanzamentoWs init(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento) {
	//
	Map<String, String> wsVersions = null;
	// base
	versamento.setDescrizione(new WSDescVersFascicolo());
	// load versions
	RispostaControlli rcLoadWsVers = controlliWsService
		.loadWsVersions(versamento.getDescrizione());
	// if positive ...
	if (rcLoadWsVers.isrBoolean()) {
	    wsVersions = (Map<String, String>) rcLoadWsVers.getrObject();
	    versamento.setWsVersions(wsVersions);
	}

	// base
	AvanzamentoWs avanzamento = AvanzamentoWs.nuovoAvanzamentoWS("Q-" + instanceUUID,
		AvanzamentoWs.Funzioni.VERSAMENTO_FASCICOLO);
	// aggancia alla rispostaWS
	rispostaWs.setAvanzamento(avanzamento);

	// base
	rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.ASSENTE);
	rispostaWs.setSeverity(IRispostaWS.SeverityEnum.OK);
	rispostaWs.setErrorCode(StringUtils.EMPTY);
	rispostaWs.setErrorMessage(StringUtils.EMPTY);

	// prepara la classe composita esito e la aggancia alla rispostaWS
	CompRapportoVersFascicolo myEsitoComposito = new CompRapportoVersFascicolo();
	rispostaWs.setCompRapportoVersFascicolo(myEsitoComposito);

	//
	myEsitoComposito
		.setVersioneRapportoVersamento(versamento.getDescrizione().getVersione(wsVersions));
	//
	myEsitoComposito.setDataRapportoVersamento(
		XmlDateUtility.dateToXMLGregorianCalendarOrNull(convert(LocalDateTime.now())));
	//

	//
	myEsitoComposito.setEsitoGenerale(new EsitoGeneraleType());
	if (!rcLoadWsVers.isrBoolean()) {
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcLoadWsVers.getCodErr(), rcLoadWsVers.getDsErr());
	} else {
	    myEsitoComposito.getEsitoGenerale().setCodiceEsito(CodiceEsitoType.POSITIVO);
	    myEsitoComposito.getEsitoGenerale().setCodiceErrore(StringUtils.EMPTY);
	    myEsitoComposito.getEsitoGenerale().setMessaggioErrore(StringUtils.EMPTY);
	    //
	    myEsitoComposito.setEsitoChiamataWS(new ECEsitoChiamataWSType());
	    myEsitoComposito.getEsitoChiamataWS().setCodiceEsito(ECEsitoPosNegNesType.POSITIVO);
	    myEsitoComposito.getEsitoChiamataWS()
		    .setCredenzialiOperatore(ECEsitoPosNegNesType.NON_ESEGUITO);
	    myEsitoComposito.getEsitoChiamataWS()
		    .setVersioneWSCorretta(ECEsitoPosNegNesType.NON_ESEGUITO);
	}

	return avanzamento;
    }

    @Override
    public void verificaVersione(String versione, RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento) {
	log.atDebug().log("sono nel metodo verificaVersione");
	CompRapportoVersFascicolo myEsito = rispostaWs.getCompRapportoVersFascicolo();
	versamento.setVersioneWsChiamata(versione);
	myEsito.setVersioneIndiceSIPFascicolo(versione);
	RispostaControlli rcCheckVers = controlliWsService.checkVersione(versione,
		versamento.getDescrizione().getNomeWs(), versamento.getWsVersions());
	if (!rcCheckVers.isrBoolean()) {
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcCheckVers.getCodErr(), rcCheckVers.getDsErr());
	    myEsito.getEsitoChiamataWS().setCodiceEsito(ECEsitoPosNegNesType.NEGATIVO);
	    myEsito.getEsitoChiamataWS().setVersioneWSCorretta(ECEsitoPosNegNesType.NEGATIVO);
	    // se la versione è sbagliata o inesistente, tento comunque di produrre una
	    // sessione fallita
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	} else {
	    // imposto la versione dell'xml di versamento in via provvisioria al valore del ws
	    // se riuscirò a leggere l'XML imposterò il valore effettivo
	    versamento.getStrutturaComponenti().setVersioneIndiceSipNonVerificata(versione);
	    myEsito.getEsitoChiamataWS().setVersioneWSCorretta(ECEsitoPosNegNesType.POSITIVO);
	    myEsito.setVersioneRapportoVersamento(VersioneWS.calculate(versione).getVersion());
	}
    }

    @Override
    public void verificaCredenziali(String loginName, String password, String indirizzoIp,
	    RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento) {
	log.atDebug().log("sono nel metodo verificaCredenziali");
	CompRapportoVersFascicolo myEsito = rispostaWs.getCompRapportoVersFascicolo();
	User tmpUser = null;
	RispostaControlli rcCheckCred = controlliWsService.checkCredenziali(loginName, password,
		indirizzoIp);
	if (rcCheckCred.isrBoolean()) {
	    tmpUser = (User) rcCheckCred.getrObject();
	    rcCheckCred = controlliWsService.checkAuthWSNoOrg(tmpUser, versamento.getDescrizione());
	    //
	    myEsito.getEsitoChiamataWS().setCredenzialiOperatore(ECEsitoPosNegNesType.POSITIVO);
	}
	if (!rcCheckCred.isrBoolean()) {
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcCheckCred.getCodErr(), rcCheckCred.getDsErr());
	    myEsito.getEsitoChiamataWS().setCodiceEsito(ECEsitoPosNegNesType.NEGATIVO);
	    myEsito.getEsitoChiamataWS().setCredenzialiOperatore(ECEsitoPosNegNesType.NEGATIVO);
	    // se le credenziali sono sbagliate, tento comunque di produrre una sessione
	    // fallita
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	}

	versamento.setLoginName(loginName);
	versamento.setUtente(tmpUser);
    }

    @Override
    public void parseXML(BlockingFakeSession sessione, RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento) {
	log.atDebug().log("sono nel metodo parseXML");

	if (versamento.getUtente() == null) {
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.ERR_666,
		    "Errore: l'utente non è autenticato.");
	    return;
	}

	try {
	    versFascicoloParserService.parseXML(sessione, versamento, rispostaWs);
	} catch (Exception e) {
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    if (ExceptionUtils.getRootCause(e) instanceof ParamApplicNotFoundException) {
		rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.FAS_CONFIG_006_001,
			((ParamApplicNotFoundException) ExceptionUtils.getRootCause(e))
				.getNmParamApplic());
	    } else {
		rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.ERR_666,
			"Errore generico nella fase di parsing dell'XML "
				+ ExceptionUtils.getMessage(e));
	    }
	    log.atError().log("Errore generico nella fase di parsing dell'XML", e);
	}
    }

    @Override
    public void salvaTutto(BlockingFakeSession sessione, RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento) {
	log.atDebug().log("sono nel metodo salvaTutto");
	CompRapportoVersFascicolo myEsito = rispostaWs.getCompRapportoVersFascicolo();

	// questo strano codice serve a includere nella colletion dei messaggi di errore
	// l'errore inserito in modo diretto tramite i metodi rispostaWs#setEsitoWsErr*
	// Se l'errore fa parte di una serie, inserita tramite
	// versamento#listErrAddError oppure
	// versamento#addError non è necessario effettuare questa aggiunta.
	if (myEsito.getEsitoGenerale().getCodiceEsito() == CodiceEsitoType.NEGATIVO
		&& !versamento.isTrovatiErrori()) {
	    versamento.aggiungErroreFatale(myEsito.getEsitoGenerale());
	}
	myEsito.setErroriUlteriori(versamento.produciEsitoErroriSec());
	myEsito.setWarningUlteriori(versamento.produciEsitoWarningSec());
	// verifico se è simulazione di salvataggio e ho qualcosa da salvare
	if (!versamento.isSimulaScrittura() && rispostaWs
		.getStatoSessioneVersamento() != IRispostaWS.StatiSessioneVersEnum.ASSENTE) {
	    if (rispostaWs.getSeverity() != IRispostaWS.SeverityEnum.ERROR && rispostaWs
		    .getStatoSessioneVersamento() == IRispostaWS.StatiSessioneVersEnum.OK) {
		// salva fascicolo - se versamento è ok o warning
		try {
		    salvataggioFascicoliService.salvaFascicolo(rispostaWs, versamento, sessione);
		} catch (AppGenericPersistenceException agpe) {
		    log.atError().log("Errore interno nella fase di salvataggio fascicolo.", agpe);
		    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.ERRATA);
		}

	    }
	    // non uso un costrutto if ... else,
	    // perché dovrei poter salvare una sessione fallita/errata
	    // dopo aver tentato di scrivere il fascicolo sul db.
	    // In pratica è possibile dover eseguire sia il salvataggio fascicolo
	    // che il salvataggio sessione fallita nella stessa chiamata.
	    if (rispostaWs.getSeverity() == IRispostaWS.SeverityEnum.ERROR) {
		if (rispostaWs
			.getStatoSessioneVersamento() == IRispostaWS.StatiSessioneVersEnum.DUBBIA) {
		    // recupero sessione (da dubbia a fallita, se possibile, altrimenti diventa
		    // errata)
		    recupSessDubbieFascService.recuperaSessioneErrata(rispostaWs, versamento,
			    sessione);
		}
		// salva sessione
		if (rispostaWs
			.getStatoSessioneVersamento() == IRispostaWS.StatiSessioneVersEnum.FALLITA) {
		    myEsito.setErroriUlteriori(versamento.produciEsitoErroriSec());
		    myEsito.setWarningUlteriori(versamento.produciEsitoWarningSec());
		    try {
			logSessioneFascicoliService.registraSessioneFallita(rispostaWs, versamento,
				sessione);
		    } catch (AppGenericPersistenceException agpe) {
			log.atError().log(
				"Errore interno nella fase di salvataggio sessione fallita.", agpe);
			// se fallisco il salvataggio della sessione fallita, ci riprovo salvando
			// una
			// sessione errata con gli stessi dati. Anche in questo caso non ho usato un
			// costrutto if ... else
			rispostaWs.setStatoSessioneVersamento(
				IRispostaWS.StatiSessioneVersEnum.ERRATA);
		    }
		}
		if (rispostaWs
			.getStatoSessioneVersamento() == IRispostaWS.StatiSessioneVersEnum.ERRATA) {
		    myEsito.setErroriUlteriori(versamento.produciEsitoErroriSec());
		    myEsito.setWarningUlteriori(versamento.produciEsitoWarningSec());
		    try {
			logSessioneFascicoliService.registraSessioneErrata(rispostaWs, versamento,
				sessione);
		    } catch (AppGenericPersistenceException agpe) {
			log.atError().log(
				"Errore interno nella fase di salvataggio sessione errata.", agpe);
		    }
		}

	    }

	}

    }

}
