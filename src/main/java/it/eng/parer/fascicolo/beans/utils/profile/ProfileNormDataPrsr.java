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

/**
 *
 */
package it.eng.parer.fascicolo.beans.utils.profile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.StringJoiner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.eng.parer.fascicolo.beans.dto.profile.norm.DXPNEvento;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DXPNIdentSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DXPNSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DatiXmlProfiloNormativo;
import it.eng.parer.fascicolo.beans.utils.Costanti;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloNormativoType;

public class ProfileNormDataPrsr {

    private static final Logger log = LoggerFactory.getLogger(ProfileNormDataPrsr.class);

    private ProfileNormDataPrsr() {
	throw new IllegalStateException("Utility class");
    }

    /**
     * Il parsing dei dati provenienti dal profilo normativo prevede: - estrazione soggetti -
     * definizione di eventi su soggetti
     *
     * @param pnt profilo normativo da indice SIP
     *
     * @return dto del profilo normativo
     */
    public static DatiXmlProfiloNormativo recuperaDatiDaXmlPN(ProfiloNormativoType pnt) {
	DatiXmlProfiloNormativo tmpDatiXml = new DatiXmlProfiloNormativo();
	final String selector = "AggregazioneDocumentaliInformatiche";
	try {
	    //
	    Node tmpnode = null;
	    Node tmpDati = pnt.getAny();
	    XPathFactory xpathfactory = XPathFactory.newInstance();
	    XPath xpath = xpathfactory.newXPath();

	    // soggetti
	    XPathExpression expr = xpath.compile("//" + selector + "/Soggetti" + "/Ruolo");
	    NodeList nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		tmpnode = nodes.item(i);
		if (tmpnode != null && tmpnode.getNodeType() == Node.ELEMENT_NODE) {
		    tmpDatiXml.addSoggetto(
			    recuperaSoggettoPN("AmministrazionePartecipante", tmpnode));
		    tmpDatiXml.addSoggetto(recuperaSoggettoPN("AmministrazioneTitolare", tmpnode));
		    tmpDatiXml.addSoggetto(
			    recuperaSoggettoPN("SoggettoIntestatarioPersonaFisica", tmpnode));
		    tmpDatiXml.addSoggetto(
			    recuperaSoggettoPN("SoggettoIntestatarioPersonaGiuridica", tmpnode));
		    tmpDatiXml.addSoggetto(recuperaSoggettoPN("Assegnatario", tmpnode));
		    tmpDatiXml.addSoggetto(recuperaSoggettoPN("RUP", tmpnode));
		}
	    }

	    // assegnazioni (soggetto + evento)
	    expr = xpath.compile("//" + selector + "/Assegnazione" + "/TipoAssegnazione");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		tmpnode = nodes.item(i);
		if (tmpnode != null && tmpnode.getNodeType() == Node.ELEMENT_NODE) {
		    tmpDatiXml.addSoggetto(recuperaAssegnazione("PerCompetenza", tmpnode, i));
		    tmpDatiXml.addSoggetto(recuperaAssegnazione("PerConoscenza", tmpnode, i));
		}
	    }

