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

import java.math.BigDecimal;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DXPAFascicoloCollegato;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DXPAVoceClassificazione;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DatiXmlProfiloArchivistico;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloArchivisticoType;

public class ProfileArchDataPrsr {

    private static final Logger log = LoggerFactory.getLogger(ProfileArchDataPrsr.class);

    private ProfileArchDataPrsr() {
	throw new IllegalStateException("Utility class");
    }

    public static DatiXmlProfiloArchivistico recuperaDatiDaXmlPA(ProfiloArchivisticoType pat) {
	final String selector = "SegnaturaArchivistica";
	DatiXmlProfiloArchivistico tmpDatiXml = new DatiXmlProfiloArchivistico();
	try {
	    String tmpString;
	    Node tmpDati = pat.getAny();
	    XPath xpath = XPathFactory.newInstance().newXPath();
	    //
	    XPathExpression expr = xpath.compile(
		    "//" + selector + "/Classificazione" + "/IndiceClassificazione/text()");
	    NodeList nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		tmpString = nodes.item(i).getNodeValue();
		tmpDatiXml.setIndiceClassificazione(tmpString);
	    }
	    //
	    Node tmpnode;
	    StringBuilder tmpSb = new StringBuilder();
	    expr = xpath.compile("//" + selector + "/Classificazione"
		    + "/DescrizioneIndiceClassificazione" + "/VoceClassificazione");
	    nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	    for (int i = 0; i < nodes.getLength(); i++) {
		tmpnode = nodes.item(i);
		if (tmpnode.getNodeType() == Node.ELEMENT_NODE) {
		    tmpSb.append(recuperaDescVoceClassif(tmpnode));
		    tmpDatiXml.addVoceClassificazione(recuperVoceClassificazione(tmpnode));
		}
	    }
	    tmpDatiXml.setDescIndiceClassificazione(tmpSb.toString());
	    //
	    expr = xpath.compile("//" + selector + "/ChiaveFascicoloDiAppartenenza");
	    tmpnode = (Node) expr.evaluate(tmpDati, XPathConstants.NODE);
	    if (tmpnode != null && tmpnode.getNodeType() == Node.ELEMENT_NODE) {
		tmpDatiXml.setChiaveFascicoloDiAppartenenza(recuperaCsChiaveFascDaNodo(tmpnode));
	    }
	    //
	    recuperaCollegamenti(tmpDatiXml, tmpDati, xpath);
	    //
	    recuperaPianoCons(tmpDatiXml, tmpDati, xpath);
	} catch (Exception ex) {
	    log.atError().log("errore recupero dati di profilo archivistico", ex);
	    return null;
	}

	return tmpDatiXml;
    }

    private static void recuperaCollegamenti(DatiXmlProfiloArchivistico tmpDatiXml, Node tmpDati,
	    XPath xpath) throws XPathExpressionException {
	final String selector = "Collegamenti";
	XPathExpression expr = xpath.compile("//" + selector + "/FascicoloCollegato");
	NodeList nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	for (int i = 0; i < nodes.getLength(); i++) {
	    // FascicoloCollegato
	    Node tmpnode = nodes.item(i);
	    if (tmpnode != null && tmpnode.getNodeType() == Node.ELEMENT_NODE) {
		tmpDatiXml.addFascCollegato(recuperaFascCollegato(tmpnode));
	    }
	}
    }

    private static void recuperaPianoCons(DatiXmlProfiloArchivistico tmpDatiXml, Node tmpDati,
	    XPath xpath) throws XPathExpressionException {
	final String selector = "PianoConservazione";
	String tmpString;
	//
	XPathExpression expr = xpath.compile("//" + selector + "/TempoConservazione/text()");
	NodeList nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	for (int i = 0; i < nodes.getLength(); i++) {
	    tmpString = nodes.item(i).getNodeValue();
	    if (tmpString.matches("-?\\d+")) {
		tmpDatiXml.setTempoConservazione(new BigDecimal(tmpString));
	    }
	}

	//
	expr = xpath.compile("//" + selector + "/InfoPianoConservazione/text()");
	nodes = (NodeList) expr.evaluate(tmpDati, XPathConstants.NODESET);
	for (int i = 0; i < nodes.getLength(); i++) {
	    tmpString = nodes.item(i).getNodeValue();
	    tmpDatiXml.setInfoPianoCoservazione(tmpString);
	}
    }

    private static String recuperaDescVoceClassif(final Node tmpnode) {
	NodeList tmpList;
	String retVal = "/";
	tmpList = ((Element) tmpnode).getElementsByTagName("CodiceVoce");
	if (tmpList.getLength() > 0) {
	    retVal += tmpList.item(0).getTextContent();
	}
	//
	tmpList = ((Element) tmpnode).getElementsByTagName("DescrizioneVoce");
	if (tmpList.getLength() > 0) {
	    retVal += "/" + tmpList.item(0).getTextContent();
	}
	return retVal;
    }

    private static DXPAFascicoloCollegato recuperaFascCollegato(final Node tmpnode) {
	DXPAFascicoloCollegato tmpFascCollegato = new DXPAFascicoloCollegato();
	NodeList tmpList;
	boolean continua = true;
	//
	tmpList = ((Element) tmpnode).getElementsByTagName("DescrizioneCollegamento");
	if (tmpList.getLength() > 0) {
	    tmpFascCollegato.setDescCollegamento(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}
	if (continua) {
	    tmpList = ((Element) tmpnode).getElementsByTagName("ChiaveCollegamento");
	    if (tmpList.getLength() > 0) {
		tmpFascCollegato.setCsChiaveFasc(recuperaCsChiaveFascDaNodo(tmpList.item(0)));
	    } else {
		continua = false;
	    }
	}
	return continua ? tmpFascCollegato : null;
    }

    private static DXPAVoceClassificazione recuperVoceClassificazione(final Node tmpnode) {
	DXPAVoceClassificazione tmpVoceClassificazione = new DXPAVoceClassificazione();
	NodeList tmpList;
	boolean continua = true;
	//
	tmpList = ((Element) tmpnode).getElementsByTagName("CodiceVoce");
	if (tmpList.getLength() > 0) {
	    tmpVoceClassificazione.setCodiceVoce(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}
	if (continua) {
	    tmpList = ((Element) tmpnode).getElementsByTagName("DescrizioneVoce");
	    if (tmpList.getLength() > 0) {
		tmpVoceClassificazione.setDescrizioneVoce(tmpList.item(0).getTextContent());
	    } else {
		continua = false;
	    }
	}
	return continua ? tmpVoceClassificazione : null;
    }

    private static CSChiaveFasc recuperaCsChiaveFascDaNodo(final Node tmpnode) {
	CSChiaveFasc tmpChiave = new CSChiaveFasc();
	NodeList tmpList;
	boolean continua = true;
	//
	tmpList = ((Element) tmpnode).getElementsByTagName("Numero");
	if (tmpList.getLength() > 0) {
	    tmpChiave.setNumero(tmpList.item(0).getTextContent());
	} else {
	    continua = false;
	}
	if (continua) {
	    tmpList = ((Element) tmpnode).getElementsByTagName("Anno");
	    if (tmpList.getLength() > 0) {
		String tmpString = tmpList.item(0).getTextContent();
		if (tmpString.matches("-?\\d+")) {
		    tmpChiave.setAnno(Integer.parseInt(tmpString));
		}
	    } else {
		continua = false;
	    }
	}
	return continua ? tmpChiave : null;

    }

}
