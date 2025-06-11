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

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGEvento;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGIdentSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGProcAmmininistrativo;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DatiXmlProfiloGenerale;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloGeneraleType;

public class ProfileGenDataPrsr {

    private static final Logger log = LoggerFactory.getLogger(ProfileGenDataPrsr.class);

    private ProfileGenDataPrsr() {
	throw new IllegalStateException("Utility class");
    }

    public static DatiXmlProfiloGenerale recuperaDatiDaXmlPG(ProfiloGeneraleType pgt) {
	DatiXmlProfiloGenerale tmpDatiXml = new DatiXmlProfiloGenerale();
	final String selector = "ProfiloGeneraleFascicolo";
	try {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    //
	    Node tmpDati = pgt.getAny();
	    XPath xpath = XPathFactory.newInstance().newXPath();

	    XPathExpression expr = xpath.compile("//" + selector + "/Oggetto/text()");
	    NodeList nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		String tmpString = nodes.item(i).getNodeValue();
		tmpDatiXml.setOggettoFascicolo(tmpString);
	    }
	    //
	    expr = xpath.compile("//" + selector + "/DataApertura/text()");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		String tmpString = nodes.item(i).getNodeValue();
		tmpDatiXml.setDataApertura(dateFormat.parse(tmpString));
	    }
	    //
	    expr = xpath.compile("//" + selector + "/DataChiusura/text()");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		String tmpString = nodes.item(i).getNodeValue();
		tmpDatiXml.setDataChiusura(dateFormat.parse(tmpString));
	    }
	    //
	    expr = xpath.compile("//" + selector + "/LivelloRiservatezza/text()");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		String tmpString = nodes.item(i).getNodeValue();
		tmpDatiXml.setLvlRiservatezza(tmpString);
	    }
	    //
	    expr = xpath.compile("//" + selector + "/Note/text()");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		String tmpString = nodes.item(i).getNodeValue();
		tmpDatiXml.setNoteFascicolo(tmpString);
	    }
	    //
	    expr = xpath.compile("//" + selector + "/Soggetti" + "/Soggetto");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		Node localtmpnode = nodes.item(i);
		if (localtmpnode != null && localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
		    tmpDatiXml.addSoggetto(recuperaSoggettoPG(localtmpnode));
		}
	    }

	    //
	    expr = xpath.compile("//" + selector + "/Eventi/Evento");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		Node localtmpnode = nodes.item(i);
		if (localtmpnode != null && localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
		    tmpDatiXml.addEvento(recuperaEvento(localtmpnode));
		}
	    }

	    //
	    expr = xpath.compile("//" + selector + "/ProcedimentoAmministrativo");
	    Node tmpnode = (Node) expr.evaluate(tmpDati, XPathConstants.NODE);
	    if (tmpnode != null && tmpnode.getNodeType() == Node.ELEMENT_NODE) {
		tmpDatiXml.setProcAmm(recuperaProcAmm(tmpnode));
	    }
	} catch (Exception ex) {
	    log.atError().log("errore recupero dati di profilo generale", ex);
	    return null;
	}

	return tmpDatiXml;

    }

    private static DXPGEvento recuperaEvento(final Node tmpnode)
	    throws DOMException, ParseException {
	DXPGEvento tmpEvento = new DXPGEvento();
	NodeList tmpList;
	boolean continua = true;

	// verificare se rimuovere
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	// can be null
	tmpList = ((Element) tmpnode).getElementsByTagName("Denominazione");
	if (tmpList.getLength() > 0) {
	    tmpEvento.setDescrizione(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}

	if (continua) {
	    tmpList = ((Element) tmpnode).getElementsByTagName("DataInizio");
	    if (tmpList.getLength() > 0) {
		tmpEvento.setDataInizio(dateFormat.parse(tmpList.item(0).getTextContent()));
	    } else {
		continua = false;
	    }
	}

	// can be null
	tmpList = ((Element) tmpnode).getElementsByTagName("DataFine");
	if (tmpList.getLength() > 0) {
	    tmpEvento.setDataFine(dateFormat.parse(tmpList.item(0).getTextContent()));
	}

	return continua ? tmpEvento : null;
    }

    private static DXPGIdentSoggetto recuperaGenericIdentSogPG(final Node parentNode) {
	DXPGIdentSoggetto tmpIdent = new DXPGIdentSoggetto();
	boolean continua = true;
	// cannot be null
	NodeList tmplist = ((Element) parentNode).getElementsByTagName("TipoCodice");
	if (tmplist.getLength() > 0) {
	    tmpIdent.setTipo(tmplist.item(0).getTextContent());
	} else {
	    continua = false;
	}

	if (continua) {
	    // cannot be null
	    tmplist = ((Element) parentNode).getElementsByTagName("Codice");
	    if (tmplist.getLength() > 0 && StringUtils
		    .isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {
		tmpIdent.setCodice(tmplist.item(0).getTextContent());
	    } else {
		continua = false;
	    }
	}

	return continua ? tmpIdent : null;
    }

    private static DXPGIdentSoggetto recuperaFixedIdentSogPG(final String tagname,
	    final Node parentNode) {
	DXPGIdentSoggetto tmpIdent = null;
	//
	NodeList tmplist = ((Element) parentNode).getElementsByTagName(tagname);
	if (tmplist.getLength() > 0
		&& StringUtils.isNotBlank(tmplist.item(0).getTextContent()) /* skip if empty */) {
	    tmpIdent = new DXPGIdentSoggetto(true);
	    tmpIdent.setTipo(tmplist.item(0).getNodeName());
	    tmpIdent.setCodice(tmplist.item(0).getTextContent());
	}
	return tmpIdent;
    }

    private static DXPGProcAmmininistrativo recuperaProcAmm(final Node parentNode) {
	DXPGProcAmmininistrativo tmpProcAmmininistrativo = new DXPGProcAmmininistrativo();
	boolean continua = true;
	NodeList tmpList = ((Element) parentNode).getElementsByTagName("CodiceProcedimento");
	if (tmpList.getLength() > 0) {
	    tmpProcAmmininistrativo.setCodice(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}
	if (continua) {
	    tmpList = ((Element) parentNode).getElementsByTagName("DenominazioneProcedimento");
	    if (tmpList.getLength() > 0) {
		tmpProcAmmininistrativo.setDenominazione(tmpList.item(0).getTextContent());
	    } else {
		continua = false;
	    }
	}
	if (continua) {
	    tmpList = ((Element) parentNode).getElementsByTagName("MateriaArgomentoStruttura");
	    if (tmpList.getLength() > 0) {
		tmpProcAmmininistrativo.setMateriaArgStrut(tmpList.item(0).getTextContent());
	    } else {
		continua = false;
	    }
	}
	return continua ? tmpProcAmmininistrativo : null;
    }

    private static DXPGSoggetto recuperaSoggettoPG(final Node parentNode)
	    throws DOMException, ParseException {
	NodeList tmpList;
	boolean continua = true;
	// soggetto elaborato
	DXPGSoggetto tmpSoggetto = new DXPGSoggetto();

	// mandatory
	tmpList = ((Element) parentNode).getElementsByTagName("Ruolo");
	if (tmpList.getLength() > 0) {
	    tmpSoggetto.setTipoRapporto(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}

	if (continua) {
	    // TipoSoggettoPAI
	    elabTiPaiPaePgOnSoggettoPG("TipoSoggettoPAI", parentNode, tmpSoggetto);

	    // TipoSoggettoPAE
	    elabTiPaiPaePgOnSoggettoPG("TipoSoggettoPAE", parentNode, tmpSoggetto);

	    // TipoSoggettoPG
	    elabTiPaiPaePgOnSoggettoPG("TipoSoggettoPG", parentNode, tmpSoggetto);

	    // TipoSoggettoPF
	    elabTiPFOnSoggettoPG("TipoSoggettoPF", parentNode, tmpSoggetto);
	}
	//
	if (continua) {
	    // get by tagname
	    NodeList nodeEvents = ((Element) parentNode).getElementsByTagName("Eventi");
	    if (nodeEvents.getLength() > 0) {
		// Evento
		Node localNodeEvents = nodeEvents.item(0);
		if (localNodeEvents.getNodeType() == Node.ELEMENT_NODE) {
		    // Identificativo
		    tmpList = ((Element) localNodeEvents).getElementsByTagName("Evento");
		    for (int i = 0; i < tmpList.getLength(); i++) {
			Node localtmpnode = tmpList.item(i);
			if (localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
			    tmpSoggetto.addEvento(recuperaEvento(localtmpnode));
			}
		    }
		}
	    }
	}
	return continua ? tmpSoggetto : null;
    }

    private static void elabTiPFOnSoggettoPG(final String tagName, final Node parentNode,
	    DXPGSoggetto tmpSoggetto) {
	// get by tagname
	NodeList nodesSog = ((Element) parentNode).getElementsByTagName(tagName);
	if (nodesSog.getLength() > 0) {
	    Node localtmpnode = nodesSog.item(0);
	    if (localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
		NodeList tmpList = ((Element) localtmpnode).getElementsByTagName("Cognome");
		if (tmpList.getLength() > 0) {
		    tmpSoggetto.setCognome(tmpList.item(0).getTextContent());
		}
		// can be null
		tmpList = ((Element) localtmpnode).getElementsByTagName("Nome");
		if (tmpList.getLength() > 0) {
		    tmpSoggetto.setNome(tmpList.item(0).getTextContent());
		}
		// can be null
		tmpList = ((Element) localtmpnode).getElementsByTagName("Sesso");
		if (tmpList.getLength() > 0) {
		    tmpSoggetto.setSesso(tmpList.item(0).getTextContent());
		}
		// can be null
		tmpList = ((Element) localtmpnode).getElementsByTagName("DataNascita");
		if (tmpList.getLength() > 0) {
		    tmpSoggetto.setDataNascita(tmpList.item(0).getTextContent());
		}
		// can be null
		tmpList = ((Element) localtmpnode).getElementsByTagName("LuogoNascita");
		if (tmpList.getLength() > 0) {
		    tmpSoggetto.setLuogoNascita(tmpList.item(0).getTextContent());
		}
		// can be null
		tmpList = ((Element) localtmpnode).getElementsByTagName("Cittadinanza");
		if (tmpList.getLength() > 0) {
		    tmpSoggetto.setCittadinanza(tmpList.item(0).getTextContent());
		}

		// can be null
		elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);

		// can be null
		elabIdentificativi(localtmpnode, tmpSoggetto);
	    }
	}
    }

    private static void elabTiPaiPaePgOnSoggettoPG(final String tagName, final Node parentNode,
	    DXPGSoggetto tmpSoggetto) {
	// get by tagname
	NodeList nodesSog = ((Element) parentNode).getElementsByTagName(tagName);
	if (nodesSog.getLength() > 0) {
	    Node localtmpnode = nodesSog.item(0); // unico elemento presente
	    if (localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
		NodeList tmplist = ((Element) localtmpnode).getElementsByTagName("Denominazione");
		if (tmplist.getLength() > 0) {
		    tmpSoggetto.addDenominazione(tmplist.item(0).getTextContent());
		}

		// can be null
		elabIndirizziDigitaliDiRiferimento(localtmpnode, tmpSoggetto);

		// can be null
		elabIdentificativi(localtmpnode, tmpSoggetto);
	    }
	}

    }

    private static void elabIndirizziDigitaliDiRiferimento(Node localNodeSog,
	    DXPGSoggetto tmpSoggetto) {
	NodeList nodesIndirRif = ((Element) localNodeSog)
		.getElementsByTagName("IndirizziDigitaliDiRiferimento");
	if (nodesIndirRif.getLength() > 0) {
	    Node localNodeIndirRif = nodesIndirRif.item(0);
	    if (localNodeIndirRif.getNodeType() == Node.ELEMENT_NODE) {
		// IndirizzoDigitaleDiRiferimento
		NodeList tmpList = ((Element) localNodeIndirRif)
			.getElementsByTagName("IndirizzoDigitaleDiRiferimento");
		for (int i = 0; i < tmpList.getLength(); i++) {
		    Node localtmpnode = tmpList.item(i);
		    if (localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
			tmpSoggetto.addIndirizzoDigitRif(tmpList.item(0).getTextContent());
		    }
		}
	    }
	}
    }

    private static void elabIdentificativi(Node localNodeSog, DXPGSoggetto tmpSoggetto) {
	NodeList nodesIdent = ((Element) localNodeSog).getElementsByTagName("Identificativi");
	if (nodesIdent.getLength() > 0) {
	    // Identificativo
	    Node localNodeIdent = nodesIdent.item(0);
	    if (localNodeIdent.getNodeType() == Node.ELEMENT_NODE) {
		// Identificativo
		NodeList tmpList = ((Element) localNodeIdent)
			.getElementsByTagName("Identificativo");
		for (int i = 0; i < tmpList.getLength(); i++) {
		    Node localtmpnode = tmpList.item(i);
		    if (localtmpnode.getNodeType() == Node.ELEMENT_NODE) {
			tmpSoggetto.addIdentificativo(recuperaGenericIdentSogPG(localtmpnode));
			tmpSoggetto
				.addIdentificativo(recuperaFixedIdentSogPG("IPAAmm", localtmpnode));
			tmpSoggetto
				.addIdentificativo(recuperaFixedIdentSogPG("IPAAOO", localtmpnode));
			tmpSoggetto
				.addIdentificativo(recuperaFixedIdentSogPG("IPAUOR", localtmpnode));
		    }
		}
	    }
	}
    }

}