	    // aggregazione (solo per validazione)
	    expr = xpath.compile("//" + selector + "/IdAgg" + "/TipoAggregazione/text()");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		tmpDatiXml.setTipoAggreg(nodes.item(i).getNodeValue());
	    }
	} catch (Exception ex) {
	    log.atError().log("Errore recupero dati di profilo normativo", ex);
	    return null;
	}

	return tmpDatiXml;
    }

    private static DXPNSoggetto recuperaSoggettoPN(final String tagName, final Node parentNode) {
	// init dto
	DXPNSoggetto tmpSoggetto = new DXPNSoggetto();
	boolean continua = false;
	// get by tagname
	NodeList nodesSog = ((Element) parentNode).getElementsByTagName(tagName);
	if (nodesSog.getLength() > 0) {
	    Node localtmpnode = nodesSog.item(0);
	    if (localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
		NodeList tmpList = ((Element) localtmpnode).getElementsByTagName("TipoRuolo");
		if (tmpList.getLength() > 0) {
		    continua = true;
		    tmpSoggetto.setTipoRapporto(tmpList.item(0).getTextContent());
		}
	    }
	    if (continua) {
		// identificativi
		// PAE
		elabPAEOnSoggettoPN(localtmpnode, tmpSoggetto);

		// PAI
		elabPAIOnSoggettoPN(localtmpnode, tmpSoggetto);

		// PG
		elabPGOnSoggettoPN(localtmpnode, tmpSoggetto);

		// AS
		elabASOnSoggettoPN(localtmpnode, tmpSoggetto);

		// RUP
		elabRUPOnSoggettoPN(localtmpnode, tmpSoggetto);

		// PF
		elabPFOnSoggettoPN(localtmpnode, tmpSoggetto);
	    }
	}

	return continua ? tmpSoggetto : null;
    }

    private static void elabPAEOnSoggettoPN(final Node parentNode, DXPNSoggetto tmpSoggetto) {
	// get by tagname
	NodeList nodesPAE = ((Element) parentNode).getElementsByTagName("PAE");
	if (nodesPAE.getLength() > 0) {
	    Node localtmpnode = nodesPAE.item(0); // unico elemento presente

	    // PAE - DenominazioneAmministrazione
	    // can be null
	    NodeList tmplist = ((Element) localtmpnode)
		    .getElementsByTagName("DenominazioneAmministrazione");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.addDenominazione(tmplist.item(0).getTextContent());
	    }

	    // PAE - DenominazioneUfficio
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("DenominazioneUfficio");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.addDenominazione(tmplist.item(0).getTextContent());
	    }

	    // PAE - IndirizziDigitaliDiRiferimento
	    // can be null
	    elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);
	}

    }

    private static void elabPAIOnSoggettoPN(final Node parentNode, DXPNSoggetto tmpSoggetto) {
	// get by tagname
	NodeList nodesPAI = ((Element) parentNode).getElementsByTagName("PAI");
	if (nodesPAI.getLength() > 0) {
	    Node localtmpnode = nodesPAI.item(0); // unico elemento presente

	    // PAI - TAG_IPAAOO
	    // can be null
	    elabCodiceIPA("IPAAOO", tmpSoggetto, localtmpnode);

	    // PAI - IPAAmm
	    // can be null
	    elabCodiceIPA("IPAAmm", tmpSoggetto, localtmpnode);

	    // PAI - "IPAUOR"
	    // can be null
	    elabCodiceIPA("IPAUOR", tmpSoggetto, localtmpnode);

	    // PAI - IndirizziDigitaliDiRiferimento
	    // can be null
	    elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);

	}
    }

    private static void elabPGOnSoggettoPN(final Node parentNode, DXPNSoggetto tmpSoggetto) {
	NodeList tmplist;
	// can be null
	// get by tagname
	NodeList nodesPG = ((Element) parentNode).getElementsByTagName("PG");
	if (nodesPG.getLength() > 0) {
	    Node localtmpnode = nodesPG.item(0); // unico elemento presente

	    // PG - CodiceFiscale_PartitaIva
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("CodiceFiscale_PartitaIva");
	    if (tmplist.getLength() > 0 && StringUtils
		    .isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {
		DXPNIdentSoggetto tmpIdent = new DXPNIdentSoggetto();
		tmpIdent.setTipo(tmplist.item(0).getNodeName());
		tmpIdent.setCodice(tmplist.item(0).getTextContent());
		tmpSoggetto.addIdentificativo(tmpIdent);
	    }

	    // PG - DenominazioneOrganizzazione
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("DenominazioneOrganizzazione");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.addDenominazione((tmplist.item(0).getTextContent()));
	    }

	    // PG - DenominazioneUfficio
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("DenominazioneUfficio");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.addDenominazione((tmplist.item(0).getTextContent()));
	    }

	    // PG - IndirizziDigitaliDiRiferimento
	    // can be null
	    elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);

	}
    }

    private static void elabASOnSoggettoPN(final Node parentNode, DXPNSoggetto tmpSoggetto) {
	NodeList tmplist;
	// get by tagname
	NodeList nodesAS = ((Element) parentNode).getElementsByTagName("AS");
	if (nodesAS.getLength() > 0) {
	    Node localtmpnode = nodesAS.item(0); // unico elemento presente

	    // AS - CodiceFiscale
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("CodiceFiscale");
	    if (tmplist.getLength() > 0 && StringUtils
		    .isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {
		DXPNIdentSoggetto tmpIdent = new DXPNIdentSoggetto();
		tmpIdent.setTipo(tmplist.item(0).getNodeName()); // fixed
		tmpIdent.setCodice(tmplist.item(0).getTextContent());
		tmpSoggetto.addIdentificativo(tmpIdent);
	    }

	    // AS - Nome
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("Nome");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.setNome(tmplist.item(0).getTextContent());
	    }

	    // AS - Cognome
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("Cognome");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.setCognome(tmplist.item(0).getTextContent());
	    }

	    // AS - TAG_IPAAOO
	    // can be null
	    elabCodiceIPA("IPAAOO", tmpSoggetto, localtmpnode);

	    // AS - IPAAmm
	    // can be null
	    elabCodiceIPA("IPAAmm", tmpSoggetto, localtmpnode);

	    // AS - "IPAUOR"
	    // can be null
	    elabCodiceIPA("IPAUOR", tmpSoggetto, localtmpnode);

	    // AS - IndirizziDigitaliDiRiferimento
	    // can be null
	    elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);
	}
    }

    private static void elabRUPOnSoggettoPN(Node parentNode, DXPNSoggetto tmpSoggetto) {
	NodeList tmplist;
	// get by tagname
	NodeList nodesAS = ((Element) parentNode).getElementsByTagName("RUP");
	if (nodesAS.getLength() > 0) {
	    Node localtmpnode = nodesAS.item(0); // unico elemento presente

	    // RUP - CodiceFiscale
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("CodiceFiscale");
	    if (tmplist.getLength() > 0 && StringUtils
		    .isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {
		DXPNIdentSoggetto tmpIdent = new DXPNIdentSoggetto();
		tmpIdent.setTipo(tmplist.item(0).getNodeName()); // fixed
		tmpIdent.setCodice(tmplist.item(0).getTextContent());
		tmpSoggetto.addIdentificativo(tmpIdent);
	    }

	    // RUP - Nome
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("Nome");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.setNome(tmplist.item(0).getTextContent());
	    }

	    // RUP - Cognome
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("Cognome");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.setCognome(tmplist.item(0).getTextContent());
	    }

	    // RUP - TAG_IPAAOO
	    // can be null
	    elabCodiceIPA("IPAAOO", tmpSoggetto, localtmpnode);

	    // RUP - IPAAmm
	    // can be null
	    elabCodiceIPA("IPAAmm", tmpSoggetto, localtmpnode);

	    // RUP - "IPAUOR"
	    // can be null
	    elabCodiceIPA("IPAUOR", tmpSoggetto, localtmpnode);

	    // RUP - IndirizziDigitaliDiRiferimento
	    // can be null
	    elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);
	}
    }

    private static void elabPFOnSoggettoPN(final Node parentNode, DXPNSoggetto tmpSoggetto) {
	NodeList tmplist;
	// get by tagname
	NodeList nodesPF = ((Element) parentNode).getElementsByTagName("PF");
	if (nodesPF.getLength() > 0) {
	    Node localtmpnode = nodesPF.item(0); // unico elemento presente

	    // PF - CodiceFiscale
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("CodiceFiscale");
	    if (tmplist.getLength() > 0 && StringUtils
		    .isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {
		DXPNIdentSoggetto tmpIdent = new DXPNIdentSoggetto();
		tmpIdent.setTipo(tmplist.item(0).getNodeName()); // fixed
		tmpIdent.setCodice(tmplist.item(0).getTextContent());
		tmpSoggetto.addIdentificativo(tmpIdent);
	    }

	    // PF - Nome
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("Nome");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.setNome(tmplist.item(0).getTextContent());
	    }

	    // PF - Cognome
	    // can be null
	    tmplist = ((Element) localtmpnode).getElementsByTagName("Cognome");
	    if (tmplist.getLength() > 0) {
		tmpSoggetto.setCognome(tmplist.item(0).getTextContent());
	    }

	    // PF - IndirizziDigitaliDiRiferimento
	    // can be null
	    elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);
	}
    }

    private static DXPNSoggetto recuperaAssegnazione(final String tagName, final Node parentNode,
	    int idx) throws DOMException {
	DXPNSoggetto tmpSoggetto = new DXPNSoggetto();
	boolean continua = false;
	String tmpDescEvento = null;

	// get by tagname
	NodeList nodesAss = ((Element) parentNode).getElementsByTagName(tagName);
	if (nodesAss.getLength() > 0) {
	    Node tmpnode = nodesAss.item(0); // presente un solo elemento
	    if (tmpnode != null && tmpnode.getNodeType() == Node.ELEMENT_NODE) {
		// can be null
		NodeList tmpList = ((Element) tmpnode)
			.getElementsByTagName("TipoAssegnazioneRuolo");
		if (tmpList.getLength() > 0) {
		    continua = true;
		    tmpSoggetto.setTipoRapporto(
			    "SoggettoAssegnatario - " + tmpList.item(0).getTextContent());
		    tmpDescEvento = tmpList.item(0).getTextContent();
		}

		if (continua) {
		    //
		    elabSoggettoOnAssegnazione("SoggettoAssegnatario", tmpnode, tmpSoggetto, idx);
		    // event
		    // skip if tmpDescEvento is null (mandatory field)
		    if (StringUtils.isNotBlank(tmpDescEvento)) {
			DXPNEvento event = elabEventoOnAssegnazione(tmpDescEvento, tmpnode);
			tmpSoggetto.addEvento(event);
		    }
		}
	    } // if
	}

	return continua ? tmpSoggetto : null;
    }

    private static void elabSoggettoOnAssegnazione(final String tagName, final Node parentNode,
	    DXPNSoggetto tmpSoggetto, int idx) {
	// get by tagname
	NodeList tmplist = ((Element) parentNode).getElementsByTagName(tagName);
	if (tmplist.getLength() > 0) {
	    Node localtmpnode = tmplist.item(0);
	    if (localtmpnode != null && localtmpnode.getChildNodes() != null
		    && localtmpnode.getChildNodes().getLength() != 0
		    && localtmpnode.getNodeType() == Node.ELEMENT_NODE) {

		tmplist = ((Element) localtmpnode).getElementsByTagName("Nome");
		if (tmplist.getLength() > 0) {
		    tmpSoggetto.setNome(tmplist.item(0).getTextContent());
		}

		tmplist = ((Element) localtmpnode).getElementsByTagName("Cognome");
		if (tmplist.getLength() > 0) {
		    tmpSoggetto.setCognome(tmplist.item(0).getTextContent());
		}
		//
		tmplist = ((Element) localtmpnode).getElementsByTagName("CodiceFiscale");
		if (tmplist.getLength() > 0 && StringUtils
			.isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {
		    DXPNIdentSoggetto tmpIdent = new DXPNIdentSoggetto();
		    tmpIdent.setTipo(tmplist.item(0).getNodeName());// fixed
		    tmpIdent.setCodice(tmplist.item(0).getTextContent());
		    tmpSoggetto.addIdentificativo(tmpIdent);
		}

		// IPAAOO
		// can be null
		elabCodiceIPA("IPAAOO", tmpSoggetto, localtmpnode);

		// IPAAmm
		// can be null
		elabCodiceIPA("IPAAmm", tmpSoggetto, localtmpnode);

		// IPAUOR
		// can be null
		elabCodiceIPA("IPAUOR", tmpSoggetto, localtmpnode);

		// can be null
		elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);
	    } else {
		// default
		tmpSoggetto.setDenominazioneDefault(Costanti.SOGG_DENOMINAZIONE_DEFAULT.concat(
			" - " + String.format(Costanti.UrnFormatter.PAD2DIGITS_FMT, (idx + 1))));
	    }
	} // if
    }

    private static DXPNEvento elabEventoOnAssegnazione(final String desc, final Node parentNode)
	    throws DOMException {
	NodeList tmpList;
	StringJoiner tmpStringDataInizio = new StringJoiner(" ");
	StringJoiner tmpStringDataFine = new StringJoiner(" ");
	boolean continua = true;

	// yyyy-MM-dd[[XXX][ ]['T'][HH:mm][:ss][.SSS][XXX]] -> must date / optional timezone1 +
	// hour:minutes:seconds.milli seconds + timezone2
	// e.g. 2024-05-15+02:00 00:00:00.000+02:00
	DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		.appendPattern("yyyy-MM-dd[[XXX][ ]['T'][HH:mm][:ss][.SSS][XXX]]")
		.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
		.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter();

	// init dto
	DXPNEvento tmpEvento = new DXPNEvento();
	tmpEvento.setDescrizione(desc);

	// can be null (mandatory on format)
	tmpList = ((Element) parentNode).getElementsByTagName("DataInizioAssegnazione");
	if (tmpList.getLength() > 0) {
	    tmpStringDataInizio.add(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}

	if (continua) {
	    // can be null
	    tmpList = ((Element) parentNode).getElementsByTagName("OraInizioAssegnazione");
	    if (tmpList.getLength() > 0) {
		tmpStringDataInizio.add(tmpList.item(0).getTextContent());
	    }

	    Date dataInzio = Date
		    .from(LocalDateTime.parse(tmpStringDataInizio.toString(), formatter)
			    .atZone(ZoneId.systemDefault()).toInstant());
	    tmpEvento.setDataInizio(dataInzio);
	}

	// can be null (mandatory on format)
	tmpList = ((Element) parentNode).getElementsByTagName("DataFineAssegnazione");
	if (tmpList.getLength() > 0) {
	    tmpStringDataFine.add(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}

	if (continua) {
	    // can be null
	    tmpList = ((Element) parentNode).getElementsByTagName("OraFineAssegnazione");
	    if (tmpList.getLength() > 0) {
		tmpStringDataFine.add(tmpList.item(0).getTextContent());
	    }

	    Date dataFine = Date.from(LocalDateTime.parse(tmpStringDataFine.toString(), formatter)
		    .atZone(ZoneId.systemDefault()).toInstant());
	    tmpEvento.setDataFine(dataFine);
	}

	return tmpEvento;
    }

    private static void elabCodiceIPA(final String tagName, DXPNSoggetto tmpSoggetto,
	    Node localtmpnode) {
	NodeList tmplist = ((Element) localtmpnode).getElementsByTagName(tagName);
	if (tmplist.getLength() > 0) {
	    Node tmpnode = tmplist.item(0); // unico elemento presente
	    // Denominazione
	    // can be null
	    tmplist = ((Element) tmpnode).getElementsByTagName("Denominazione");
	    boolean addDenominIfExist = StringUtils.isBlank(tmpSoggetto.getNome())
		    && StringUtils.isBlank(tmpSoggetto.getCognome());
	    if (tmplist.getLength() > 0 && addDenominIfExist) {
		tmpSoggetto.addDenominazione(tmplist.item(0).getTextContent());
	    }

	    // CodiceIPA
	    // can be null
	    tmplist = ((Element) tmpnode).getElementsByTagName("CodiceIPA");
	    if (tmplist.getLength() > 0 && StringUtils
		    .isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {

		DXPNIdentSoggetto tmpIdent = new DXPNIdentSoggetto();
		tmpIdent.setTipo(tagName);// fixed
		tmpIdent.setCodice(tmplist.item(0).getTextContent()); // unico elemento
		tmpSoggetto.addIdentificativo(tmpIdent);
	    }
	}
    }

    private static void elabIndirizziDigitaliDiRiferimento(Node localtmpnode,
	    DXPNSoggetto tmpSoggetto) {
	NodeList tmplist = ((Element) localtmpnode)
		.getElementsByTagName("IndirizziDigitaliDiRiferimento");
	if (tmplist.getLength() > 0) {
	    tmpSoggetto.addIndirizzoDigitRif(tmplist.item(0).getTextContent());
	}
    }
}
