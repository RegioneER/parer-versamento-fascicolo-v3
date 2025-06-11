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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.fascicolo.beans.IConfigurationDao;
import it.eng.parer.fascicolo.beans.IControlliCollFascicoloService;
import it.eng.parer.fascicolo.beans.IControlliFascicoliService;
import it.eng.parer.fascicolo.beans.IControlliProfiliFascicoloService;
import it.eng.parer.fascicolo.beans.IControlliSemanticiService;
import it.eng.parer.fascicolo.beans.IControlliWsService;
import it.eng.parer.fascicolo.beans.IRapportoVersBuilderService;
import it.eng.parer.fascicolo.beans.IVersFascicoloParserService;
import it.eng.parer.fascicolo.beans.XmlFascCache;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.ConfigNumFasc;
import it.eng.parer.fascicolo.beans.dto.FascicoloLink;
import it.eng.parer.fascicolo.beans.dto.FlControlliFasc;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.CSVersatore;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS.SeverityEnum;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.base.VoceDiErrore;
import it.eng.parer.fascicolo.beans.utils.Costanti;
import it.eng.parer.fascicolo.beans.utils.Costanti.TipiGestioneFascAnnullati;
import it.eng.parer.fascicolo.beans.utils.KeyOrdFascUtility;
import it.eng.parer.fascicolo.beans.utils.KeySizeFascUtility;
import it.eng.parer.fascicolo.beans.utils.LogSessioneUtils;
import it.eng.parer.fascicolo.beans.utils.ParametroApplDB;
import it.eng.parer.fascicolo.beans.utils.VerificaVersione;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSFormat;
import it.eng.parer.fascicolo.beans.utils.xml.XmlDateUtility;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;
import it.eng.parer.ws.xml.versfascicoloV3.IntestazioneType;
import it.eng.parer.ws.xml.versfascicoloV3.TipoConservazioneType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECConfigurazioneSIPType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECConfigurazioneType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoPosNegType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoPosNegWarType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECFascicoloType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECFascicoloType.EsitoControlliFascicolo;
import it.eng.parer.ws.xml.versfascicolorespV3.SCChiaveType;
import it.eng.parer.ws.xml.versfascicolorespV3.SCVersatoreType;
import it.eng.parer.ws.xml.versfascicolorespV3.SIPType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class VersFascicoloParserService implements IVersFascicoloParserService {

    private static final Logger log = LoggerFactory.getLogger(VersFascicoloParserService.class);
    // controlli sul db
    @Inject
    IControlliSemanticiService controlliSemanticiService;
    // controlli specifici del fascicolo
    @Inject
    IControlliFascicoliService controlliFascicoliService;
    // controlli su profilo archivistico e profilo generale fascicolo
    @Inject
    IControlliProfiliFascicoloService controlliProfiliFascicoloService;
    // stateless ejb dei controlli collegamenti dei fascicoli
    @Inject
    IControlliCollFascicoloService controlliCollFascicoloService;
    // verifica autorizzazione ws
    @Inject
    IControlliWsService controlliWsService;
    // singleton gestione cache dei parser jaxb dei fascicoli
    @Inject
    XmlFascCache xmlFascCache;
    // crea il rapporto di versamento e canonicalizza (C14N) il SIP
    @Inject
    IRapportoVersBuilderService rapportoVersBuilderService;
    // gestione configurazioni applicative
    @Inject
    IConfigurationDao configurationDao;

    @Override
    public void parseXML(BlockingFakeSession sessione, VersFascicoloExt versamento,
	    RispostaWSFascicolo rispostaWs) {
	//
	CSVersatore tagCSVersatore = new CSVersatore();
	CSChiaveFasc tagCSChiave = new CSChiaveFasc();
	String descChiaveFasc = StringUtils.EMPTY;
	String sistema = StringUtils.EMPTY;

	// istanzia la classe di verifica retrocompatibilità
	VerificaVersione tmpVerificaVersione = new VerificaVersione();
	VerificaVersione.EsitiVerfica tmpEsitiVerfica;

	CompRapportoVersFascicolo myEsito = rispostaWs.getCompRapportoVersFascicolo();
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();

	// a priori, un problema in questo punto provoca il fallimento completo del
	// versamento
	rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.ERRATA);

	/*
	 * produco la versione canonicalizzata del SIP. Gestisco l'eventuale errore relativo
	 * all'encoding indicato in maniera errata (es. "ISO8859/1" oppure "utf8"), non rilevato
	 * dalla verifica precedente.
	 */
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    RispostaControlli rcCanonicalizzaDaSalvareIndiceSip = rapportoVersBuilderService
		    .canonicalizzaDaSalvareIndiceSip(sessione);
	    if (!rcCanonicalizzaDaSalvareIndiceSip.isrBoolean()) {

		rispostaWs.setSeverity(SeverityEnum.ERROR);
		rispostaWs.setErrorCode(rcCanonicalizzaDaSalvareIndiceSip.getCodErr());
		rispostaWs.setErrorMessage(rcCanonicalizzaDaSalvareIndiceSip.getDsErr());

		rispostaWs.setEsitoWsError(rcCanonicalizzaDaSalvareIndiceSip.getCodErr(),
			rcCanonicalizzaDaSalvareIndiceSip.getDsErr());
	    } else {
		//
		sessione.setDatiDaSalvareIndiceSip(rcCanonicalizzaDaSalvareIndiceSip.getrString());
	    }
	}

	/*
	 *
	 */
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    //
	    tagCSVersatore
		    .setAmbiente(parsedIndiceFasc.getIntestazione().getVersatore().getAmbiente());
	    tagCSVersatore.setEnte(parsedIndiceFasc.getIntestazione().getVersatore().getEnte());
	    tagCSVersatore
		    .setStruttura(parsedIndiceFasc.getIntestazione().getVersatore().getStruttura());
	    versamento.getStrutturaComponenti().setVersatoreNonverificato(tagCSVersatore);
	    // memorizzo il tipo fascicolo non verificato
	    versamento.getStrutturaComponenti().setTipoFascicoloNonverificato(
		    parsedIndiceFasc.getIntestazione().getTipoFascicolo());
	    // memorizzo la versione xml, come dichiarata
	    versamento.getStrutturaComponenti().setVersioneIndiceSipNonVerificata(
		    parsedIndiceFasc.getParametri().getVersioneIndiceSIPFascicolo());
	    // memorizzo la chiave in una variabile di appoggio per usarla in diverse parti
	    // dell'elaborazione
	    tagCSChiave.setAnno(parsedIndiceFasc.getIntestazione().getChiave().getAnno());
	    tagCSChiave.setNumero(parsedIndiceFasc.getIntestazione().getChiave().getNumero());
	    descChiaveFasc = MessaggiWSFormat.formattaChiaveFascicolo(tagCSVersatore, tagCSChiave);
	    versamento.getStrutturaComponenti().setChiaveNonVerificata(tagCSChiave);
	    sistema = configurationDao.getParamApplicValue(ParametroApplDB.NM_SISTEMACONSERVAZIONE);
	    versamento.getStrutturaComponenti().setUrnPartChiaveFascicolo(
		    MessaggiWSFormat.formattaChiaveFascicolo(tagCSVersatore, tagCSChiave, sistema));
	    // normalized key
	    versamento.getStrutturaComponenti().setUrnPartChiaveFascicoloNormalized(MessaggiWSFormat
		    .formattaChiaveFascicolo(tagCSVersatore, tagCSChiave, sistema, true));

	}

	ECConfigurazioneSIPType myConfigurazioneSIPType = new ECConfigurazioneSIPType();
	ECFascicoloType myFascicoloType = new ECFascicoloType();
	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = new ECFascicoloType.EsitoControlliFascicolo();
	// se l'unmarshalling è andato bene
	// imposta il flag globale di simulazione scrittura
	// preparo la risposta relativa alla configurazione SIP
	// preparo la risposta relativa al fascicolo
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    if (parsedIndiceFasc.getParametri() != null) {

		// imposta il flag globale di simulazione scrittura
		Boolean b = parsedIndiceFasc.getParametri().isSimulaSalvataggioDatiInDB();
		if (b != null && b) {
		    versamento.setSimulaScrittura(true);
		    IntestazioneType intestazione = parsedIndiceFasc.getIntestazione();
		    LogSessioneUtils.logSimulazioni(intestazione.getVersatore().getAmbiente(),
			    intestazione.getVersatore().getEnte(),
			    intestazione.getVersatore().getStruttura(), "fascicolo",
			    intestazione.getChiave().getAnno(),
			    intestazione.getChiave().getNumero(),
			    intestazione.getVersatore().getUserID(), log);
		}
		//
		myEsito.setIdentificativoRapportoVersamento(
			MessaggiWSFormat.formattaUrnRappVersFasc(
				versamento.getStrutturaComponenti().getUrnPartChiaveFascicolo(),
				Costanti.UrnFormatter.URN_RAPP_VERS_V2));
		//
		SIPType mySip = new SIPType();
		myEsito.setSIP(mySip);
		mySip.setDataVersamento(
			XmlDateUtility.dateToXMLGregorianCalendarOrNull(sessione.getTmApertura()));
		mySip.setURNIndiceSIP(MessaggiWSFormat.formattaUrnIndiceSipFasc(
			versamento.getStrutturaComponenti().getUrnPartChiaveFascicolo(),
			Costanti.UrnFormatter.URN_INDICE_SIP_V2));
		//
		myEsito.setParametriVersamento(myConfigurazioneSIPType);
		myConfigurazioneSIPType.setTipoConservazione(
			parsedIndiceFasc.getParametri().getTipoConservazione().name());
		myConfigurazioneSIPType.setForzaClassificazione(
			parsedIndiceFasc.getParametri().isForzaClassificazione());
		myConfigurazioneSIPType
			.setForzaNumero(parsedIndiceFasc.getParametri().isForzaNumero());
		myConfigurazioneSIPType.setForzaCollegamento(
			parsedIndiceFasc.getParametri().isForzaCollegamento());
	    }

	    myEsito.setFascicolo(myFascicoloType);
	    myFascicoloType.setEsitoControlliFascicolo(myControlliFascicolo);
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
	    // preparo il tag Versatore nella response, obbligatorio
	    SCVersatoreType tmpVersatoreType = new SCVersatoreType();
	    myFascicoloType.setVersatore(tmpVersatoreType);
	    tmpVersatoreType.setAmbiente(tagCSVersatore.getAmbiente());
	    tmpVersatoreType.setEnte(tagCSVersatore.getEnte());
	    tmpVersatoreType.setStruttura(tagCSVersatore.getStruttura());
	    tmpVersatoreType
		    .setUserID(parsedIndiceFasc.getIntestazione().getVersatore().getUserID());
	    // preparo anche il tag relativo alla chiave
	    SCChiaveType tmpChiaveType = new SCChiaveType();
	    myFascicoloType.setChiave(tmpChiaveType);
	    tmpChiaveType.setAnno(tagCSChiave.getAnno());
	    tmpChiaveType.setNumero(tagCSChiave.getNumero());
	    // e quello del tipo fascicolo
	    myFascicoloType.setTipoFascicolo(
		    versamento.getStrutturaComponenti().getTipoFascicoloNonverificato());
	}

	// MEV#25288
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    // prepara risposta con urn sip
	    this.buildUrnSipOnEsito(myEsito, versamento);
	}
	// end MEV#25288

	// ////////////////////////////////////////////////////////////
	// in questo punto verranno messi i controlli
	// sula struttura del versamento basati sulla versione
	// dichiarata. Gli errori verranno censiti some "errori XSD".
	// ////////////////////////////////////////////////////////////
	// verifica se la struttura della chiamata al ws è coerente con la versione
	// dichiarata
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    tmpVerificaVersione.verifica(rispostaWs, versamento, sessione);
	    tmpEsitiVerfica = tmpVerificaVersione.getEsitiVerfica();
	    if (tmpEsitiVerfica.isFlgErrore()) {
		rispostaWs.setSeverity(SeverityEnum.ERROR);
		rispostaWs.setEsitoWsError(tmpEsitiVerfica.getCodErrore(),
			tmpEsitiVerfica.getMessaggio());
		myEsito.getEsitoXSD().setCodiceEsito(ECEsitoPosNegType.NEGATIVO);
	    }
	}

	// in seguito vengono i controlli strutturali non basati sulla versione:
	// verifica nel complesso se ci sono problemi strutturali, non verificati
	// tramite XSD
	// tipo id duplicati o conteggi che non coincidono

	// come prima cosa verifico che il versatore e la versione dichiarati nel WS
	// coincidano
	// con quelli nell'xml
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.controllaUserIdAndVersioneWS(myControlliFascicolo, versamento, rispostaWs,
		    sessione);
	}

	// se ho passato la verifica strutturale...
	// verifica la struttura versante
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.controllaStruttura(myControlliFascicolo, versamento, rispostaWs, tagCSVersatore);
	}

	// se il versatore alla fine è ok e la struttura è utilizzabile, lo scrivo
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    myControlliFascicolo.setIdentificazioneVersatore(ECEsitoPosNegType.POSITIVO);
	    versamento.getStrutturaComponenti().setVersatoreVerificato(true);
	}

	// verifico il tipo fascicolo - in caso di errore la sessione è DUBBIA
	//
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.controllaTipoFasc(myControlliFascicolo, versamento, rispostaWs, descChiaveFasc);
	}

	// verifico il tipo fascicolo - abilitato su utente
	//
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.controllaTipoFascUserOrg(myControlliFascicolo, versamento, rispostaWs,
		    descChiaveFasc);
	}

	// verifico il tipo fascicolo - abilitato su anno
	//
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.controllaTipoFascAnno(myControlliFascicolo, versamento, rispostaWs,
		    descChiaveFasc);
	}

	// nota: comunque vada, da qui in poi la sessione non può essere più DUBBIA
	// (può essere solo OK, FALLITA oppure ERRATA) perché ho tutti gli elementi per
	// memorizzare
	// una sessione fallita: id struttura, id tipo fascicolo e chiave, che è
	// certamente
	// valorizzata in quanto ho superato la verifica XSD.
	//
	// Predispongo lo stato della sessione a FALLITA, in via precauzionale.
	// in caso di verifica positiva dei metadati, porterò questo valore a OK
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.FALLITA);
	}

	// verifico la chiave
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.controllaChiave(myControlliFascicolo, myEsito, versamento, rispostaWs,
		    descChiaveFasc, tagCSChiave);
	}

	// verifico cdkey normaliz
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.calcAndCrontrolCdKeyNorm(versamento, rispostaWs, tagCSChiave);
	}

	// verifico tipo conservazione
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    this.controllaTipoConservazione(myControlliFascicolo, rispostaWs, sessione,
		    descChiaveFasc);
	}

	// questi controlli li faccio sempre tutti, dal momento che ognuno di essi
	// può essere eseguito anche se il precedente è fallito
	if (rispostaWs.getSeverity() != SeverityEnum.ERROR) {
	    boolean prosegui = false;
	    // blocco controlli profilo archivistico/normativo/specifico

	    controllaProfiloArchivistico(myEsito, versamento, sessione);

	    controllaProfiloSpecifico(myEsito, versamento, sessione);

	    controllaProfiloNormativo(myEsito, versamento, sessione);

	    // viene sempre eseguito
	    // blocco controlli profilo generale
	    prosegui = controllaUdFascAndProfiloGenerale(myEsito, myControlliFascicolo, versamento,
		    sessione, descChiaveFasc);

	    // viene sempre eseguito (dati già controllati in precedenza)
	    if (prosegui) {
		prosegui = this.controllaFlagStruttura(versamento, rispostaWs, descChiaveFasc);
	    }

	    if (prosegui) {
		// check soggetto produttore
		this.controllaSoggettoProd(myEsito, versamento, sessione, descChiaveFasc);
		// check classificazione
		this.controllaClassificazione(myEsito, versamento, sessione, descChiaveFasc);
		// check formato numero
		this.controllaFormatoNumero(myEsito, versamento, sessione, sistema, descChiaveFasc,
			tagCSChiave);
		// check collegamenti
		this.controllaCollegamenti(myEsito, versamento, sessione);
	    }

	    // calcolo l'esito del ws in funzione di warning ed errori (sia degli errori
	    // legati al profilo archivistico che generale)
	    VoceDiErrore tmpVdE = versamento.calcolaErrorePrincipale();
	    if (tmpVdE != null) {
		rispostaWs.setSeverity(tmpVdE.getSeverity());
		if (tmpVdE.getCodiceEsito() == VoceDiErrore.TipiEsitoErrore.NEGATIVO) {
		    rispostaWs.setEsitoWsError(tmpVdE.getErrorCode(), tmpVdE.getErrorMessage());
		    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
		} else if (tmpVdE.getCodiceEsito() == VoceDiErrore.TipiEsitoErrore.WARNING) {
		    rispostaWs.setEsitoWsWarning(tmpVdE.getErrorCode(), tmpVdE.getErrorMessage());
		    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.WARNING);
		}
	    } else {
		myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
	    }

	}

	// se sono arrivato vivo fino a qui,
	// imposto la sessione di versamento a OK, in modo da
	// poter salvare il fascicolo con la relativa sessione OK
	if (rispostaWs.getSeverity() != SeverityEnum.ERROR) {
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.OK);
	}
    }

    private boolean controllaProfiloNormativo(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento, BlockingFakeSession sessione) {
	boolean prosegui = true;
	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = myEsito.getFascicolo()
		.getEsitoControlliFascicolo();
	// Controllo profilo Archivistico
	boolean isVerificaProfiloNormativoOk = controlliProfiliFascicoloService
		.verificaProfiloNormativo(versamento, sessione);
	if (!isVerificaProfiloNormativoOk) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setControlloProfiloNormativo(ECEsitoPosNegType.NEGATIVO);
	    // la lista errori è già stata popolata dal metodo, non faccio nulla
	    prosegui = false;
	} else {
	    myControlliFascicolo.setControlloProfiloNormativo(ECEsitoPosNegType.POSITIVO);
	}

	// se il controllo consistenza è fallito, il ws _deve_ interrompere il controllo
	return prosegui;
    }

    private void controllaTipoConservazione(EsitoControlliFascicolo myControlliFascicolo,
	    RispostaWSFascicolo rispostaWs, BlockingFakeSession sessione, String descChiaveFasc) {
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();
	// tipo conservazione VERSAMENTO_ANTICIPATO al momento non supportata
	//
	if (parsedIndiceFasc.getParametri().getTipoConservazione()
		.equals(TipoConservazioneType.VERSAMENTO_ANTICIPATO)) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);

	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(MessaggiWSBundle.FAS_PF_GEN_003_010, MessaggiWSBundle
		    .getString(MessaggiWSBundle.FAS_PF_GEN_003_010, descChiaveFasc));
	} else {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
	}
    }

    private boolean controllaFlagStruttura(VersFascicoloExt versamento,
	    RispostaWSFascicolo rispostaWs, String descChiaveFasc) {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();

	boolean prosegui = true;

	// verifico se abilitato controllo di classificazione alla struttura
	RispostaControlli rcCheckFlDecAaTipoFascicoloOrgStrutt = controlliFascicoliService
		.checkFlDecAaTipoFascicoloOrgStrutt(svf.getIdAATipoFasc());

	if (rcCheckFlDecAaTipoFascicoloOrgStrutt.isrBoolean()) {
	    FlControlliFasc flControlliFasc = (FlControlliFasc) rcCheckFlDecAaTipoFascicoloOrgStrutt
		    .getrObject();
	    versamento.getStrutturaComponenti().setFlControlliFasc(flControlliFasc);// set oggetto
										    // ottenuto

	    ECConfigurazioneType ecconfigurazioneType = new ECConfigurazioneType();
	    ecconfigurazioneType
		    .setAbilitaControlloClassificazione(flControlliFasc.isFlAbilitaContrClassif());
	    ecconfigurazioneType.setAccettaControlloClassificazioneNegativo(
		    flControlliFasc.isFlAccettaContrClassifNeg());
	    ecconfigurazioneType.setForzaClassificazione(flControlliFasc.isFlForzaContrFlassif());

	    ecconfigurazioneType
		    .setAbilitaControlloFormatoNumero(flControlliFasc.isFlAbilitaContrNumero());
	    ecconfigurazioneType.setAccettaControlloFormatoNumeroNegativo(
		    flControlliFasc.isFlAccettaContrNumeroNeg());
	    ecconfigurazioneType.setForzaNumero(flControlliFasc.isFlForzaContrNumero());// set
											// configurationDao
											// di
	    // struttura

	    ecconfigurazioneType
		    .setAbilitaControlloCollegamenti(flControlliFasc.isFlAbilitaContrColleg());
	    ecconfigurazioneType.setAccettaControlloCollegamentiNegativo(
		    flControlliFasc.isFlAccettaContrCollegNeg());
	    ecconfigurazioneType.setForzaCollegamento(flControlliFasc.isFlForzaContrColleg());// set
											      // configurationDao
											      // di
	    // struttura

	    // set risposta ...
	    rispostaWs.getCompRapportoVersFascicolo()
		    .setConfigurazioneStruttura(ecconfigurazioneType);

	} else {
	    // aggiungo l'errore alla lista
	    versamento.addError(descChiaveFasc, rcCheckFlDecAaTipoFascicoloOrgStrutt.getCodErr(),
		    rcCheckFlDecAaTipoFascicoloOrgStrutt.getDsErr());
	    prosegui = false;// non è stato impostato un codice di errore (non esiste da specifica
	    // perché non
	    // dovrebbe essere un caso possibile)
	}

	return prosegui;// i controlli successivi, senza il flag, non possono continuare! (non
			// dovrebbe
	// mai accadere!)
    }

    private void controllaClassificazione(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento, BlockingFakeSession sessione, String descChiaveFasc) {

	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = myEsito.getFascicolo()
		.getEsitoControlliFascicolo();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();
	boolean flForzaClass = false;

	// se esiste il profilo generale e archivistico
	if (svf.getDatiXmlProfiloGenerale() != null
		&& svf.getDatiXmlProfiloArchivistico() != null) {

	    // recupero configurationDao flag per struttura
	    FlControlliFasc flContrFasc = svf.getFlControlliFasc();

	    // se nullo ma esiste su SIP di versamento lo setto ... altrimenti se presente,
	    // setto quello da
	    // configurazione
	    if (parsedIndiceFasc.getParametri().isForzaClassificazione() != null) {
		flForzaClass = parsedIndiceFasc.getParametri().isForzaClassificazione()
			.booleanValue();// utilizza
		// questo
		// update flag (utilizzato per la scrittura su FAS_FASCICOLO)
		flContrFasc.setFlForzaContrFlassif(flForzaClass);
	    } else {
		flForzaClass = flContrFasc.isFlForzaContrFlassif();// altrimenti, usa quello di
								   // struttura
	    }

	    /*
	     * 7) Se il parametro fl_abilita_contr_classif vale false l’esito del controllo è
	     * “Controllo non eseguito”. <ControlloClassificazione> assume valore “NON_ATTIVATO”.
	     * (Non viene generato warning) ed esce dal controllo
	     */
	    if (!flContrFasc.isFlAbilitaContrClassif()) {
		myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NON_ATTIVATO);
		myControlliFascicolo.setControlloClassificazione(ECEsitoPosNegWarType.NON_ATTIVATO);
	    } else {
		// verifica sul titolario
		RispostaControlli rcVerificaSIPTitolario = controlliFascicoliService
			.verificaSIPTitolario(svf);

		if (!rcVerificaSIPTitolario.isrBoolean()) {
		    boolean isWarn = false;
		    // si eseguono una serie di verificare prima di restituire l'esito
		    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
		    /*
		     * 9) Se almeno un controllo di classificazione è NEGATIVO: a) Se “Accetta
		     * controllo classificazione negativo” è false: <ControlloClassificazione>
		     * assume valore NEGATIVO. Il sistema esce dal presente controllo sulla
		     * classificazione.
		     */
		    if (!flContrFasc.isFlAccettaContrClassifNeg()) {
			myControlliFascicolo
				.setControlloClassificazione(ECEsitoPosNegWarType.NEGATIVO);
		    } else {
			/*
			 * se <ForzaClassificazione> vale true: <ControlloClassificazione> assume
			 * valore WARNING. Il sistema esce dal presente controllo sulla
			 * classificazione. se <ForzaClassificazione> vale false:
			 * <ControlloClassificazione> assume valore NEGATIVO. Il sistema esce dal
			 * presente controllo sulla classificazione.
			 */
			if (flForzaClass) {
			    isWarn = true;
			    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.WARNING);
			    myControlliFascicolo
				    .setControlloClassificazione(ECEsitoPosNegWarType.WARNING);
			    // aggiungo l'errore restituito alla lista errori
			    versamento.addWarning(descChiaveFasc,
				    rcVerificaSIPTitolario.getCodErr(),
				    rcVerificaSIPTitolario.getDsErr());
			} else {
			    myControlliFascicolo
				    .setControlloClassificazione(ECEsitoPosNegWarType.NEGATIVO);
			}
		    }

		    if (!isWarn) {
			// aggiungo l'errore restituito alla lista errori
			versamento.addError(descChiaveFasc, rcVerificaSIPTitolario.getCodErr(),
				rcVerificaSIPTitolario.getDsErr());
		    }
		} else {

		    /*
		     * 10) Se tutti i controlli di classificazione sono positivi (quindi esiste una
		     * voce di titolario attiva alla data di apertura del fascicolo coincidente con
		     * l’indice di classificazione indicato nel SIP) l’esito controllo della
		     * classificazione sul titolario è POSITIVO. Il sistema setta il tag
		     * <ControlloClassificazione> con il valore POSITIVO.
		     */
		    // set risultato
		    versamento.getStrutturaComponenti()
			    .setIdVoceTitol(rcVerificaSIPTitolario.getrLong());
		    // se il risultato è OK lo si restituisce indipendentemente dalla condizioni a
		    // contorno
		    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
		    myControlliFascicolo.setControlloClassificazione(ECEsitoPosNegWarType.POSITIVO);
		}

	    }
	}
    }

    private void controllaFormatoNumero(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento, BlockingFakeSession sessione, String sistema,
	    String descChiaveFasc, CSChiaveFasc tagCSChiave) {
	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = myEsito.getFascicolo()
		.getEsitoControlliFascicolo();

	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	boolean flForzaContrNum = false;

	// recupero configurationDao flag per controlli
	FlControlliFasc flContrFasc = svf.getFlControlliFasc();
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();

	// se nullo ma esiste su SIP di versamento lo setto ... altrimenti se presente,
	// setto quello da configurazione
	if (parsedIndiceFasc.getParametri().isForzaNumero() != null) {
	    flForzaContrNum = parsedIndiceFasc.getParametri().isForzaNumero().booleanValue();// utilizza
											     // questo
	    // update flag (utilizzato per la scrittura su FAS_FASCICOLO)
	    flContrFasc.setFlForzaContrNumero(flForzaContrNum);
	} else {
	    flForzaContrNum = flContrFasc.isFlForzaContrNumero();// altrimenti, usa quello di
								 // struttura
	}

	// verifica formato numero e generazione chiave ordinata
	RispostaControlli rcPartiAANumero = controlliFascicoliService
		.getPartiAANumero(descChiaveFasc, svf.getIdAATipoFasc());

	// ritorna eventuali errori e prosegue con successivi controlli
	if (!rcPartiAANumero.isrBoolean()) {
	    // aggiungo l'errore restituito alla lista errori
	    versamento.addError(descChiaveFasc, rcPartiAANumero.getCodErr(),
		    rcPartiAANumero.getDsErr());
	    return;
	}

	// ha trovato le parti

	ConfigNumFasc tmpConfigNumFasc = (ConfigNumFasc) rcPartiAANumero.getrObject();
	// inizializzo il calcolo della lunghezza massima del campo numero
	KeySizeFascUtility tmpKeySizeUtility = new KeySizeFascUtility(
		svf.getVersatoreNonverificato(), tagCSChiave, sistema);
	// verifico se la chiave va bene in funzione del formato atteso per il numero
	KeyOrdFascUtility tmpKeyOrdFascUtility;
	tmpKeyOrdFascUtility = new KeyOrdFascUtility(tmpConfigNumFasc,
		tmpKeySizeUtility.getMaxLenNumero());
	// verifico se la chiave va bene e produco la chiave ordinata
	// Note : se il profilo generale non esiste si passa un "null" al fine di far
	// fallire il check gestendo comunque
	// la chiave di ordinamento
	// vale lo stesso per il profilo archivistico
	RispostaControlli rcVerificaChiave = tmpKeyOrdFascUtility.verificaChiave(tagCSChiave,
		svf.getIdStruttura(),
		svf.getDatiXmlProfiloGenerale() != null
			? svf.getDatiXmlProfiloGenerale().getDataApertura()
			: null,
		svf.getDatiXmlProfiloArchivistico() != null
			? svf.getDatiXmlProfiloArchivistico().getIndiceClassificazione()
			: null);

	if (rcVerificaChiave.isrBoolean()) {
	    // calcolo della chiave ordinata
	    KeyOrdFascUtility.KeyOrdResult keyOrdResult = (KeyOrdFascUtility.KeyOrdResult) rcVerificaChiave
		    .getrObject();
	    svf.setKeyOrdCalcolata(keyOrdResult.getKeyOrdCalcolata());
	    svf.setProgressivoCalcolato(keyOrdResult.getProgressivoCalcolato());
	} else {
	    // calcolo la chiave di ordinamento, come se il numero fosse di tipo GENERICO
	    RispostaControlli rcCalcolaKeyOrdGenerica = tmpKeyOrdFascUtility
		    .calcolaKeyOrdGenerica(tagCSChiave);
	    KeyOrdFascUtility.KeyOrdResult keyOrdResult = (KeyOrdFascUtility.KeyOrdResult) rcCalcolaKeyOrdGenerica
		    .getrObject();
	    svf.setKeyOrdCalcolata(keyOrdResult.getKeyOrdCalcolata());
	    svf.setProgressivoCalcolato(keyOrdResult.getProgressivoCalcolato());
	}

	// set della configurazione
	svf.setConfigNumFasc(tmpConfigNumFasc);

	/*
	 * 7) Se il parametro fl_abilita_contr_classif vale false l’esito del controllo è “Controllo
	 * non eseguito”. <ControlloClassificazione> assume valore “NON_ATTIVATO”. (Non viene
	 * generato warning) ed esce dal controllo
	 */
	if (!flContrFasc.isFlAbilitaContrNumero()) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NON_ATTIVATO);
	    myControlliFascicolo.setControlloFormatoNumero(ECEsitoPosNegWarType.NON_ATTIVATO);
	} else {
	    if (!rcPartiAANumero.isrBoolean()) {
		boolean isWarn = false;
		// si eseguono una serie di verificare prima di restituire l'esito
		myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
		/*
		 * c) Se “Accetta controllo formato numero negativo” è false:
		 * <ControlloFormatoNumero> assume valore NEGATIVO. Il sistema esce dal presente
		 * controllo sul formato numero.
		 */
		if (!flContrFasc.isFlAccettaContrNumeroNeg()) {
		    myControlliFascicolo.setControlloFormatoNumero(ECEsitoPosNegWarType.NEGATIVO);
		} else {
		    /*
		     * se <ForzaClassificazione> vale true: <ControlloClassificazione> assume valore
		     * WARNING. Il sistema esce dal presente controllo sulla classificazione. se
		     * <ForzaClassificazione> vale false: <ControlloClassificazione> assume valore
		     * NEGATIVO. Il sistema esce dal presente controllo sulla classificazione.
		     */
		    if (flForzaContrNum) {
			isWarn = true;
			myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.WARNING);
			myControlliFascicolo
				.setControlloFormatoNumero(ECEsitoPosNegWarType.WARNING);
			// aggiungo l'errore restituito alla lista errori
			versamento.addWarning(descChiaveFasc, rcPartiAANumero.getCodErr(),
				rcPartiAANumero.getDsErr());
			svf.setWarningFormatoNumero(true);// per salvataggio su decwarnaa
		    } else {
			myControlliFascicolo
				.setControlloFormatoNumero(ECEsitoPosNegWarType.NEGATIVO);
		    }
		}

		if (!isWarn) {
		    // aggiungo l'errore restituito alla lista errori
		    versamento.addError(descChiaveFasc, rcPartiAANumero.getCodErr(),
			    rcPartiAANumero.getDsErr());
		}
	    } else {

		/*
		 * 10) Se tutti i controlli di classificazione sono positivi (quindi esiste una voce
		 * di titolario attiva alla data di apertura del fascicolo coincidente con l’indice
		 * di classificazione indicato nel SIP) l’esito controllo della classificazione sul
		 * titolario è POSITIVO. Il sistema setta il tag <ControlloClassificazione> con il
		 * valore POSITIVO.
		 */
		// se il risultato è OK lo si restituisce indipendentemente dalla condizioni a
		// contorno
		myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
		myControlliFascicolo.setControlloFormatoNumero(ECEsitoPosNegWarType.POSITIVO);
	    }
	}
    }

    private void controllaCollegamenti(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento, BlockingFakeSession sessione) {
	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = myEsito.getFascicolo()
		.getEsitoControlliFascicolo();
	// recupero flag per risposta
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();

	boolean flForzaContrColl = false;
	boolean linkHasBuilt = true;// verifica non bloccante per i controlli
	// successivi (qualunque esito)

	// se esistono fascicoli collegati
	if (svf.getDatiXmlProfiloArchivistico() != null
		&& !svf.getDatiXmlProfiloArchivistico().getFascCollegati().isEmpty()) {

	    FlControlliFasc flContrFasc = svf.getFlControlliFasc();

	    // se nullo ma esiste su SIP di versamento lo setto ... altrimenti se presente,
	    // setto quello da configurazione
	    if (parsedIndiceFasc.getParametri().isForzaCollegamento() != null) {
		flForzaContrColl = parsedIndiceFasc.getParametri().isForzaCollegamento()
			.booleanValue();// utilizza
		// questo
		// update flag (utilizzato per la scrittura su FAS_FASCICOLO)
		flContrFasc.setFlForzaContrColleg(flForzaContrColl);
	    } else {
		flForzaContrColl = flContrFasc.isFlForzaContrColleg();// altrimenti, usa quello di
								      // struttura
	    }

	    RispostaControlli rcBuildCollegamentiFascicolo = controlliCollFascicoloService
		    .buildCollegamentiFascicolo(versamento, myControlliFascicolo);
	    // terminata elaborazone finale della lista dei collegamenti....
	    // non dovrebbe mai verificarsi (vedi check sopra), trattasi di un
	    // "overcheck"
	    if (!rcBuildCollegamentiFascicolo.isrBoolean()) {
		linkHasBuilt = false;// gestione esito
	    }
	    // eventuali (non controllati) fascicoli da inserire come link
	    List<FascicoloLink> fascicoliToBeLinked = (List<FascicoloLink>) rcBuildCollegamentiFascicolo
		    .getrObject();
	    svf.setFascicoliLinked(fascicoliToBeLinked);

	    // NON_ATTIVATO
	    // isFlAbilitaContrColleg = deve essere false
	    // - scenario 1 : nessun collegamento trovato
	    // - 2 : trovati collegamenti ma privi di errore
	    // - 3 : / il codice di errore non è FASC_006_002 (collegamenti con stessa
	    // chiave) quindi sarà possibile
	    // persistere su DB (limite max colonna desc)
	    boolean isFlAbilitaContrColleg = flContrFasc.isFlAbilitaContrColleg();
	    if (!isFlAbilitaContrColleg && (!linkHasBuilt
		    || rcBuildCollegamentiFascicolo.getCodErr() == null
		    || !rcBuildCollegamentiFascicolo.getCodErr().equals(
			    MessaggiWSBundle.FASC_006_002)/* eseguo lo stesso il controllo */)) {
		myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NON_ATTIVATO);
		myControlliFascicolo.setControlloCollegamenti(ECEsitoPosNegWarType.NON_ATTIVATO);
	    } else {
		// verifica delle chiavi
		List<CSChiaveFasc> notFoundOnDb = new ArrayList<>(0);
		// verifica decrizioni
		List<CSChiaveFasc> descError = new ArrayList<>(0);
		if (linkHasBuilt) {
		    for (Iterator<FascicoloLink> it = svf.getFascicoliLinked().iterator(); it
			    .hasNext();) {
			FascicoloLink toBeLinked = it.next();
			CSChiaveFasc csChiaveFascColl = toBeLinked.getCsChiaveFasc();
			// verifico presenza fascicolo su quella struttura
			RispostaControlli rcCheckChiave = controlliFascicoliService.checkChiave(
				csChiaveFascColl, "dummy", svf.getIdStruttura(),
				TipiGestioneFascAnnullati.CARICA);// indipendentemente
			// lo stato, interessa capire SE esiste su db

			// non esiste su DB!
			if (rcCheckChiave.isrBoolean()) {
			    notFoundOnDb.add(csChiaveFascColl);// not found on db
			} else {
			    toBeLinked.setIdLinkFasc(rcCheckChiave.getrLong());// fk fascicolo
									       // trovato
			}

			// verifica descrizione
			if (toBeLinked.getDescCollegamento()
				.length() > Costanti.COLLEGAMENTO_DESC_MAX_SIZE) {
			    descError.add(csChiaveFascColl);
			    flForzaContrColl = false; // in questo caso anche se l'errore sarebbe
						      // normalmente gestito
			    // come WARNING "forzo" ad ERRORE (non sarà mai possibile
			    // persistere questo dato)
			}

		    } // fascicoliToBeLinked
		}

		/*
		 * . Se tutti i fascicoli della struttura versante identificate con la chiave
		 * definita dai tag “Numero”, “Anno” dei tag “ChiaveCollegamento” del XML di
		 * versamento, esistono nel DB, il tag <ControlloCollegamenti> è valorizzato con
		 * “POSITIVO”.
		 *
		 * 2) Il sistema controlla che la descrizione del collegamento non superi il limite
		 * massimo previsto da DB (attualmente 256 caratteri) Se la descrizione non supera
		 * il limite consentito il tag <ControlloCollegamenti> è valorizzato con “POSITIVO”.
		 * In caso contrario il sistema genera errore
		 */
		if (linkHasBuilt && (notFoundOnDb.isEmpty() && descError.isEmpty())) {
		    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
		    myControlliFascicolo.setControlloCollegamenti(ECEsitoPosNegWarType.POSITIVO);
		} else {
		    boolean isWarn = false;
		    // si eseguono una serie di verificare prima di restituire l'esito
		    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);

		    if (!flContrFasc.isFlAccettaContrCollegNeg()) {
			myControlliFascicolo
				.setControlloCollegamenti(ECEsitoPosNegWarType.NEGATIVO);
		    } else {
			if (flForzaContrColl) {
			    isWarn = true;
			    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.WARNING);
			    myControlliFascicolo
				    .setControlloCollegamenti(ECEsitoPosNegWarType.WARNING);
			    // aggiungo l'errore restituito alla lista errori
			    for (Iterator<CSChiaveFasc> it = notFoundOnDb.iterator(); it
				    .hasNext();) {
				CSChiaveFasc csChiaveFascColl = it.next();
				// aggiungo l'errore restituito alla lista errori
				versamento.addWarning(svf.getUrnPartChiaveFascicolo(),
					MessaggiWSBundle.FASC_006_001,
					MessaggiWSBundle.getString(MessaggiWSBundle.FASC_006_001,
						MessaggiWSFormat
							.formattaUrnPartFasc(csChiaveFascColl)));
			    }

			    for (Iterator<CSChiaveFasc> it = descError.iterator(); it.hasNext();) {
				CSChiaveFasc csChiaveFascColl = it.next();
				// aggiungo l'errore restituito alla lista errori
				versamento.addWarning(svf.getUrnPartChiaveFascicolo(),
					MessaggiWSBundle.FASC_006_002,
					MessaggiWSBundle.getString(MessaggiWSBundle.FASC_006_002,
						MessaggiWSFormat
							.formattaUrnPartFasc(csChiaveFascColl)));
			    }
			} else {
			    myControlliFascicolo
				    .setControlloCollegamenti(ECEsitoPosNegWarType.NEGATIVO);
			}
		    }
		    // tutti i collegamenti, con errore, non trovati su db
		    if (!isWarn) {
			// aggiungo l'errore restituito alla lista errori
			for (Iterator<CSChiaveFasc> it = notFoundOnDb.iterator(); it.hasNext();) {
			    CSChiaveFasc csChiaveFascColl = it.next();
			    // aggiungo l'errore restituito alla lista errori
			    versamento.addError(svf.getUrnPartChiaveFascicolo(),
				    MessaggiWSBundle.FASC_006_001,
				    MessaggiWSBundle.getString(MessaggiWSBundle.FASC_006_001,
					    MessaggiWSFormat
						    .formattaUrnPartFasc(csChiaveFascColl)));
			}

			for (Iterator<CSChiaveFasc> it = descError.iterator(); it.hasNext();) {
			    CSChiaveFasc csChiaveFascColl = it.next();
			    // aggiungo l'errore restituito alla lista errori
			    versamento.addError(svf.getUrnPartChiaveFascicolo(),
				    MessaggiWSBundle.FASC_006_002,
				    MessaggiWSBundle.getString(MessaggiWSBundle.FASC_006_002,
					    MessaggiWSFormat
						    .formattaUrnPartFasc(csChiaveFascColl)));
			}
		    }
		}
	    }

	}
    }

    private void controllaUserIdAndVersioneWS(
	    ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo,
	    VersFascicoloExt versamento, RispostaWSFascicolo rispostaWs,
	    BlockingFakeSession sessione) {
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();

	// come prima cosa verifico che il versatore e la versione dichiarati nel WS
	// coincidano
	// con quelli nell'xml
	if (!parsedIndiceFasc.getIntestazione().getVersatore().getUserID()
		.equals(versamento.getLoginName())) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setIdentificazioneVersatore(ECEsitoPosNegType.NEGATIVO);
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.FAS_CONFIG_002_001,
		    parsedIndiceFasc.getIntestazione().getVersatore().getUserID());
	    // se non coincidono questi elementi posso sempre
	    // tentare di riscostruire la struttura, la chiave ed il tipo fascicolo
	    // e trasformare la sessione errata in una sessione fallita
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	}

	if (rispostaWs.getSeverity() == SeverityEnum.OK && !parsedIndiceFasc.getParametri()
		.getVersioneIndiceSIPFascicolo().equals(versamento.getVersioneWsChiamata())) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setIdentificazioneVersatore(ECEsitoPosNegType.NEGATIVO);
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.FAS_CONFIG_003_003,
		    parsedIndiceFasc.getParametri().getVersioneIndiceSIPFascicolo());
	    // se non coincidono questi elementi posso sempre
	    // tentare di riscostruire la struttura, la chiave ed il tipo fascicolo
	    // e trasformare la sessione errata in una sessione fallita
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	}
    }

    private void controllaStruttura(ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo,
	    VersFascicoloExt versamento, RispostaWSFascicolo rispostaWs,
	    CSVersatore tagCSVersatore) {
	// verifica la struttura versante
	RispostaControlli rcCheckIdStrut = controlliSemanticiService.checkIdStrut(tagCSVersatore,
		versamento.getStrutturaComponenti().getDataVersamento());
	if (rcCheckIdStrut.getrLong() < 1) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setIdentificazioneVersatore(ECEsitoPosNegType.NEGATIVO);
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcCheckIdStrut.getCodErr(), rcCheckIdStrut.getDsErr());
	    if (rcCheckIdStrut.getrLongExtended() < 1) {
		// se anche questo controllo fallisce la sessione
		// è sempre errata, visto quanto dichiarato non esiste
		// e non ho alcun modo di determinare la struttura
		rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.ERRATA);
	    } else {
		// se rLongExtended è valorizzato, la struttura esiste (ma è template):
		// posso sempre tentare di riscostruire la chiave ed il tipo fascicolo
		// e trasformare la sessione errata in una sessione fallita
		rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
		// salvo idstruttura template
		versamento.getStrutturaComponenti()
			.setIdStruttura(rcCheckIdStrut.getrLongExtended());
		// salvo idUtente
		versamento.getStrutturaComponenti().setIdUser(versamento.getUtente().getIdUtente());
	    }
	} else {
	    // salvo idstruttura individuata
	    versamento.getStrutturaComponenti().setIdStruttura(rcCheckIdStrut.getrLong());
	    // salvo idUtente
	    versamento.getStrutturaComponenti().setIdUser(versamento.getUtente().getIdUtente());
	}

	// verifica se l'utente è autorizzato ad usare il WS sulla struttura
	if (rispostaWs.getSeverity() == SeverityEnum.OK) {
	    versamento.getUtente().setIdOrganizzazioneFoglia(
		    new BigDecimal(versamento.getStrutturaComponenti().getIdStruttura()));
	    RispostaControlli rcCheckAuthWS = controlliWsService.checkAuthWS(versamento.getUtente(),
		    versamento.getDescrizione());
	    if (!rcCheckAuthWS.isrBoolean()) {
		myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
		myControlliFascicolo.setIdentificazioneVersatore(ECEsitoPosNegType.NEGATIVO);
		rispostaWs.setSeverity(SeverityEnum.ERROR);
		rispostaWs.setEsitoWsError(rcCheckAuthWS.getCodErr(), rcCheckAuthWS.getDsErr());
		// se non è autorizzato posso sempre
		// tentare di riscostruire la chiave ed il tipo fascicolo
		// e trasformare la sessione errata in una sessione fallita
		rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	    }
	}
    }

    private void controllaTipoFasc(ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo,
	    VersFascicoloExt versamento, RispostaWSFascicolo rispostaWs, String descChiaveFasc) {
	// verifico il tipo fascicolo - in caso di errore la sessione è DUBBIA
	//
	RispostaControlli rcCheckTipoFascicolo = controlliFascicoliService.checkTipoFascicolo(
		versamento.getStrutturaComponenti().getTipoFascicoloNonverificato(), descChiaveFasc,
		versamento.getStrutturaComponenti().getIdStruttura());
	if (!rcCheckTipoFascicolo.isrBoolean()) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setVerificaTipoFascicolo(ECEsitoPosNegType.NEGATIVO);
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcCheckTipoFascicolo.getCodErr(),
		    rcCheckTipoFascicolo.getDsErr());
	    // se non è verificato posso sempre
	    // tentare di riscostruire la chiave ed il tipo fascicolo
	    // e trasformare la sessione errata in una sessione fallita
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	} else {
	    // salvo idtipofascicolo individuato
	    versamento.getStrutturaComponenti().setIdTipoFascicolo(rcCheckTipoFascicolo.getrLong());
	    // OK - popolo la risposta versamento
	    myControlliFascicolo.setVerificaTipoFascicolo(ECEsitoPosNegType.POSITIVO);
	}
    }

    private void controllaTipoFascUserOrg(EsitoControlliFascicolo myControlliFascicolo,
	    VersFascicoloExt versamento, RispostaWSFascicolo rispostaWs, String descChiaveFasc) {
	// verifico il tipo fascicolo abilitato per utente/org - in caso di errore la
	// sessione è DUBBIA
	//
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	RispostaControlli rcCheckTipoFascicoloIamUserOrganizzazione = controlliFascicoliService
		.checkTipoFascicoloIamUserOrganizzazione(descChiaveFasc,
			svf.getTipoFascicoloNonverificato(), svf.getIdStruttura(), svf.getIdUser(),
			svf.getIdTipoFascicolo());
	if (!rcCheckTipoFascicoloIamUserOrganizzazione.isrBoolean()) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setVerificaTipoFascicolo(ECEsitoPosNegType.NEGATIVO);
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcCheckTipoFascicoloIamUserOrganizzazione.getCodErr(),
		    rcCheckTipoFascicoloIamUserOrganizzazione.getDsErr());
	    // se non è verificato posso sempre
	    // tentare di riscostruire la chiave ed il tipo fascicolo
	    // e trasformare la sessione errata in una sessione fallita
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	} else {
	    versamento.getStrutturaComponenti()
		    .setIdIamAbilTipoDato(rcCheckTipoFascicoloIamUserOrganizzazione.getrLong());
	    // OK - popolo la risposta versamento
	    myControlliFascicolo.setVerificaTipoFascicolo(ECEsitoPosNegType.POSITIVO);
	}
    }

    private void controllaTipoFascAnno(EsitoControlliFascicolo myControlliFascicolo,
	    VersFascicoloExt versamento, RispostaWSFascicolo rispostaWs, String descChiaveFasc) {
	// verifico il tipo fascicolo abilito su anno - in caso di errore la sessione è
	// DUBBIA
	//
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	RispostaControlli rcCheckTipoFascicoloAnno = controlliFascicoliService
		.checkTipoFascicoloAnno(descChiaveFasc, svf.getTipoFascicoloNonverificato(),
			svf.getChiaveNonVerificata().getAnno(), svf.getIdTipoFascicolo());
	if (!rcCheckTipoFascicoloAnno.isrBoolean()) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setVerificaTipoFascicolo(ECEsitoPosNegType.NEGATIVO);
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcCheckTipoFascicoloAnno.getCodErr(),
		    rcCheckTipoFascicoloAnno.getDsErr());
	    // se non è verificato posso sempre
	    // tentare di riscostruire la chiave ed il tipo fascicolo
	    // e trasformare la sessione errata in una sessione fallita
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	} else {
	    // salvo idaddtipofasc individuato
	    versamento.getStrutturaComponenti()
		    .setIdAATipoFasc(rcCheckTipoFascicoloAnno.getrLong());
	    // OK - popolo la risposta versamento
	    myControlliFascicolo.setVerificaTipoFascicolo(ECEsitoPosNegType.POSITIVO);
	}

    }

    private void controllaChiave(ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo,
	    CompRapportoVersFascicolo myEsito, VersFascicoloExt versamento,
	    RispostaWSFascicolo rispostaWs, String descChiaveFasc, CSChiaveFasc tagCSChiave) {
	RispostaControlli rcCheckChiave = controlliFascicoliService.checkChiave(tagCSChiave,
		descChiaveFasc, versamento.getStrutturaComponenti().getIdStruttura(),
		TipiGestioneFascAnnullati.CONSIDERA_ASSENTE);
	if (!rcCheckChiave.isrBoolean()) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setUnivocitaChiave(ECEsitoPosNegType.NEGATIVO);
	    if (rcCheckChiave.getrLong() > 0) {
		// se ho trovato un elemento doppio, è un errore specifico
		myEsito.setRapportoVersamento(rcCheckChiave.getrString());
		rispostaWs.setErroreElementoDoppio(true);
		rispostaWs.setIdElementoDoppio(rcCheckChiave.getrLong());
	    } else {
		// altrimenti è un errore generico legato al db
		rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.ERRATA);
	    }
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rcCheckChiave.getCodErr(), rcCheckChiave.getDsErr());
	} else {
	    // OK - popolo la risposta versamento
	    myControlliFascicolo.setUnivocitaChiave(ECEsitoPosNegType.POSITIVO);
	}
    }

    private void calcAndCrontrolCdKeyNorm(VersFascicoloExt versamento,
	    RispostaWSFascicolo rispostaWs, CSChiaveFasc tagCSChiave) {
	// calcola e verifica la chiave normalizzata
	String cdKeyNormalized = MessaggiWSFormat.normalizingKey(tagCSChiave.getNumero()); // base
	RispostaControlli rispostaControlli = controlliFascicoliService.calcAndcheckCdKeyNormalized(
		tagCSChiave, versamento.getStrutturaComponenti().getIdStruttura(), cdKeyNormalized);
	if (!rispostaControlli.isrBoolean()) {
	    // 666 error
	    rispostaWs.setSeverity(SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(rispostaControlli.getCodErr(), rispostaControlli.getDsErr());
	} else {
	    // normalized cd_key
	    versamento.getStrutturaComponenti()
		    .setCdKeyFascicoloNormalized(rispostaControlli.getrString());
	}
    }

    private boolean controllaProfiloArchivistico(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento, BlockingFakeSession sessione) {

	boolean prosegui = true;
	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = myEsito.getFascicolo()
		.getEsitoControlliFascicolo();
	// Controllo profilo Archivistico
	boolean isVerificaProfiloArchivisticoOk = controlliProfiliFascicoloService
		.verificaProfiloArchivistico(versamento, myEsito.getFascicolo(), sessione);
	if (!isVerificaProfiloArchivisticoOk) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setControlloProfiloArchivistico(ECEsitoPosNegType.NEGATIVO);
	    // la lista errori è già stata popolata dal metodo, non faccio nulla
	    prosegui = false;
	} else {
	    myControlliFascicolo.setControlloProfiloArchivistico(ECEsitoPosNegType.POSITIVO);
	}

	// se il controllo consistenza è fallito, il ws _deve_ interrompere il controllo
	return prosegui;
    }

    private boolean controllaUdFascAndProfiloGenerale(CompRapportoVersFascicolo myEsito,
	    ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo,
	    VersFascicoloExt versamento, BlockingFakeSession sessione, String descChiaveFasc) {
	// Controllo Consistenza; è bloccante per quanto riguarda il controllo
	// successivo
	boolean prosegui = true;
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();

	boolean isVerificaUdFascicoloOk = controlliCollFascicoloService
		.verificaUdFascicolo(versamento, myEsito.getFascicolo(), sessione);
	if (!isVerificaUdFascicoloOk) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo
		    .setControlloConsistenzaUnitaDocumentarie(ECEsitoPosNegType.NEGATIVO);
	    // la lista errori è già stata popolata dal metodo, non faccio nulla
	    prosegui = false;
	} else {
	    myControlliFascicolo
		    .setControlloConsistenzaUnitaDocumentarie(ECEsitoPosNegType.POSITIVO);
	}

	// sotto-fascicoli non implementati
	if (parsedIndiceFasc.getContenuto().getFascicoli() != null) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setControlloConsistenzaFascicoli(ECEsitoPosNegType.NEGATIVO);
	    // aggiungo l'errore alla lista
	    versamento.listErrAddError(descChiaveFasc, MessaggiWSBundle.FAS_CONFIG_007_001);
	    prosegui = false;
	} else {
	    myControlliFascicolo.setControlloConsistenzaFascicoli(ECEsitoPosNegType.NON_ATTIVATO);
	}

	if (prosegui) {
	    // Controllo profilo Generale
	    boolean isVerificaProfiloGeneraleOk = controlliProfiliFascicoloService
		    .verificaProfiloGenerale(versamento, myEsito.getFascicolo(), sessione);
	    if (!isVerificaProfiloGeneraleOk) {
		myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
		myControlliFascicolo.setControlloProfiloGenerale(ECEsitoPosNegType.NEGATIVO);
		// la lista errori è già stata popolata dal metodo, non faccio nulla
		// prosegui = false;//bloccante per i successivi
	    } else {
		myControlliFascicolo.setControlloProfiloGenerale(ECEsitoPosNegType.POSITIVO);
	    }
	}
	return prosegui;
    }

    private boolean controllaProfiloSpecifico(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento, BlockingFakeSession syncFakeSession) {
	boolean prosegui = true;
	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = myEsito.getFascicolo()
		.getEsitoControlliFascicolo();
	// Controllo profilo Archivistico
	boolean isVerificaProfiloSpecificoOk = controlliProfiliFascicoloService
		.verificaProfiloSpecifico(versamento, syncFakeSession);
	if (!isVerificaProfiloSpecificoOk) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	    myControlliFascicolo.setControlloProfiloSpecifico(ECEsitoPosNegType.NEGATIVO);
	    // la lista errori è già stata popolata dal metodo, non faccio nulla
	    prosegui = false;
	} else {
	    myControlliFascicolo.setControlloProfiloSpecifico(ECEsitoPosNegType.POSITIVO);
	}

	// se il controllo consistenza è fallito, il ws _deve_ interrompere il controllo
	return prosegui;
    }

    private void controllaSoggettoProd(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento, BlockingFakeSession sessione, String descChiaveFasc) {
	ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo = myEsito.getFascicolo()
		.getEsitoControlliFascicolo();
	IndiceSIPFascicolo parsedIndiceFasc = sessione.getIndiceSIPFascicolo();

	// al momento non supportato
	if (parsedIndiceFasc.getIntestazione().getSoggettoProduttore() != null) {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NON_ATTIVATO);
	    myControlliFascicolo
		    .setIdentificazioneSoggettoProduttore(ECEsitoPosNegWarType.NON_ATTIVATO);
	    // aggiungo l'errore alla lista
	    versamento.listErrAddError(descChiaveFasc, MessaggiWSBundle.FAS_CONFIG_004_003);
	}
	// per il momento commentato (codice testato e funzionante da attivare
	// nella prossima versione del WS)

	// if (parsedIndiceFasc.getIntestazione().getSoggettoProduttore() != null) {
	//
	// String code =
	// parsedIndiceFasc.getIntestazione().getSoggettoProduttore().getCodice();
	// String den =
	// parsedIndiceFasc.getIntestazione().getSoggettoProduttore().getDenominazione();
	//
	//
	// /*
	// * Nota: vengono sempre testati entrambi i campi!
	// *
	// * Casi 1 : codice 2 : denominazione
	// */
	// // verifica per codice
	// if (StringUtils.isNotBlank(code)) {
	//
	// rispostaControlli =
	// controlliFascicoliService.verificaCodSoggettoProduttore(versamento, code);
	//
	// if (!rispostaControlli.isrBoolean()) {
	// myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	// myControlliFascicolo.setIdentificazioneSoggettoProduttore(ECEsitoPosNegWarType.NEGATIVO);
	// } else {
	// versamento.getStrutturaComponenti().setIdOrgEnteConv(rispostaControlli.getrLong());
	// myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
	// myControlliFascicolo.setIdentificazioneSoggettoProduttore(ECEsitoPosNegWarType.POSITIVO);
	// }
	// } // codice
	// // verifica per denominazione
	// if (StringUtils.isNotBlank(den)) {
	//
	// rispostaControlli =
	// controlliFascicoliService.verificaDenSoggettoProduttore(versamento, den);
	//
	// if (!rispostaControlli.isrBoolean()) {
	// myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	// myControlliFascicolo.setIdentificazioneSoggettoProduttore(ECEsitoPosNegWarType.NEGATIVO);
	// } else {
	// versamento.getStrutturaComponenti().setIdOrgEnteConv(rispostaControlli.getrLong());
	// myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.POSITIVO);
	// myControlliFascicolo.setIdentificazioneSoggettoProduttore(ECEsitoPosNegWarType.POSITIVO);
	//
	// }
	// } // denominazione
	// //Nota: non indicati codice/denominazione -> si restituiscono
	// // entrambi gli errori?
	// if (StringUtils.isBlank(code) && StringUtils.isBlank(den)) {
	//
	// myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NEGATIVO);
	// myControlliFascicolo.setIdentificazioneSoggettoProduttore(ECEsitoPosNegWarType.NEGATIVO);
	// // aggiungo
	// // l'errore restituito alla lista errori
	// versamento.addError(descChiaveFasc, MessaggiWSBundle.FAS_CONFIG_004_001,
	// MessaggiWSBundle.getString(MessaggiWSBundle.FAS_CONFIG_004_001, "N/A"));
	// // aggiungo l'errore restituito alla lista errori
	// versamento.addError(descChiaveFasc, MessaggiWSBundle.FAS_CONFIG_004_002,
	// MessaggiWSBundle.getString(MessaggiWSBundle.FAS_CONFIG_004_002, "N/A"));
	// } }

	else {
	    myControlliFascicolo.setCodiceEsito(ECEsitoPosNegWarType.NON_ATTIVATO);
	    myControlliFascicolo
		    .setIdentificazioneSoggettoProduttore(ECEsitoPosNegWarType.NON_ATTIVATO);
	}
    }

    // MEV#25288
    private void buildUrnSipOnEsito(CompRapportoVersFascicolo myEsito,
	    VersFascicoloExt versamento) {

	// calcolo l'URN del Fascicolo
	String tmpUrn = versamento.getStrutturaComponenti().getUrnPartChiaveFascicolo();
	// calcolo URN del SIP del Fascicolo
	String urnSip = MessaggiWSFormat.formattaUrnSip(tmpUrn, Costanti.UrnFormatter.URN_SIP_FASC);
	//
	myEsito.setURNSIP(urnSip);

    }
    // end MEV#25288
}
