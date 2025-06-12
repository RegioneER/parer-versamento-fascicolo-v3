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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import it.eng.parer.fascicolo.beans.IControlliProfiliFascicoloService;
import it.eng.parer.fascicolo.beans.IControlliSemanticiService;
import it.eng.parer.fascicolo.beans.XmlFascCache;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.DatoSpecifico;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlliAttSpec;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DatiXmlProfiloArchivistico;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGEvento;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DatiXmlProfiloGenerale;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DXPNSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DatiXmlProfiloNormativo;
import it.eng.parer.fascicolo.beans.utils.Costanti;
import it.eng.parer.fascicolo.beans.utils.CostantiDB;
import it.eng.parer.fascicolo.beans.utils.CostantiDB.TipiUsoDatiSpec;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.profile.ProfileArchDataPrsr;
import it.eng.parer.fascicolo.beans.utils.profile.ProfileGenDataPrsr;
import it.eng.parer.fascicolo.beans.utils.profile.ProfileNormDataPrsr;
import it.eng.parer.fascicolo.beans.utils.xml.XmlDateUtility;
import it.eng.parer.fascicolo.beans.utils.xml.XmlValidationEventHandler;
import it.eng.parer.fascicolo.jpa.entity.DecAttribFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecModelloXsdFascicolo;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloSpecificoType;
import it.eng.parer.ws.xml.versfascicoloV3.TipoConservazioneType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECFascicoloType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.ValidationEvent;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class ControlliProfiliFascicoloService implements IControlliProfiliFascicoloService {

    @Inject
    EntityManager entityManager;

    @Inject
    IControlliSemanticiService controlliSemanticiService;

    @Inject
    XmlFascCache xmlFascCache;

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean verificaProfiloArchivistico(VersFascicoloExt versamento,
	    ECFascicoloType fascicoloResp, BlockingFakeSession syncFakeSession) {
	//
	boolean result = false;

	IndiceSIPFascicolo parsedIndiceFasc = syncFakeSession.getIndiceSIPFascicolo();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();

	try {
	    // verifico se esiste almeno un modello di tipo fascicolo utile
	    if (checkXsdProfileExistence(svf,
		    CostantiDB.TiModelloXsd.PROFILO_ARCHIVISTICO_FASCICOLO.name())) {
		// esiste un modello XSD, devo vedere se è dichiarato nell'XML e se va bene
		if (parsedIndiceFasc.getProfiloArchivistico() == null) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_ARCH_001_004, svf.getUrnPartChiaveFascicolo(),
			    svf.getTipoFascicoloNonverificato());
		    return false;
		}
		//
		String versione = parsedIndiceFasc.getProfiloArchivistico().getVersione();
		// la versione è stata indicata
		List<DecModelloXsdFascicolo> dmxfs = getXsdProfileByVersion(svf, versione,
			CostantiDB.TiModelloXsd.PROFILO_ARCHIVISTICO_FASCICOLO.name());
		if (dmxfs.isEmpty()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_ARCH_001_002, svf.getUrnPartChiaveFascicolo(),
			    versione);
		    return false;
		}

		// ho recuperato l'XSD. salvo il riferimento per un futuro utilizzo
		svf.setIdRecXsdProfiloArchivistico(dmxfs.get(0).getIdModelloXsdFascicolo());
		// ho finalmente l'XSD del profilo archivistico, adesso lo verifico
		String paXsd = dmxfs.get(0).getBlXsd();
		RispostaControlli rc = validateXmlProfileOnXsd(paXsd,
			parsedIndiceFasc.getProfiloArchivistico().getAny());
		if (!rc.isrBoolean()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_ARCH_002_001, svf.getUrnPartChiaveFascicolo(),
			    rc.getDsErr());
		    return false;
		}
		DatiXmlProfiloArchivistico dxpa = null;
		dxpa = ProfileArchDataPrsr
			.recuperaDatiDaXmlPA(parsedIndiceFasc.getProfiloArchivistico());
		if (dxpa == null) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_ARCH_002_001, svf.getUrnPartChiaveFascicolo(),
			    "Errore in fase di elaborazione dei metadati dal Profilo Archivistico del fascicolo");
		    return false;
		}
		// salvo i dati XML del profilo archivistico
		svf.setDatiXmlProfiloArchivistico(dxpa);
		// imposto l'esito con i dati trovati
		fascicoloResp.setTempoConservazione(dxpa.getTempoConservazione().longValue());
		// Oggi è la mia giornata fortunata... la verifica è andata bene.
		result = true;
	    } else {
		if (parsedIndiceFasc.getProfiloArchivistico() != null) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_ARCH_001_003, svf.getUrnPartChiaveFascicolo(),
			    svf.getTipoFascicoloNonverificato());
		    return false;
		}
		// il modello non c'è e non è presente nell'XSD... la verifica è andata bene.
		result = true;
	    }
	} catch (Exception e) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(), MessaggiWSBundle.ERR_666,
		    "ControlliProfiliFascicoloService.verificaProfiloArchivistico: "
			    + ExceptionUtils.getRootCauseMessage(e));
	}

	return result;
    }

    private List<DecModelloXsdFascicolo> getXsdProfileByVersion(StrutturaVersFascicolo svf,
	    String versione, String segnatura) {
	final String queryStr = "select xsd from DecModelloXsdFascicolo xsd "
		+ "join xsd.decUsoModelloXsdFascs uf " + "join uf.decAaTipoFascicolo aa "
		+ "join aa.decTipoFascicolo tf " + "where xsd.tiModelloXsd = :segnatura "
		+ "and xsd.tiUsoModelloXsd = :usoversamento " + "and xsd.cdXsd = :cdXsd "
		+ "and xsd.dtIstituz <= :dataDiOggiIn " + "and xsd.dtSoppres > :dataDiOggiIn "
		// da notare STRETTAMENTE MAGGIORE
		+ "and tf.idTipoFascicolo = :idTipoFascicolo "
		+ "and aa.aaIniTipoFascicolo <= :annoIn " + "and aa.aaFinTipoFascicolo >= :annoIn ";
	Query query = entityManager.createQuery(queryStr, DecModelloXsdFascicolo.class);
	query.setParameter("cdXsd", versione);
	query.setParameter("idTipoFascicolo", Long.valueOf(svf.getIdTipoFascicolo()));
	query.setParameter("annoIn", new BigDecimal(svf.getChiaveNonVerificata().getAnno()));
	query.setParameter("segnatura", segnatura);
	query.setParameter("usoversamento", CostantiDB.TiUsoModelloXsd.VERS.name());// fixed???
	query.setParameter("dataDiOggiIn", LocalDateTime.now());
	//
	return query.getResultList();
    }

    private boolean checkXsdProfileExistence(StrutturaVersFascicolo svf, String segnatura) {
	long conta = 0;
	final String queryStr = "select count(uso) from DecUsoModelloXsdFasc uso "
		+ "join uso.decModelloXsdFascicolo mod " + "join uso.decAaTipoFascicolo aa "
		+ "join aa.decTipoFascicolo tf " + "where tf.idTipoFascicolo = :idTipoFascicolo "
		+ "and aa.aaIniTipoFascicolo <= :annoIn " + "and aa.aaFinTipoFascicolo >= :annoIn "
		+ "and mod.tiModelloXsd = :segnatura " + "and mod.tiUsoModelloXsd = :usoversamento "
		+ "and mod.dtIstituz <= :dataDiOggiIn " + "and mod.dtSoppres > :dataDiOggiIn "; // da
												// notare
												// STRETTAMENTE
												// MAGGIORE
	Query query = entityManager.createQuery(queryStr);
	query.setParameter("idTipoFascicolo", Long.valueOf(svf.getIdTipoFascicolo()));
	query.setParameter("annoIn", new BigDecimal(svf.getChiaveNonVerificata().getAnno()));
	query.setParameter("segnatura", segnatura);
	query.setParameter("usoversamento", CostantiDB.TiUsoModelloXsd.VERS.name());
	query.setParameter("dataDiOggiIn", LocalDateTime.now());
	conta = (Long) query.getSingleResult();
	return conta > 0;
    }

    private RispostaControlliAttSpec checkDatiSpecifici(TipiUsoDatiSpec tipoUsoAttr,
	    long idAaTipoFasc, long idModelloXsdFascicolo) {
	RispostaControlliAttSpec rispostaControlli;
	rispostaControlli = new RispostaControlliAttSpec();
	rispostaControlli.setrBoolean(false);
	rispostaControlli.setDatiSpecifici(new LinkedHashMap<>());
	DatoSpecifico tmpDatoSpecifico;
	List<DecAttribFascicolo> lstAttribDatiSpecs = null;

	try {
	    final String queryStr = "select attrfas from DecAttribFascicolo attrfas "
		    + "join attrfas.decModelloXsdAttribFascicolos attxsd "
		    + "where attrfas.decAaTipoFascicolo.idAaTipoFascicolo = :idAaTipoFascicolo "
		    + " and attrfas.tiUsoAttrib = :tiUsoAttribIn "
		    + " and attxsd.decModelloXsdFascicolo.idModelloXsdFascicolo = :idModelloXsdFascicolo ";
	    Query query = entityManager.createQuery(queryStr, DecAttribFascicolo.class);
	    query.setParameter("idAaTipoFascicolo", idAaTipoFasc);
	    query.setParameter("tiUsoAttribIn", tipoUsoAttr.name());
	    query.setParameter("idModelloXsdFascicolo", idModelloXsdFascicolo);
	    lstAttribDatiSpecs = query.getResultList();

	    rispostaControlli.setrBoolean(!lstAttribDatiSpecs.isEmpty());
	    for (DecAttribFascicolo td : lstAttribDatiSpecs) {
		tmpDatoSpecifico = new DatoSpecifico();
		tmpDatoSpecifico.setChiave(td.getNmAttribFascicolo().trim());
		// uso trim() per gestire il caso in cui in tabella ci siano degli spazi
		// all'inizio o alla fine del nome (va confrontato con un tag XML, che
		// per definizione non ha spazi)
		tmpDatoSpecifico.setIdDatoSpec(td.getIdAttribFascicolo());
		rispostaControlli.getDatiSpecifici().put(td.getNmAttribFascicolo().trim(),
			tmpDatoSpecifico);
	    }

	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliSemanticiService.checkDatiSpecifici: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean verificaProfiloGenerale(VersFascicoloExt versamento,
	    ECFascicoloType fascicoloResp, BlockingFakeSession syncFakeSession) {
	//
	boolean result = false;

	IndiceSIPFascicolo parsedIndiceFasc = syncFakeSession.getIndiceSIPFascicolo();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();

	try {
	    String versione = parsedIndiceFasc.getProfiloGenerale().getVersione();
	    // la versione è stata indicata
	    List<DecModelloXsdFascicolo> dmxfs = getXsdProfileByVersion(svf, versione,
		    CostantiDB.TiModelloXsd.PROFILO_GENERALE_FASCICOLO.name());
	    if (dmxfs.isEmpty()) {
		versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			MessaggiWSBundle.FAS_PF_GEN_001_002, svf.getUrnPartChiaveFascicolo(),
			versione);
		return false;
	    }

	    // ho recuperato l'XSD. salvo il riferimento per un futuro utilizzo
	    svf.setIdRecxsdProfiloGenerale(dmxfs.get(0).getIdModelloXsdFascicolo());
	    // ho finalmente l'XSD del profilo generale, adesso lo verifico
	    String pgXsd = dmxfs.get(0).getBlXsd();
	    RispostaControlli rc = validateXmlProfileOnXsd(pgXsd,
		    parsedIndiceFasc.getProfiloGenerale().getAny());
	    if (!rc.isrBoolean()) {
		versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			MessaggiWSBundle.FAS_PF_GEN_002_001, svf.getUrnPartChiaveFascicolo(),
			rc.getDsErr());
		return false;
	    }
	    //
	    DatiXmlProfiloGenerale dxpg = null;
	    dxpg = ProfileGenDataPrsr.recuperaDatiDaXmlPG(parsedIndiceFasc.getProfiloGenerale());
	    if (dxpg == null) {
		versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			MessaggiWSBundle.FAS_PF_GEN_002_001, svf.getUrnPartChiaveFascicolo(),
			"Errore in fase di elaborazione dei metadati dal Profilo Generale del fascicolo");
		return false;
	    }
	    // salvo i dati XML del profilo generale
	    svf.setDatiXmlProfiloGenerale(dxpg);
	    // imposto l'esito con i dati trovati
	    fascicoloResp.setDataApertura(
		    XmlDateUtility.dateToXMLGregorianCalendarOrNull(dxpg.getDataApertura()));
	    fascicoloResp.setDataChiusura(
		    XmlDateUtility.dateToXMLGregorianCalendarOrNull(dxpg.getDataChiusura()));

	    // se ci sono errori sulle date proseguo, ma so di aver fallito il versamento.
	    // questo metodo scrive autonomamente gli errori nella lista errori
	    boolean resDateProfiloGen = verificaDateProfiloGen(versamento, dxpg, syncFakeSession);
	    // se ci sono errori sui soggetti proseguo, ma so di aver fallito il versamento.
	    // questo metodo scrive autonomamente gli errori nella lista errori
	    boolean resSoggettiProfiloGen = verificaSoggettiProfiloGen(versamento, dxpg);

	    if (resDateProfiloGen && resSoggettiProfiloGen) {
		// Oggi è la mia giornata fortunata... la verifica è andata bene.
		result = true;
	    }
	} catch (Exception e) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(), MessaggiWSBundle.ERR_666,
		    "ControlliProfiliFascicoloService.verificaProfiloGenerale: "
			    + ExceptionUtils.getRootCauseMessage(e));
	}

	return result;
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean verificaProfiloSpecifico(VersFascicoloExt versamento,
	    BlockingFakeSession syncFakeSession) {
	//
	boolean result = false;

	IndiceSIPFascicolo parsedIndiceFasc = syncFakeSession.getIndiceSIPFascicolo();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();

	try {
	    // verifico se esiste almeno un modello di tipo fascicolo utile
	    if (checkXsdProfileExistence(svf,
		    CostantiDB.TiModelloXsd.PROFILO_SPECIFICO_FASCICOLO.name())) {
		// esiste un modello XSD, devo vedere se è dichiarato nell'XML e se va bene
		if (parsedIndiceFasc.getProfiloSpecifico() == null
			|| parsedIndiceFasc.getProfiloSpecifico().isNil()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_SPEC_001_001, svf.getUrnPartChiaveFascicolo(),
			    svf.getTipoFascicoloNonverificato());
		    return false;
		}
		//
		String versione = parsedIndiceFasc.getProfiloSpecifico().getValue().getVersione();
		// la versione è stata indicata
		List<DecModelloXsdFascicolo> dmxfs = getXsdProfileByVersion(svf, versione,
			CostantiDB.TiModelloXsd.PROFILO_SPECIFICO_FASCICOLO.name());
		if (dmxfs.isEmpty()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_SPEC_001_002, svf.getUrnPartChiaveFascicolo(),
			    versione);
		    return false;
		}
		// nota: questa logica (una variabile bool che mi indica se proseguire o meno)
		// in questo caso non serve a nulla perché questo metodo esce al primo errore
		// incontrato. Se in futuro sarà posibile rendere una lista di errori come
		// la verifica del profilo Generale, sarà già pronta.
		DecModelloXsdFascicolo decModelloXsdFascicolo = dmxfs.get(0); // un solo risultato
									      // (vincolo univocità)
		// uso modello
		svf.setIdRecXsdUsoProfiloSpec(decModelloXsdFascicolo.getDecUsoModelloXsdFascs()
			.get(0).getIdUsoModelloXsdFasc());
		// ho recuperato l'XSD. salvo il riferimento per un futuro utilizzo
		svf.setIdRecXsdDatiSpec(decModelloXsdFascicolo.getIdModelloXsdFascicolo());
		// ho finalmente l'XSD del profilo specifico, adesso lo verifico
		String psXsd = dmxfs.get(0).getBlXsd();
		RispostaControlli rc = validateProfileSpecOnXsd(psXsd,
			parsedIndiceFasc.getProfiloSpecifico());
		if (!rc.isrBoolean()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_SPEC_002_001, svf.getUrnPartChiaveFascicolo(),
			    rc.getDsErr());
		    return false;
		}
		//
		RispostaControlliAttSpec rcAttSpec = recuperaDatiDaXmlPSpec(svf,
			parsedIndiceFasc.getProfiloSpecifico().getValue());
		if (StringUtils.isNotBlank(rcAttSpec.getCodErr())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    rcAttSpec.getCodErr(), rcAttSpec.getDsErr());
		    return false;
		}
		// salvo i dati specifici
		svf.setDatiSpecifici(rcAttSpec.getDatiSpecifici());
		// Oggi è la mia giornata fortunata... la verifica è andata bene.
		result = true;
	    } else {
		if (parsedIndiceFasc.getProfiloSpecifico() != null) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_SPEC_001_004, svf.getUrnPartChiaveFascicolo(),
			    svf.getTipoFascicoloNonverificato());
		    return false;
		}
		// il modello non c'è e non è presente nell'XSD... la verifica è andata bene.
		result = true;
	    }
	} catch (Exception e) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(), MessaggiWSBundle.ERR_666,
		    "ControlliProfiliFascicoloService.verificaProfiloSpecifico: "
			    + ExceptionUtils.getRootCauseMessage(e));
	}

	return result;
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public boolean verificaProfiloNormativo(VersFascicoloExt versamento,
	    BlockingFakeSession syncFakeSession) {
	//
	boolean result = false;

	IndiceSIPFascicolo parsedIndiceFasc = syncFakeSession.getIndiceSIPFascicolo();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();

	try {
	    // verifico se esiste almeno un modello di tipo fascicolo utile
	    if (checkXsdProfileExistence(svf,
		    CostantiDB.TiModelloXsd.PROFILO_NORMATIVO_FASCICOLO.name())) {
		// esiste un modello XSD, devo vedere se è dichiarato nell'XML e se va bene
		if (parsedIndiceFasc.getProfiloNormativo() == null
			|| parsedIndiceFasc.getProfiloNormativo().isNil()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_001_001, svf.getUrnPartChiaveFascicolo(),
			    svf.getTipoFascicoloNonverificato());
		    return false;
		}
		//
		String versione = parsedIndiceFasc.getProfiloNormativo().getValue().getVersione();
		// la versione è stata indicata
		List<DecModelloXsdFascicolo> dmxfs = getXsdProfileByVersion(svf, versione,
			CostantiDB.TiModelloXsd.PROFILO_NORMATIVO_FASCICOLO.name());
		if (dmxfs.isEmpty()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_001_002, svf.getUrnPartChiaveFascicolo(),
			    versione);
		    return false;
		}
		// ho recuperato l'XSD. salvo il riferimento per un futuro utilizzo
		svf.setIdRecXsdProfiloNormativo(dmxfs.get(0).getIdModelloXsdFascicolo());
		// ho finalmente l'XSD del profilo specifico, adesso lo verifico
		String paXsd = dmxfs.get(0).getBlXsd();
		RispostaControlli rc = validateXmlProfileOnXsd(paXsd,
			parsedIndiceFasc.getProfiloNormativo().getValue().getAny());
		if (!rc.isrBoolean()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_002_001, svf.getUrnPartChiaveFascicolo(),
			    rc.getDsErr());
		    return false;
		}
		DatiXmlProfiloNormativo dxpn = null;
		dxpn = ProfileNormDataPrsr
			.recuperaDatiDaXmlPN(parsedIndiceFasc.getProfiloNormativo().getValue());
		if (dxpn == null) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_002_001, svf.getUrnPartChiaveFascicolo(),
			    "Errore in fase di elaborazione dei metadati dal Profilo Normativo del fascicolo");
		    return false;
		}
		// salvo i dati XML del profilo archivistico
		svf.setDatiXmlProfiloNormativo(dxpn);

		// se ci sono errori sui soggetti proseguo, ma so di aver fallito il versamento.
		// questo metodo scrive autonomamente gli errori nella lista errori
		boolean resSoggettiProfiloNorm = verificaSoggettiProfiloNorm(versamento, dxpn);
		// se ci sono errori sull'aggregazione proseguo, ma so di aver fallito il
		// versamento
		// questo metodo scrive autonomamente gli errori nella lista errori
		boolean resAggregProfiloNorm = verificaAggregProfiloNorm(versamento, dxpn);

		if (resSoggettiProfiloNorm && resAggregProfiloNorm) {
		    // Oggi è la mia giornata fortunata... la verifica è andata bene.
		    result = true;
		}
	    } else {
		if (parsedIndiceFasc.getProfiloNormativo() != null) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_001_004, svf.getUrnPartChiaveFascicolo(),
			    svf.getTipoFascicoloNonverificato());
		    return false;
		}
	    }
	} catch (Exception e) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(), MessaggiWSBundle.ERR_666,
		    "ControlliProfiliFascicoloService.verificaProfiloNormativo: "
			    + ExceptionUtils.getRootCauseMessage(e));
	}

	return result;
    }

    private boolean verificaSoggettiProfiloNorm(VersFascicoloExt versamento,
	    DatiXmlProfiloNormativo dxpn) {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	// verifica sul soggetto
	if (!dxpn.getSoggetti().isEmpty()) {
	    Map<String, String> tipoRapportoUNQ = new HashMap<>();
	    for (DXPNSoggetto sogg : dxpn.getSoggetti()) {
		// 1. individuato come - nome/cognome oppure denominazione
		// 1.1 non esistono dati indicati per il soggetto den/cogn/nomne
		if (StringUtils.isBlank(sogg.getNome()) && StringUtils.isBlank(sogg.getCognome())
			&& StringUtils.isBlank(sogg.getDenominazione())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_003_001, svf.getUrnPartChiaveFascicolo(),
			    sogg.toString().replaceFirst(Costanti.TO_STRING_FIELD_SEPARATOR + "$",
				    ""));
		    return false;
		}
		// 1.2 se presente nome anche cognome (o viceversa)
		if (StringUtils.isNotBlank(sogg.getCognome()) && StringUtils.isBlank(sogg.getNome())
			|| StringUtils.isNotBlank(sogg.getNome())
				&& StringUtils.isBlank(sogg.getCognome())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_003_002, svf.getUrnPartChiaveFascicolo(),
			    sogg.toString().replaceFirst(Costanti.TO_STRING_FIELD_SEPARATOR + "$",
				    ""));
		    return false;
		}
		// 1.3 tutte presenti
		if (StringUtils.isNotBlank(sogg.getNome())
			&& StringUtils.isNotBlank(sogg.getCognome())
			&& StringUtils.isNotBlank(sogg.getDenominazione())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_003_003, svf.getUrnPartChiaveFascicolo(),
			    sogg.toString().replaceFirst(Costanti.TO_STRING_FIELD_SEPARATOR + "$",
				    ""));
		    return false;
		}
		// 2. per ogni soggetto (individuato come ...) tipo rapporto non ripetibile
		String key = StringUtils.isNotBlank(sogg.getDenominazione())
			? sogg.getDenominazione()
			: sogg.getCognome() + "_" + sogg.getNome();
		String tipoRapporto = tipoRapportoUNQ.get(key);
		if (tipoRapporto == null) {
		    tipoRapportoUNQ.put(key, sogg.getTipoRapporto());
		} else if (tipoRapporto.equalsIgnoreCase(sogg.getTipoRapporto())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_003_004, svf.getUrnPartChiaveFascicolo(),
			    StringUtils.isNotBlank(sogg.getDenominazione())
				    ? sogg.getDenominazione()
				    : sogg.getCognome() + " " + sogg.getNome(),
			    sogg.getTipoRapporto());
		    return false;
		}
		// 3. per ogni soggetto gli identificativi IPA non sono "ripetibili"
		boolean codIPAMoreThanOne = sogg.getIdentificativi().stream()
			.filter(i -> i.getTipo().equalsIgnoreCase("IPA")).count() > 1;
		if (codIPAMoreThanOne) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_NORM_003_005, svf.getUrnPartChiaveFascicolo(),
			    StringUtils.isNotBlank(sogg.getDenominazione())
				    ? sogg.getDenominazione()
				    : sogg.getCognome() + " " + sogg.getNome());
		    return false;
		}
	    }
	}

	return true;
    }

    private boolean verificaAggregProfiloNorm(VersFascicoloExt versamento,
	    DatiXmlProfiloNormativo dxpn) {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();

	if (StringUtils.isNotBlank(dxpn.getTipoAggreg())
		&& !dxpn.getTipoAggreg().equalsIgnoreCase(Costanti.PNORM_TIPO_AGGREG_FASCICOLO)) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
		    MessaggiWSBundle.FAS_PF_NORM_003_006, svf.getUrnPartChiaveFascicolo(),
		    dxpn.getTipoAggreg(), Costanti.PNORM_TIPO_AGGREG_FASCICOLO);
	    return false;
	}

	return true;
    }

    private boolean verificaDateProfiloGen(VersFascicoloExt versamento, DatiXmlProfiloGenerale dxpg,
	    BlockingFakeSession syncFakeSession) {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	// 1. date apertura / chiusura
	if (dxpg.getDataApertura() != null && dxpg.getDataChiusura() != null
		&& dxpg.getDataApertura().after(dxpg.getDataChiusura())) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
		    MessaggiWSBundle.FAS_PF_GEN_003_001, svf.getUrnPartChiaveFascicolo());
	    return false;
	}
	// 2. tipo conservazione
	if (syncFakeSession.getIndiceSIPFascicolo().getParametri()
		.getTipoConservazione() == TipoConservazioneType.IN_ARCHIVIO
		&& dxpg.getDataChiusura() == null) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
		    MessaggiWSBundle.FAS_PF_GEN_003_002, svf.getUrnPartChiaveFascicolo());
	    return false;
	}
	// 3. events
	Optional<DXPGEvento> event = dxpg
		.getEventi().stream().filter(e -> e.getDataInizio() != null
			&& e.getDataFine() != null && e.getDataInizio().after(e.getDataFine()))
		.findAny();
	if (event.isPresent()) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
		    MessaggiWSBundle.FAS_PF_GEN_003_012, svf.getUrnPartChiaveFascicolo(),
		    event.get().getDescrizione());
	    return false;
	}

	return true;

    }

    private boolean verificaSoggettiProfiloGen(VersFascicoloExt versamento,
	    DatiXmlProfiloGenerale dxpg) {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	// verifica sul soggetto
	if (!dxpg.getSoggetti().isEmpty()) {
	    Map<String, String> tipoRapportoUNQ = new HashMap<>();
	    for (DXPGSoggetto sogg : dxpg.getSoggetti()) {
		// 1. individuato come - nome/cognome oppure denominazione
		// 1.1 non esistono dati indicati per il soggetto den/cogn/nomne
		if (StringUtils.isBlank(sogg.getNome()) && StringUtils.isBlank(sogg.getCognome())
			&& StringUtils.isBlank(sogg.getDenominazione())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_GEN_003_006, svf.getUrnPartChiaveFascicolo());
		    return false;
		}
		// 1.2 se presente nome anche cognome (o viceversa)
		if (StringUtils.isNotBlank(sogg.getCognome()) && StringUtils.isBlank(sogg.getNome())
			|| StringUtils.isNotBlank(sogg.getNome())
				&& StringUtils.isBlank(sogg.getCognome())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_GEN_003_007, svf.getUrnPartChiaveFascicolo());
		    return false;
		}
		// 1.3 tutte presenti
		if (StringUtils.isNotBlank(sogg.getNome())
			&& StringUtils.isNotBlank(sogg.getCognome())
			&& StringUtils.isNotBlank(sogg.getDenominazione())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_GEN_003_008, svf.getUrnPartChiaveFascicolo());
		    return false;
		}
		// 2. per ogni soggetto (individuato come ...) tipo rapporto non ripetibile
		String key = StringUtils.isNotBlank(sogg.getDenominazione())
			? sogg.getDenominazione()
			: sogg.getCognome() + "_" + sogg.getNome();
		String tipoRapporto = tipoRapportoUNQ.get(key);
		if (tipoRapporto == null) {
		    tipoRapportoUNQ.put(key, sogg.getTipoRapporto());
		} else if (tipoRapporto.equalsIgnoreCase(sogg.getTipoRapporto())) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_GEN_003_009, svf.getUrnPartChiaveFascicolo(),
			    StringUtils.isNotBlank(sogg.getDenominazione())
				    ? sogg.getDenominazione()
				    : sogg.getCognome() + " " + sogg.getNome(),
			    sogg.getTipoRapporto());
		    return false;
		}
		// 3. per ogni soggetto gli identificativi il "limite" consentito di codici di
		// tipo "IPA" è massimo 3,
		// superati i quali significa
		// che per lo stesso soggetto si sta cercando di inserire un ulteriore terna non
		// consentita
		// (IPAAmm/IPAAOO/IPAUor)
		boolean codIPAMoreThanOne = sogg.getIdentificativi().stream()
			.filter(i -> i.isPredefined() && i.getTipo().startsWith("IPA")).count() > 3;
		if (codIPAMoreThanOne) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_GEN_003_011, svf.getUrnPartChiaveFascicolo(),
			    StringUtils.isNotBlank(sogg.getDenominazione())
				    ? sogg.getDenominazione()
				    : sogg.getCognome() + " " + sogg.getNome());
		    return false;
		}

		// 4. events
		Optional<DXPGEvento> event = sogg.getEventi().stream()
			.filter(e -> e.getDataInizio() != null && e.getDataFine() != null
				&& e.getDataInizio().after(e.getDataFine()))
			.findAny();
		if (event.isPresent()) {
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FAS_PF_GEN_003_012, svf.getUrnPartChiaveFascicolo(),
			    event.get().getDescrizione() + " su "
				    + (StringUtils.isNotBlank(sogg.getDenominazione())
					    ? sogg.getDenominazione()
					    : sogg.getCognome() + " " + sogg.getNome()));
		    return false;
		}
	    }
	}

	return true;
    }

    private RispostaControlli validateXmlProfileOnXsd(String xsd, Node xml) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	try {
	    String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
	    SchemaFactory factory = SchemaFactory.newInstance(language);
	    // to be compliant, completely disable DOCTYPE declaration:
	    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	    // or prohibit the use of all protocols by external entities:
	    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
	    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
	    Schema schema = factory.newSchema(new StreamSource(new StringReader(xsd)));
	    Validator validator = schema.newValidator();
	    validator.validate(new DOMSource(xml));
	    rispostaControlli.setrBoolean(true);
	} catch (IOException | SAXException e) {
	    rispostaControlli.setDsErr(e.getLocalizedMessage());
	}

	return rispostaControlli;
    }

    private RispostaControlli validateProfileSpecOnXsd(String xsd,
	    JAXBElement<ProfiloSpecificoType> profile) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);
	// xml handler
	XmlValidationEventHandler handler = new XmlValidationEventHandler();
	// compilazione schema
	// 1. Lookup a factory for the W3C XML Schema language
	SchemaFactory tmpSchemaFactoryValidazSpec = SchemaFactory
		.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	Schema tmpSchemaValidazSpec = null;
	// anche in questo caso l'eccezione non deve mai verificarsi, a meno di non aver
	// caricato
	// nel database un xsd danneggiato...
	try {
	    // 2. Compile the schema.
	    tmpSchemaValidazSpec = tmpSchemaFactoryValidazSpec
		    .newSchema(new StreamSource(new StringReader(xsd)));
	    // 3. Marshall and validate
	    Marshaller m = xmlFascCache.getVersReqFascicoloCtxProfiloSpec().createMarshaller();
	    m.setSchema(tmpSchemaValidazSpec);
	    m.setEventHandler(handler);
	    m.marshal(profile, new StringWriter());
	    rispostaControlli.setrBoolean(true);
	} catch (JAXBException e) {
	    ValidationEvent events = handler.getFirstErrorValidationEvent();
	    rispostaControlli.setDsErr(events.getMessage());
	} catch (SAXException e) {
	    rispostaControlli.setDsErr(e.getMessage());
	}
	return rispostaControlli;
    }

    private RispostaControlliAttSpec recuperaDatiDaXmlPSpec(StrutturaVersFascicolo svf,
	    ProfiloSpecificoType datiSpecifici) {
	// leggi la tabella dei dati spec attesi
	RispostaControlliAttSpec rispostaControlliAttSpec = checkDatiSpecifici(TipiUsoDatiSpec.VERS,
		svf.getIdAATipoFasc(), svf.getIdRecXsdDatiSpec());
	if (rispostaControlliAttSpec.getCodErr() != null) { // 666
	    return rispostaControlliAttSpec;
	}

	// impostazione dell'Id dell'Xsd
	rispostaControlliAttSpec.setIdRecXsdDatiSpec(svf.getIdRecXsdDatiSpec());

	for (Element element : datiSpecifici.getAny()) {
	    String tmpChiave = element.getLocalName();

	    DatoSpecifico tmpAttSpecAtteso = rispostaControlliAttSpec.getDatiSpecifici()
		    .get(tmpChiave);
	    if (tmpAttSpecAtteso != null) {
		if (tmpAttSpecAtteso.getValore() == null) {

		    if (element.getFirstChild() != null) {
			String tmpValore = element.getFirstChild().getNodeValue();
			if (tmpValore.length() <= Costanti.MAXLEN_DATOSPEC) {
			    tmpAttSpecAtteso.setValore(tmpValore);
			} else {
			    // "Errore nei dati specifici: uno o più valori superano
			    // la lunghezza di " + MAXLEN_DATOSPEC + " caratteri"
			    rispostaControlliAttSpec
				    .setCodErr(MessaggiWSBundle.FAS_PF_SPEC_002_001);
			    rispostaControlliAttSpec.setDsErr(MessaggiWSBundle.getString(
				    MessaggiWSBundle.FAS_PF_SPEC_002_001,
				    svf.getUrnPartChiaveFascicolo(),
				    "Il tag " + tmpChiave
					    + " ha un valore che supera il limite consentito di "
					    + Costanti.MAXLEN_DATOSPEC + " caratteri"));
			    return rispostaControlliAttSpec;
			}
		    } else {
			tmpAttSpecAtteso.setValore(null);
		    }
		} else {
		    rispostaControlliAttSpec.setCodErr(MessaggiWSBundle.FAS_PF_SPEC_002_001);
		    rispostaControlliAttSpec.setDsErr(MessaggiWSBundle.getString(
			    MessaggiWSBundle.FAS_PF_SPEC_002_001, svf.getUrnPartChiaveFascicolo(),
			    "Il tag " + tmpChiave + " risulta duplicato"));
		    return rispostaControlliAttSpec;
		}
	    } else {
		rispostaControlliAttSpec.setCodErr(MessaggiWSBundle.ERR_666);
		rispostaControlliAttSpec
			.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
				"GestioneDatiSpec.parseDatiSpec.i dati specifici attesi "
					+ "non coincidono con l'XSD "));
		return rispostaControlliAttSpec;
	    }
	}

	return rispostaControlliAttSpec;
    }

}
