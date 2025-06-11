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

package it.eng.parer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.FlControlliFasc;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DatiXmlProfiloArchivistico;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGProcAmmininistrativo;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DatiXmlProfiloGenerale;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DatiXmlProfiloNormativo;
import it.eng.parer.fascicolo.beans.security.User;
import it.eng.parer.fascicolo.jpa.entity.DecTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.OrgStrut;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloKo;
import it.eng.parer.ws.xml.versfascicoloV3.ChiaveType;
import it.eng.parer.ws.xml.versfascicoloV3.ConfigType;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;
import it.eng.parer.ws.xml.versfascicoloV3.IntestazioneType;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoGeneraleType;

public class TestFactory {

    public VersFascicoloExtBuilder createVersFascicoloExtBuilder() {
	return new VersFascicoloExtBuilder();
    }

    public BlockingFakeSession createBlockingFakeSession() {
	final BlockingFakeSession sessione = new BlockingFakeSession();
	sessione.setTmApertura(ZonedDateTime.now());
	sessione.setTmChiusura(ZonedDateTime.now());
	sessione.setIpChiamante("127.0.0.1");
	sessione.setVersioneWS("9.9.9");
	sessione.setLoginName("junit.test");
	sessione.setIndiceSIPFascicolo(new IndiceSIPFascicolo());
	sessione.getIndiceSIPFascicolo().setParametri(new ConfigType());
	sessione.getIndiceSIPFascicolo().setIntestazione(new IntestazioneType());
	sessione.getIndiceSIPFascicolo().getIntestazione().setChiave(new ChiaveType());
	return sessione;
    }

    public FasFascicolo createFasFascicolo() {
	FasFascicolo fascicolo = new FasFascicolo();
	fascicolo.setFasXmlVersFascicolos(new ArrayList<>());
	fascicolo.setFasAipFascicoloDaElabs(new ArrayList<>());
	return fascicolo;
    }

    public RispostaWSFascicolo createRispostaWSFascicolo() {
	final RispostaWSFascicolo rispostaWSFascicolo = new RispostaWSFascicolo();
	rispostaWSFascicolo.setCompRapportoVersFascicolo(new CompRapportoVersFascicolo());
	rispostaWSFascicolo.getCompRapportoVersFascicolo().setVersioneRapportoVersamento("1.0");
	rispostaWSFascicolo.getCompRapportoVersFascicolo()
		.setEsitoGenerale(new EsitoGeneraleType());
	return rispostaWSFascicolo;
    }

    public VrsFascicoloKo createVrsFascicoloKo() {
	final VrsFascicoloKo vrsFascicoloKo = new VrsFascicoloKo();
	vrsFascicoloKo.setVrsSesFascicoloKos(new ArrayList<>());
	vrsFascicoloKo.setOrgStrut(new OrgStrut());
	vrsFascicoloKo.setDecTipoFascicolo(new DecTipoFascicolo());
	return vrsFascicoloKo;
    }

    public VrsSesFascicoloKo createVrsSesFascicoloKo() {
	final VrsSesFascicoloKo sessFascicoloKo = new VrsSesFascicoloKo();
	sessFascicoloKo.setVrsXmlSesFascicoloKos(new ArrayList<>());
	sessFascicoloKo.setVrsErrSesFascicoloKos(new ArrayList<>());
	return sessFascicoloKo;
    }

    public BackendStorage createBackendStorage() {

	return new BackendStorage() {

	    @Override
	    public BackendStorage.STORAGE_TYPE getType() {
		return BackendStorage.STORAGE_TYPE.BLOB;
	    }

	    @Override
	    public String getBackendName() {
		return "DATABASE_PRIMARIO";
	    }
	};
    }

    public Element createProfiloGenerale() {
	return xmlToElement("/factory/profiloGenerale.xml");
    }

    public Element createProfiloArchivistico() {
	return xmlToElement("/factory/profiloArchivistico.xml");
    }

    public Element createProfiloSpecifico() {
	return xmlToElement("/factory/profiloSpecifico.xml");
    }

    public Element createProfiloNormativo() {
	return xmlToElement("/factory/profiloNormativo.xml");
    }

    private Element xmlToElement(String xmlPath) {
	String xml;
	try {
	    xml = IOUtils.toString(this.getClass().getResourceAsStream(xmlPath), "UTF-8");
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setNamespaceAware(true);
	Document result;
	try {
	    result = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
	} catch (SAXException | IOException | ParserConfigurationException e) {
	    throw new RuntimeException(e);
	}
	return result.getDocumentElement();
    }

    public class VersFascicoloExtBuilder {

	private VersFascicoloExt versamento;

	public VersFascicoloExt build() {
	    return getVersamento();
	}

	public VersFascicoloExtBuilder addDatiXmlProfiloGenerale() {
	    getVersamento().getStrutturaComponenti()
		    .setDatiXmlProfiloGenerale(new DatiXmlProfiloGenerale());
	    getVersamento().getStrutturaComponenti().getDatiXmlProfiloGenerale()
		    .setProcAmm(new DXPGProcAmmininistrativo());
	    getVersamento().getStrutturaComponenti().getDatiXmlProfiloGenerale()
		    .setEventi(new ArrayList<>());
	    getVersamento().getStrutturaComponenti().getDatiXmlProfiloGenerale()
		    .setSoggetti(new ArrayList<>());
	    return this;
	}

	public VersFascicoloExtBuilder addDatiXmlProfiloArchivistico() {
	    getVersamento().getStrutturaComponenti()
		    .setDatiXmlProfiloArchivistico(new DatiXmlProfiloArchivistico());
	    return this;
	}

	public VersFascicoloExtBuilder addDatiXmlProfiloNormativo() {
	    getVersamento().getStrutturaComponenti()
		    .setDatiXmlProfiloNormativo(new DatiXmlProfiloNormativo());
	    getVersamento().getStrutturaComponenti().getDatiXmlProfiloNormativo()
		    .setSoggetti(new ArrayList<>());
	    return this;
	}

	public VersFascicoloExtBuilder addFascicoliLinked() {
	    getVersamento().getStrutturaComponenti().setFascicoliLinked(new ArrayList<>());
	    return this;
	}

	public VersFascicoloExtBuilder addUnitaDocElencate() {
	    getVersamento().getStrutturaComponenti().setUnitaDocElencate(new ArrayList<>());
	    return this;
	}

	public VersFascicoloExtBuilder addDatiSpecifici() {
	    getVersamento().getStrutturaComponenti().setDatiSpecifici(new HashMap<>());
	    return this;
	}

	// default
	private VersFascicoloExt getVersamento() {
	    if (versamento == null) {
		StrutturaVersFascicolo strutturaComponenti = new StrutturaVersFascicolo();
		strutturaComponenti.setIdStruttura(0L);
		CSChiaveFasc chiaveNonVerificata = new CSChiaveFasc();
		chiaveNonVerificata.setAnno(2020);
		chiaveNonVerificata.setNumero("999999");
		strutturaComponenti.setChiaveNonVerificata(chiaveNonVerificata);
		strutturaComponenti.setFlControlliFasc(new FlControlliFasc());
		strutturaComponenti.setVersioneIndiceSipNonVerificata("1.0");
		versamento = new VersFascicoloExt();
		versamento.setStrutturaComponenti(strutturaComponenti);
		versamento.setUtente(new User());
		versamento.setDatiXml("");
	    }
	    return versamento;
	}

    }
}
