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

import static it.eng.parer.fascicolo.beans.utils.Costanti.DESCKEY_FASCICOLO_DUMMY;
import static it.eng.parer.fascicolo.beans.utils.Costanti.TipiGestioneFascAnnullati.CONSIDERA_ASSENTE;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import it.eng.parer.fascicolo.beans.IControlliFascicoliService;
import it.eng.parer.fascicolo.beans.IControlliSemanticiService;
import it.eng.parer.fascicolo.beans.IControlliWsService;
import it.eng.parer.fascicolo.beans.IRecupSessDubbieFascService;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.CSVersatore;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RecupSessDubbieFascService implements IRecupSessDubbieFascService {

    private static final Logger log = LoggerFactory.getLogger(RecupSessDubbieFascService.class);
    //
    @Inject
    EntityManager entityManager;

    // stateless ejb per i controlli sul db
    @Inject
    IControlliSemanticiService controlliSemanticiService;

    @Inject
    IControlliFascicoliService controlliFascicoliService;

    @Inject
    IControlliWsService controlliWsService;

    /*
     * //Markdown:
     *
     * # casi di uso della sessione DUBBIA
     *
     * |Caso |Ho Id Utente |Ho XML parsed|Ho ID Struttura|Versatore OK|
     * |----------------------------------|:-----------:|:-----------:|:------------
     * -:|:----------:| |Errore di versione ws | | | | | |Errore di credenziali | | | | | |Errore di
     * parser |SI | | | | |Versatore diverso tra ws e xml | |SI | | | |Versione diversa tra ws e xml
     * | |SI | | | |Struttura template | |SI |SI | | |Utente non autorizzato a Struttura| |SI |SI |
     * | |Tipo fascicolo inesistente | |SI |SI |SI |
     *
     * La versione non ha molta importanza: anche se manca oppure è prova di senso posso sempre
     * tentare di scrivere qualcosa nella tabella sessione fallita
     *
     * se le credenziali sono errate, perché non è stato fornito l'utente o se la password è errata.
     * dovrò in ogni caso salvare l'utente come nullo
     *
     * La chiave non ha molta importanza, viene scritta come si trova anche nelle sessioni fallite
     *
     * in pratica se ho un'id struttura ed un id fascicolo, la sessione è per lo meno FALLITA
     *
     * in definitiva, lo scopo di questo modulo è quello di restituire queste due informazioni, così
     * da poterle registare nelle tabelle delle sessioni fallite.
     *
     * se ricavo questi dati devo in ogni caso verificare che la chiave non sia doppia (come nel
     * versamento)
     *
     * anche se la chiave non è doppia, devo verificare che per quella chiave non ci siano già
     * errori (questo è gestito dal procedimento ordinario)
     *
     */
    //
    @Override
    @Transactional
    public void recuperaSessioneErrata(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
	    BlockingFakeSession sessione) {
	// questo codice ha senso solo per le sesioni DUBBIE, per capire se è
	// è possibile recuperare i dati essenziali per il salvataggio di una sessione
	// FALLITA pur non avendoli ricavati durante il normale processo del WS
	if (rispostaWs.getStatoSessioneVersamento() == IRispostaWS.StatiSessioneVersEnum.DUBBIA) {
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.ERRATA);
	    // verifico se le credenziali o la versione ws inserite sono errate
	    // e devo quindi ricalcolare tutto
	    if (versamento.getUtente() == null) {
		// in realtà non devo fare niente di diverso dal caso successivo:
		// se le credenziali sono errate, l'utente è comunque nullo
		// e non lo devo recuperare
		if (!this.recuperaDatiDaXml(versamento)) {
		    return;
		}
		if (!this.recuperaStrutturaDaVersatore(versamento)) {
		    return;
		}
		if (!this.recuperaTipoFascicolo(versamento)) {
		    return;
		}
	    }

	    // verifico se sono fallito per un errore del parser
	    if (sessione.getIndiceSIPFascicolo() == null) {
		if (!this.recuperaDatiDaXml(versamento)) {
		    return;
		}
		if (!this.recuperaStrutturaDaVersatore(versamento)) {
		    return;
		}
		if (!this.recuperaTipoFascicolo(versamento)) {
		    return;
		}
	    }
	    // verifico se ho effettuato il marshall ma non ho un id struttura
	    // avviene se versatore e/o versione xml non coincidono con la chiamata ws
	    if (sessione.getIndiceSIPFascicolo() != null
		    && versamento.getStrutturaComponenti().getIdStruttura() < 1) {
		if (!this.recuperaStrutturaDaVersatore(versamento)) {
		    return;
		}
		if (!this.recuperaTipoFascicolo(versamento)) {
		    return;
		}
	    }

	    // verifico se ho l'idstruttura ma il versatore non è valido
	    // (avviene se la struttura è template o se il versatore non è autorizzato ad
	    // usarla)
	    if (sessione.getIndiceSIPFascicolo() != null
		    && versamento.getStrutturaComponenti().getIdStruttura() > 0
		    && !versamento.getStrutturaComponenti().isVersatoreVerificato()) {
		if (!this.recuperaTipoFascicolo(versamento)) {
		    return;
		}
	    }
	    // verifico se ho l'idstruttura ed il versatore è valido
	    // (avviene se il tipo fascicolo dichiarato non è valido)
	    if (sessione.getIndiceSIPFascicolo() != null
		    && versamento.getStrutturaComponenti().getIdStruttura() > 0
		    && versamento.getStrutturaComponenti().isVersatoreVerificato()
		    && versamento.getStrutturaComponenti().getIdTipoFascicolo() < 1) {
		if (!this.recuperaTipoFascicoloStandard(versamento)) {
		    return;
		}
	    }
	    // il controllo della eventuale duplicazione della chiave
	    // lo devo fare sempre e comunque.
	    // Da notare che a questo punto la sessione è certamente FALLITA
	    // (a meno di errori sul DB...)
	    if (!this.controllaChiaveFascicolo(rispostaWs, versamento)) {
		return;
	    }
	    // sono sopravvissuto ai controlli, quindi posso dichiarare la sessione come
	    // FALLITA
	    // dal momento che ho recuperato e verificato tutti gli elementi necessari alla
	    // sua
	    // registrazione
	    rispostaWs.setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.FALLITA);
	}
    }

    private boolean recuperaDatiDaXml(VersFascicoloExt versamento) {
	boolean trovatiDatiEssenziali = false;
	if (StringUtils.isNotBlank(versamento.getDatiXml())) {
	    CSVersatore tmpVersatore = new CSVersatore();
	    CSChiaveFasc tmpChiave = new CSChiaveFasc();
	    boolean cercaStruttura = false;
	    boolean cercaChiave = false;
	    try {
		Node tmpDati = this.convertStringToDocument(versamento.getDatiXml().trim());

		if (tmpDati != null) {
		    XPathFactory xpathfactory = XPathFactory.newInstance();
		    XPath xpath = xpathfactory.newXPath();

		    XPathExpression expr = xpath
			    .compile("//Intestazione/Versatore/Ambiente/text()");
		    NodeList nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
		    for (int i = 0; i < nodes.getLength(); i++) {
			tmpVersatore.setAmbiente(nodes.item(i).getNodeValue());
		    }
		    cercaStruttura = nodes.getLength() != 0;
		    //
		    expr = xpath.compile("//Intestazione/Versatore/Ente/text()");
		    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
		    for (int i = 0; i < nodes.getLength(); i++) {
			tmpVersatore.setEnte(nodes.item(i).getNodeValue());
		    }
		    cercaStruttura = nodes.getLength() != 0 && cercaStruttura;
		    //
		    expr = xpath.compile("//Intestazione/Versatore/Struttura/text()");
		    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
		    for (int i = 0; i < nodes.getLength(); i++) {
			tmpVersatore.setStruttura(nodes.item(i).getNodeValue());
		    }
		    cercaStruttura = nodes.getLength() != 0 && cercaStruttura;
		    // cerca la chiave, se è definita
		    expr = xpath.compile("//Intestazione/Chiave/Anno/text()");
		    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
		    for (int i = 0; i < nodes.getLength(); i++) {
			String tmpString = nodes.item(i).getNodeValue();
			if (tmpString.matches("-?\\d+")) {
			    tmpChiave.setAnno(Integer.parseInt(tmpString));
			    cercaChiave = true;
			}
		    }
		    //
		    expr = xpath.compile("//Intestazione/Chiave/Numero/text()");
		    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
		    for (int i = 0; i < nodes.getLength(); i++) {
			tmpChiave.setNumero(nodes.item(i).getNodeValue());
		    }
		    cercaChiave = cercaChiave && nodes.getLength() != 0;
		    // memorizzo il tipo fascicolo non verificato
		    expr = xpath.compile("//Intestazione/TipoFascicolo/text()");
		    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
		    for (int i = 0; i < nodes.getLength(); i++) {
			versamento.getStrutturaComponenti()
				.setTipoFascicoloNonverificato(nodes.item(i).getNodeValue());
		    }
		    trovatiDatiEssenziali = cercaStruttura && cercaChiave && nodes.getLength() != 0;
		}
	    } catch (XPathExpressionException ex) {
		log.atError().log("errore xpath!!", ex);
	    } catch (Exception ex) {
		log.atError().log("errore generico, " + "ma a questo punto ormai tutto è perduto, "
			+ "inutile notificarlo: ", ex);
	    }
	    if (cercaStruttura) {
		versamento.getStrutturaComponenti().setVersatoreNonverificato(tmpVersatore);
	    }
	    if (cercaChiave) {
		versamento.getStrutturaComponenti().setChiaveNonVerificata(tmpChiave);
	    }
	}
	return trovatiDatiEssenziali;
    }

    private Document convertStringToDocument(String xmlStr) {
	try {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    // to be compliant, completely disable DOCTYPE declaration:
	    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	    // or completely disable external entities declarations:
	    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
	    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
	    // or prohibit the use of all protocols by external entities:
	    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
	    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    return builder.parse(new InputSource(new StringReader(xmlStr)));
	} catch (IOException | ParserConfigurationException | SAXException e) {
	    // in caso di eccezione non faccio nulla:
	    // è perfettamente accettabile che questa stringa, che ha già fallito una
	    // validazione, non sia well-formed e provochi uh'eccezione.
	    log.atWarn().log("errore parsing xml durante recupero: ", e);
	}
	return null;
    }

    private boolean recuperaStrutturaDaVersatore(VersFascicoloExt versamento) {
	boolean trovato = false;
	CSVersatore tmpVersatore = versamento.getStrutturaComponenti().getVersatoreNonverificato();
	if (tmpVersatore != null) {
	    RispostaControlli rcCheckIdStrut = controlliSemanticiService.checkIdStrut(tmpVersatore,
		    versamento.getStrutturaComponenti().getDataVersamento());
	    if (rcCheckIdStrut.getrLongExtended() > 0) {
		trovato = true;
		versamento.getStrutturaComponenti()
			.setIdStruttura(rcCheckIdStrut.getrLongExtended());
	    }
	}
	return trovato;
    }

    private boolean recuperaTipoFascicolo(VersFascicoloExt versamento) {
	boolean trovato = false;

	RispostaControlli rcCheckTipoFasc = controlliFascicoliService.checkTipoFascicolo(
		versamento.getStrutturaComponenti().getTipoFascicoloNonverificato(),
		DESCKEY_FASCICOLO_DUMMY, versamento.getStrutturaComponenti().getIdStruttura());
	if (rcCheckTipoFasc.isrBoolean()) {
	    // salvo idtipofascicolo individuato
	    versamento.getStrutturaComponenti().setIdTipoFascicolo(rcCheckTipoFasc.getrLong());
	    trovato = true;
	} else {
	    // se il tipo fascicolo non esiste tento di cercare un tipo
	    // fascicolo standard definito per la struttura.
	    // Se lo trovo potrò probabilmente avere abbastanza informazioni per
	    // scrivere una sessione FALLITA invece che ERRATA
	    rcCheckTipoFasc = controlliFascicoliService.checkTipoFascicoloSconosciuto(
		    versamento.getStrutturaComponenti().getIdStruttura());
	    if (rcCheckTipoFasc.isrBoolean()) {
		// salvo idtipofascicolo individuato
		versamento.getStrutturaComponenti().setIdTipoFascicolo(rcCheckTipoFasc.getrLong());
		trovato = true;
	    }
	}
	return trovato;
    }

    private boolean recuperaTipoFascicoloStandard(VersFascicoloExt versamento) {
	boolean trovato = false;
	// se il tipo fascicolo non esiste tento di cercare un tipo
	// fascicolo standard definito per la struttura.
	// Se lo trovo potrò probabilmente avere abbastanza informazioni per
	// scrivere una sessione FALLITA invece che ERRATA
	RispostaControlli rcTipoFascUnknown = controlliFascicoliService
		.checkTipoFascicoloSconosciuto(
			versamento.getStrutturaComponenti().getIdStruttura());
	if (rcTipoFascUnknown.isrBoolean()) {
	    // salvo idtipofascicolo individuato
	    versamento.getStrutturaComponenti().setIdTipoFascicolo(rcTipoFascUnknown.getrLong());
	    trovato = true;
	}
	return trovato;
    }

    private boolean controllaChiaveFascicolo(RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento) {
	boolean controlloOk = true;

	CSChiaveFasc tagCSChiave = versamento.getStrutturaComponenti().getChiaveNonVerificata();

	RispostaControlli rcCheckChiave = controlliFascicoliService.checkChiave(tagCSChiave,
		DESCKEY_FASCICOLO_DUMMY, versamento.getStrutturaComponenti().getIdStruttura(),
		CONSIDERA_ASSENTE);
	if (!rcCheckChiave.isrBoolean()) {
	    if (rcCheckChiave.getrLong() > 0) {
		// se ho trovato un elemento doppio, è una sessione fallita
		rispostaWs.setErroreElementoDoppio(true);
		rispostaWs.setIdElementoDoppio(rcCheckChiave.getrLong());
	    } else {
		// altrimenti è un errore generico legato al db
		controlloOk = false;
	    }
	}

	return controlloOk;
    }

}
