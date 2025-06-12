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
package it.eng.parer.fascicolo.beans.dao;

import static it.eng.parer.fascicolo.beans.utils.converter.DateUtilsConverter.convert;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.Node;

import it.eng.parer.fascicolo.beans.AppServerInstance;
import it.eng.parer.fascicolo.beans.ISalvataggioFascicoliDao;
import it.eng.parer.fascicolo.beans.IlogSessioneFascicoliDao;
import it.eng.parer.fascicolo.beans.XmlFascCache;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.FascicoloLink;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.UnitaDocLink;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.DatoSpecifico;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.base.VoceDiErrore;
import it.eng.parer.fascicolo.beans.dto.profile.IEvento;
import it.eng.parer.fascicolo.beans.dto.profile.IIdentSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.ISoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DatiXmlProfiloArchivistico;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGEvento;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGProcAmmininistrativo;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DatiXmlProfiloGenerale;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.utils.Costanti;
import it.eng.parer.fascicolo.beans.utils.CostantiDB;
import it.eng.parer.fascicolo.beans.utils.CostantiDB.TipiHash;
import it.eng.parer.fascicolo.beans.utils.LogSessioneUtils;
import it.eng.parer.fascicolo.beans.utils.hash.HashCalculator;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSFormat;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSHelper;
import it.eng.parer.fascicolo.beans.utils.xml.XmlUtils;
import it.eng.parer.fascicolo.jpa.entity.AroUnitaDoc;
import it.eng.parer.fascicolo.jpa.entity.DecAaTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecAttribFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecModelloXsdFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecUsoModelloXsdFasc;
import it.eng.parer.fascicolo.jpa.entity.DecVoceTitol;
import it.eng.parer.fascicolo.jpa.entity.DecWarnAaTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasCodIdeSog;
import it.eng.parer.fascicolo.jpa.entity.FasEventoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasEventoSog;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasLinkFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasRifIndSog;
import it.eng.parer.fascicolo.jpa.entity.FasSogFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasUnitaDocFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasValoreAttribFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasWarnFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasXmlFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasXmlVersFascicolo;
import it.eng.parer.fascicolo.jpa.entity.IamUser;
import it.eng.parer.fascicolo.jpa.entity.OrgStrut;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasCodIdeSog.TiCodSog;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasXmlFascicolo.TiModXsdFasXmlFascicolo;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloSpecificoType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

@SuppressWarnings({
	"rawtypes", "unchecked" })
@ApplicationScoped
public class SalvataggioFascicoliDao implements ISalvataggioFascicoliDao {

    //
    private static final String LOG_FASCICOLO_ERR = "Errore interno nella fase di salvataggio fascicolo: ";

    @Inject
    MessaggiWSHelper messaggiWSHelper;

    @Inject
    XmlFascCache xmlFascCache;

    @Inject
    AppServerInstance appServerInstance;

    @Inject
    IlogSessioneFascicoliDao logSessioneFascicoliDao;

    @Inject
    EntityManager entityManager;

    @Override
    public RispostaControlli scriviFascicolo(VersFascicoloExt versamento,
	    BlockingFakeSession sessione, VrsFascicoloKo fascicoloKo)
	    throws AppGenericPersistenceException {
	RispostaControlli tmpRispostaControlli = new RispostaControlli();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	// salvo il fascicolo quando il versamento è andato bene
	tmpRispostaControlli.setrBoolean(false);
	try {
	    Calendar tmpCal = Calendar.getInstance();
	    tmpCal.set(2444, 11, 31, 0, 0, 0); // 31 dicembre 2444, data di annullo fittizia

	    FasFascicolo tmpFascicolo = new FasFascicolo();
	    tmpFascicolo.setFasEventoFascicolos(new ArrayList<>());

	    tmpFascicolo.setOrgStrut(entityManager.find(OrgStrut.class, svf.getIdStruttura()));
	    tmpFascicolo.setAaFascicolo(new BigDecimal(svf.getChiaveNonVerificata().getAnno()));
	    tmpFascicolo.setCdKeyFascicolo(svf.getChiaveNonVerificata().getNumero());
	    tmpFascicolo.setCdKeyNormalizFascicolo(svf.getCdKeyFascicoloNormalized());
	    if (tmpCal.getTime() != null) {
		tmpFascicolo.setDtAnnull(convert(tmpCal.getTime()).toLocalDateTime());
	    }
	    tmpFascicolo.setTsIniSes(sessione.getTmApertura().toLocalDateTime());
	    tmpFascicolo.setTsFineSes(sessione.getTmChiusura().toLocalDateTime());
	    // salvo il nome del server/istanza nel cluster che sta salvando i dati e ha
	    // gestito il versamento
	    tmpFascicolo.setCdIndServer(appServerInstance.getName());
	    // salvo l'indirizzo IP del sistema che ha effettuato la richiesta di
	    // versamento/aggiunta
	    tmpFascicolo.setCdIndIpClient(sessione.getIpChiamante());
	    // salvo l'utente identificato (se non è identificato, la sessione è errata)
	    tmpFascicolo.setIamUser(
		    entityManager.find(IamUser.class, versamento.getUtente().getIdUtente()));
	    tmpFascicolo.setDecTipoFascicolo(
		    entityManager.find(DecTipoFascicolo.class, svf.getIdTipoFascicolo()));
	    //
	    tmpFascicolo.setTiConservazione(
		    it.eng.parer.fascicolo.jpa.entity.constraint.FasFascicolo.TiConservazione
			    .valueOf(sessione.getIndiceSIPFascicolo().getParametri()
				    .getTipoConservazione().name()));
	    // TODO IL sistema di migraziobe va gestito
	    //
	    tmpFascicolo.setFlForzaContrClassif(
		    svf.getFlControlliFasc().isFlForzaContrFlassif() ? CostantiDB.Flag.TRUE
			    : CostantiDB.Flag.FALSE);
	    tmpFascicolo.setFlForzaContrColleg(
		    svf.getFlControlliFasc().isFlForzaContrColleg() ? CostantiDB.Flag.TRUE
			    : CostantiDB.Flag.FALSE);
	    tmpFascicolo.setFlForzaContrNumero(
		    svf.getFlControlliFasc().isFlForzaContrNumero() ? CostantiDB.Flag.TRUE
			    : CostantiDB.Flag.FALSE);
	    tmpFascicolo.setFlUpdAnnulUnitaDoc(CostantiDB.Flag.FALSE); // fixed
	    // DatiXmlProfiloArchivistico mandatory
	    DatiXmlProfiloArchivistico dxpa = svf.getDatiXmlProfiloArchivistico();
	    //
	    tmpFascicolo.setCdIndiceClassif(dxpa.getIndiceClassificazione());
	    if (StringUtils.isNotBlank(dxpa.getDescIndiceClassificazione())) {
		tmpFascicolo.setDsIndiceClassif(LogSessioneUtils.getStringAtMaxLen(
			dxpa.getDescIndiceClassificazione(), Costanti.DS_INDICE_CLASS_MAX_LEN));
	    }
	    //
	    tmpFascicolo.setNiAaConservazione(dxpa.getTempoConservazione());
	    if (StringUtils.isNotBlank(dxpa.getInfoPianoCoservazione())) {
		tmpFascicolo.setDsInfoConservazione(dxpa.getInfoPianoCoservazione());
	    }

	    if (svf.getIdVoceTitol() != null) {
		tmpFascicolo.setDecVoceTitol(
			entityManager.find(DecVoceTitol.class, svf.getIdVoceTitol()));
	    }

	    if (svf.getDatiXmlProfiloGenerale() != null) {
		DatiXmlProfiloGenerale dxpg = svf.getDatiXmlProfiloGenerale();
		// oggetto fascicolo
		if (StringUtils.isNotBlank(dxpg.getOggettoFascicolo())) {
		    tmpFascicolo.setDsOggettoFascicolo(LogSessioneUtils.getStringAtMaxLen(
			    dxpg.getOggettoFascicolo(), Costanti.DS_OGGETTO_FASC_MAX_LEN));
		}
		// popolamento data apertura, chiusura, ud first e last, aaconservazione, note
		if (dxpg.getDataApertura() != null) {
		    tmpFascicolo
			    .setDtApeFascicolo(convert(dxpg.getDataApertura()).toLocalDateTime());
		}
		if (dxpg.getDataChiusura() != null) {
		    tmpFascicolo
			    .setDtChiuFascicolo(convert(dxpg.getDataChiusura()).toLocalDateTime());
		}
		if (StringUtils.isNotBlank(dxpg.getNoteFascicolo())) {
		    tmpFascicolo.setDsNota(LogSessioneUtils
			    .getStringAtMaxLen(dxpg.getNoteFascicolo(), Costanti.DS_NOTA_MAX_LEN));
		}

		tmpFascicolo.setCdLivelloRiserv(dxpg.getLvlRiservatezza());

		if (dxpg.getProcAmm() != null) {
		    DXPGProcAmmininistrativo procAmm = dxpg.getProcAmm();
		    tmpFascicolo.setCdProcAmmin(procAmm.getCodice());
		    tmpFascicolo.setDsProcAmmin(procAmm.getDenominazione());
		    tmpFascicolo.setDsProcAmminMateriaArgStrut(procAmm.getMateriaArgStrut());
		}

	    }
	    // TODO: gestire questi campi:
	    // aa_fascicolo_padre

	    // cd_key_fascicolo_padre ds_oggetto_fascicolo_padre id_fascicolo_padre
	    //
	    tmpFascicolo.setCdKeyOrd(svf.getKeyOrdCalcolata());
	    tmpFascicolo.setNiUnitaDoc(new BigDecimal(svf.getUnitaDocElencate().size()));
	    tmpFascicolo.setNiSottoFascicoli(BigDecimal.ZERO);
	    tmpFascicolo.setTiStatoFascElencoVers(
		    it.eng.parer.fascicolo.jpa.entity.constraint.FasFascicolo.TiStatoFascElencoVers.IN_ATTESA_SCHED);
	    // da fare: id_elenco_vers_fasc
	    tmpFascicolo.setTiStatoConservazione(
		    it.eng.parer.fascicolo.jpa.entity.constraint.FasFascicolo.TiStatoConservazione.PRESA_IN_CARICO);
	    // gestione fascicolo KO, in funzione della presenza o meno di un fascicolo KO
	    // corrispondente
	    if (fascicoloKo != null) {
		tmpFascicolo.setFlSesFascicoloKo(CostantiDB.Flag.TRUE);
	    } else {
		tmpFascicolo.setFlSesFascicoloKo(CostantiDB.Flag.FALSE); // il campo in tabella è
									 // NOT NULL
	    }

	    // eventi (recuperati dal profilo generale)
	    /*
	     * check != null superfluo in quanto elemento sempre presente previsto da xsd SIP
	     */
	    if (svf.getDatiXmlProfiloGenerale() != null) {
		for (DXPGEvento event : svf.getDatiXmlProfiloGenerale().getEventi()) {
		    FasEventoFascicolo eventoFasciolo = new FasEventoFascicolo();
		    eventoFasciolo.setFasFascicolo(tmpFascicolo);
		    eventoFasciolo.setDsDenomEvento(event.getDescrizione());
		    if (event.getDataInizio() != null) {
			eventoFasciolo
				.setTsApertura(convert(event.getDataInizio()).toLocalDateTime());
		    }
		    if (event.getDataFine() != null) {
			eventoFasciolo
				.setTsChiusura(convert(event.getDataFine()).toLocalDateTime());
		    }
		    tmpFascicolo.getFasEventoFascicolos().add(eventoFasciolo);
		}
	    }

	    entityManager.persist(tmpFascicolo);
	    tmpRispostaControlli.setrObject(tmpFascicolo);
	    entityManager.flush();
	    tmpRispostaControlli.setrBoolean(true);
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    LOG_FASCICOLO_ERR + ExceptionUtils.getRootCauseMessage(e));
	}

	return tmpRispostaControlli;
    }

    /*
     * Soggetti ed eventi da profilo GENERALE / NORMATIVO
     */
    @Override
    public void scriviSogggetti(VersFascicoloExt versamento, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	// salvo amm. partecipanti del fascicolo
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	try {
	    // init
	    fascicolo.setFasSogFascicolos(new ArrayList<>());
	    int soggCount = 1;
	    // generale
	    if (svf.getDatiXmlProfiloGenerale() != null) {
		soggCount = manageSoggetti(fascicolo, svf.getDatiXmlProfiloGenerale().getSoggetti(),
			soggCount);
	    }

	    // normativo
	    if (svf.getDatiXmlProfiloNormativo() != null) {
		manageSoggetti(fascicolo, svf.getDatiXmlProfiloNormativo().getSoggetti(),
			soggCount);
	    }
	    // OK
	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    LOG_FASCICOLO_ERR + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    private int manageSoggetti(FasFascicolo fascicolo, List<? extends ISoggetto> soggetti,
	    int soggCount) {
	for (ISoggetto<IIdentSoggetto, IEvento> sogg : soggetti) {
	    FasSogFascicolo fasSogFascicolo = new FasSogFascicolo();
	    fasSogFascicolo.setFasEventoSogs(new ArrayList<>());
	    fasSogFascicolo.setFasCodIdeSogs(new ArrayList<>());
	    fasSogFascicolo.setFasRifIndSogs(new ArrayList<>());

	    // can be null
	    fasSogFascicolo.setDsDenomSog(sogg.getDenominazione());
	    // can be null
	    fasSogFascicolo.setNmCognSog(sogg.getCognome());
	    // can be null
	    fasSogFascicolo.setNmNomeSog(sogg.getNome());
	    //
	    fasSogFascicolo.setDsCit(sogg.getCittadinanza());
	    //
	    fasSogFascicolo.setDsCmnNsc(sogg.getLuogoNascita());
	    // ruolo
	    fasSogFascicolo.setTiRapp(sogg.getTipoRapporto());
	    //
	    fasSogFascicolo.setFasFascicolo(fascicolo);

	    // indirizzi di riferimento
	    for (String indirizzo : sogg.getIndirizzoDigitRifs()) {
		FasRifIndSog rifIndSog = new FasRifIndSog();
		//
		rifIndSog.setFasSogFascicolo(fasSogFascicolo);
		rifIndSog.setDsIndDifRif(indirizzo);
		fasSogFascicolo.getFasRifIndSogs().add(rifIndSog);
	    }

	    // identificativo default
	    if (sogg.getIdentificativi().isEmpty()) {
		FasCodIdeSog codIdeSog = new FasCodIdeSog();
		//
		codIdeSog.setFasSogFascicolo(fasSogFascicolo);
		codIdeSog.setTiCodSog(TiCodSog.PREDEFINITO);
		codIdeSog.setCdSog(Costanti.SOG_COD_IDENT_PARERLOCAL);
		codIdeSog.setNmCodSog(
			generateSogFascLocalIdent(fascicolo.getAaFascicolo().toString(),
				fascicolo.getCdKeyNormalizFascicolo(), soggCount));
		fasSogFascicolo.getFasCodIdeSogs().add(codIdeSog);
	    } else {
		// identificativi
		for (IIdentSoggetto ident : sogg.getIdentificativi()) {
		    FasCodIdeSog codIdeSog = new FasCodIdeSog();
		    //
		    codIdeSog.setFasSogFascicolo(fasSogFascicolo);
		    codIdeSog.setTiCodSog(
			    ident.isPredefined() ? TiCodSog.PREDEFINITO : TiCodSog.NON_PREDEFINITO);
		    codIdeSog.setCdSog(ident.getTipo());
		    codIdeSog.setNmCodSog(ident.getCodice());
		    fasSogFascicolo.getFasCodIdeSogs().add(codIdeSog);
		}
	    }
	    // eventi
	    for (IEvento event : sogg.getEventi()) {
		FasEventoSog eventoSog = new FasEventoSog();
		//
		eventoSog.setFasSogFascicolo(fasSogFascicolo);
		eventoSog.setDsDenomEvento(event.getDescrizione());
		if (event.getDataInizio() != null) {
		    eventoSog.setTsApertura(convert(event.getDataInizio()).toLocalDateTime());
		}
		if (event.getDataFine() != null) {
		    eventoSog.setTsChiusura(convert(event.getDataFine()).toLocalDateTime());
		}
		fasSogFascicolo.getFasEventoSogs().add(eventoSog);
	    }

	    entityManager.persist(fasSogFascicolo);
	    fascicolo.getFasSogFascicolos().add(fasSogFascicolo);
	    // inc index
	    soggCount++;
	}

	return soggCount;
    }

    private String generateSogFascLocalIdent(String anno, String cdKeyNorm, long soggCount) {
	return MessageFormat.format(Costanti.SOG_IDENT_PARERLOCAL_FMT, anno, cdKeyNorm,
		Costanti.SOG_IDENT_PARERLOCAL_SOGGETTO, String.valueOf(soggCount));
    }

    @Override
    public void scriviRequestResponseFascicolo(RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento, BlockingFakeSession sessione,
	    BackendStorage backendMetadata, Map<String, String> sipBlob, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	CompRapportoVersFascicolo esito = rispostaWs.getCompRapportoVersFascicolo();
	try {
	    // init
	    fascicolo.setFasXmlVersFascicolos(new ArrayList<>());

	    FasXmlVersFascicolo tmpXmlVersFascicolo = new FasXmlVersFascicolo();
	    tmpXmlVersFascicolo.setFasFascicolo(fascicolo);
	    tmpXmlVersFascicolo.setTiXmlVers(CostantiDB.TipiXmlDati.RICHIESTA.toString());
	    tmpXmlVersFascicolo.setCdVersioneXml(svf.getVersioneIndiceSipNonVerificata());
	    tmpXmlVersFascicolo.setFlCanonicalized(CostantiDB.Flag.TRUE);
	    // MEV#30786
	    String blXmlVers = sessione.getDatiDaSalvareIndiceSip().length() == 0 ? "--"
		    : sessione.getDatiDaSalvareIndiceSip();
	    if (backendMetadata.isDataBase()) {
		tmpXmlVersFascicolo.setBlXmlVers(blXmlVers);
	    } else {
		sipBlob.put(CostantiDB.TipiXmlDati.RICHIESTA.toString(), blXmlVers);
	    }
	    // end MEV#30786
	    tmpXmlVersFascicolo
		    .setCdEncodingHashXmlVers(CostantiDB.TipiEncBinari.HEX_BINARY.descrivi());
	    tmpXmlVersFascicolo.setDsAlgoHashXmlVers(CostantiDB.TipiHash.SHA_256.descrivi());
	    tmpXmlVersFascicolo.setDsUrnXmlVers(MessaggiWSFormat.formattaUrnIndiceSipFasc(
		    versamento.getStrutturaComponenti().getUrnPartChiaveFascicolo(),
		    Costanti.UrnFormatter.URN_INDICE_SIP_V2));
	    // normalized URN
	    tmpXmlVersFascicolo.setDsUrnNormalizXmlVers(MessaggiWSFormat.formattaUrnIndiceSipFasc(
		    versamento.getStrutturaComponenti().getUrnPartChiaveFascicoloNormalized(),
		    Costanti.UrnFormatter.URN_INDICE_SIP_V2));
	    tmpXmlVersFascicolo.setDsHashXmlVers(new HashCalculator()
		    .calculateHashSHAX(sessione.getDatiDaSalvareIndiceSip(), TipiHash.SHA_256)
		    .toHexBinary());
	    tmpXmlVersFascicolo.setIdStrut(new BigDecimal(svf.getIdStruttura()));
	    tmpXmlVersFascicolo.setDtVersFascicolo(sessione.getTmApertura().toLocalDateTime());
	    entityManager.persist(tmpXmlVersFascicolo);
	    fascicolo.getFasXmlVersFascicolos().add(tmpXmlVersFascicolo);

	    String xmlesito = logSessioneFascicoliDao.generaRapportoVersamento(rispostaWs);

	    tmpXmlVersFascicolo = new FasXmlVersFascicolo();
	    tmpXmlVersFascicolo.setFasFascicolo(fascicolo);
	    tmpXmlVersFascicolo.setTiXmlVers(CostantiDB.TipiXmlDati.RISPOSTA.toString());
	    tmpXmlVersFascicolo.setCdVersioneXml(esito.getVersioneRapportoVersamento());
	    tmpXmlVersFascicolo.setFlCanonicalized(CostantiDB.Flag.FALSE);
	    // MEV#30786
	    if (backendMetadata.isDataBase()) {
		tmpXmlVersFascicolo.setBlXmlVers(xmlesito);
	    } else {
		sipBlob.put(CostantiDB.TipiXmlDati.RISPOSTA.toString(), xmlesito);
	    }
	    // end MEV#30786
	    tmpXmlVersFascicolo
		    .setCdEncodingHashXmlVers(CostantiDB.TipiEncBinari.HEX_BINARY.descrivi());
	    tmpXmlVersFascicolo.setDsAlgoHashXmlVers(CostantiDB.TipiHash.SHA_256.descrivi());
	    tmpXmlVersFascicolo.setDsUrnXmlVers(MessaggiWSFormat.formattaUrnRappVersFasc(
		    versamento.getStrutturaComponenti().getUrnPartChiaveFascicolo(),
		    Costanti.UrnFormatter.URN_RAPP_VERS_V2));
	    // normalized URN
	    tmpXmlVersFascicolo.setDsUrnNormalizXmlVers(MessaggiWSFormat.formattaUrnRappVersFasc(
		    versamento.getStrutturaComponenti().getUrnPartChiaveFascicoloNormalized(),
		    Costanti.UrnFormatter.URN_RAPP_VERS_V2));
	    tmpXmlVersFascicolo.setDsHashXmlVers(new HashCalculator()
		    .calculateHashSHAX(xmlesito, TipiHash.SHA_256).toHexBinary());
	    tmpXmlVersFascicolo.setIdStrut(new BigDecimal(svf.getIdStruttura()));
	    tmpXmlVersFascicolo.setDtVersFascicolo(sessione.getTmApertura().toLocalDateTime());
	    entityManager.persist(tmpXmlVersFascicolo);
	    fascicolo.getFasXmlVersFascicolos().add(tmpXmlVersFascicolo);

	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio XML versamento fascicolo: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void scriviProfiliXMLFascicolo(VersFascicoloExt versamento, BlockingFakeSession sessione,
	    BackendStorage backendMetadata, Map<String, String> profBlob, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	try {
	    // init
	    fascicolo.setFasXmlFascicolos(new ArrayList<>());

	    FasXmlFascicolo fasXmlFascicolo = new FasXmlFascicolo();
	    fasXmlFascicolo.setFasFascicolo(fascicolo);
	    fasXmlFascicolo.setTiModelloXsd(TiModXsdFasXmlFascicolo.PROFILO_GENERALE_FASCICOLO);
	    fasXmlFascicolo.setDecModelloXsdFascicolo(entityManager
		    .find(DecModelloXsdFascicolo.class, svf.getIdRecxsdProfiloGenerale()));
	    String xmlProfilo = generaXmlProfilo(
		    sessione.getIndiceSIPFascicolo().getProfiloGenerale().getAny(), true);
	    fasXmlFascicolo.setFlCanonicalized(CostantiDB.Flag.TRUE);
	    // MEV#30786
	    if (backendMetadata.isDataBase()) {
		fasXmlFascicolo.setBlXml(xmlProfilo);
	    } else {
		profBlob.put(TiModXsdFasXmlFascicolo.PROFILO_GENERALE_FASCICOLO.toString(),
			xmlProfilo);
	    }
	    // end MEV#30786
	    fasXmlFascicolo.setIdStrut(new BigDecimal(svf.getIdStruttura()));
	    fasXmlFascicolo.setDtVersFascicolo(sessione.getTmApertura().toLocalDateTime());
	    entityManager.persist(fasXmlFascicolo);
	    fascicolo.getFasXmlFascicolos().add(fasXmlFascicolo);

	    // il profilo archivistico potrebbe non esistere
	    if (svf.getIdRecXsdProfiloArchivistico() > 0) {
		fasXmlFascicolo = new FasXmlFascicolo();
		fasXmlFascicolo.setFasFascicolo(fascicolo);
		fasXmlFascicolo
			.setTiModelloXsd(TiModXsdFasXmlFascicolo.PROFILO_ARCHIVISTICO_FASCICOLO);
		fasXmlFascicolo.setDecModelloXsdFascicolo(entityManager
			.find(DecModelloXsdFascicolo.class, svf.getIdRecXsdProfiloArchivistico()));
		xmlProfilo = generaXmlProfilo(
			sessione.getIndiceSIPFascicolo().getProfiloArchivistico().getAny(), true);
		fasXmlFascicolo.setFlCanonicalized(CostantiDB.Flag.TRUE);
		// MEV#30786
		if (backendMetadata.isDataBase()) {
		    fasXmlFascicolo.setBlXml(xmlProfilo);
		} else {
		    profBlob.put(TiModXsdFasXmlFascicolo.PROFILO_ARCHIVISTICO_FASCICOLO.toString(),
			    xmlProfilo);
		}
		// end MEV#30786
		fasXmlFascicolo.setIdStrut(new BigDecimal(svf.getIdStruttura()));
		fasXmlFascicolo.setDtVersFascicolo(sessione.getTmApertura().toLocalDateTime());
		entityManager.persist(fasXmlFascicolo);
		fascicolo.getFasXmlFascicolos().add(fasXmlFascicolo);
	    }

	    // il profilo normativo potrebbe non esistere
	    if (svf.getIdRecXsdProfiloNormativo() > 0) {
		fasXmlFascicolo = new FasXmlFascicolo();
		fasXmlFascicolo.setFasFascicolo(fascicolo);
		fasXmlFascicolo
			.setTiModelloXsd(TiModXsdFasXmlFascicolo.PROFILO_NORMATIVO_FASCICOLO);
		fasXmlFascicolo.setDecModelloXsdFascicolo(entityManager
			.find(DecModelloXsdFascicolo.class, svf.getIdRecXsdProfiloNormativo()));
		xmlProfilo = generaXmlProfilo(
			sessione.getIndiceSIPFascicolo().getProfiloNormativo().getValue().getAny(),
			true);
		fasXmlFascicolo.setFlCanonicalized(CostantiDB.Flag.TRUE);
		// MEV#30786
		if (backendMetadata.isDataBase()) {
		    fasXmlFascicolo.setBlXml(xmlProfilo);
		} else {
		    profBlob.put(TiModXsdFasXmlFascicolo.PROFILO_NORMATIVO_FASCICOLO.toString(),
			    xmlProfilo);
		}
		// end MEV#30786
		fasXmlFascicolo.setIdStrut(new BigDecimal(svf.getIdStruttura()));
		fasXmlFascicolo.setDtVersFascicolo(sessione.getTmApertura().toLocalDateTime());
		entityManager.persist(fasXmlFascicolo);
		fascicolo.getFasXmlFascicolos().add(fasXmlFascicolo);
	    }

	    // il profilo specifico potrebbe non esistere
	    if (svf.getIdRecXsdDatiSpec() > 0) {
		fasXmlFascicolo = new FasXmlFascicolo();
		fasXmlFascicolo.setFasFascicolo(fascicolo);
		fasXmlFascicolo
			.setTiModelloXsd(TiModXsdFasXmlFascicolo.PROFILO_SPECIFICO_FASCICOLO);
		fasXmlFascicolo.setDecModelloXsdFascicolo(entityManager
			.find(DecModelloXsdFascicolo.class, svf.getIdRecXsdDatiSpec()));
		xmlProfilo = generaXmlProfiloSpec(
			sessione.getIndiceSIPFascicolo().getProfiloSpecifico());
		fasXmlFascicolo.setFlCanonicalized(CostantiDB.Flag.FALSE);
		// MEV#30786
		if (backendMetadata.isDataBase()) {
		    fasXmlFascicolo.setBlXml(xmlProfilo);
		} else {
		    profBlob.put(TiModXsdFasXmlFascicolo.PROFILO_SPECIFICO_FASCICOLO.toString(),
			    xmlProfilo);
		}
		// end MEV#30786
		fasXmlFascicolo.setIdStrut(new BigDecimal(svf.getIdStruttura()));
		fasXmlFascicolo.setDtVersFascicolo(sessione.getTmApertura().toLocalDateTime());
		entityManager.persist(fasXmlFascicolo);
		fascicolo.getFasXmlFascicolos().add(fasXmlFascicolo);
	    }

	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio profili xml fascicolo: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void scriviUnitaDocFascicolo(VersFascicoloExt versamento, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	try {
	    if (svf.getUnitaDocElencate() != null && !svf.getUnitaDocElencate().isEmpty()) {
		// init
		fascicolo.setFasUnitaDocFascicolos(new ArrayList<>());
		for (UnitaDocLink udColl : svf.getUnitaDocElencate()) {
		    FasUnitaDocFascicolo fasUnitaDocFascicolo = new FasUnitaDocFascicolo();
		    fasUnitaDocFascicolo.setFasFascicolo(fascicolo);
		    fasUnitaDocFascicolo.setAroUnitaDoc(
			    entityManager.find(AroUnitaDoc.class, udColl.getIdLinkUnitaDoc()));
		    if (udColl.getDataInserimentoFas() != null) {
			fasUnitaDocFascicolo.setDtDataInserimentoFas(
				convert(udColl.getDataInserimentoFas()).toLocalDateTime());
		    }
		    if (udColl.getPosizione() != null) {
			fasUnitaDocFascicolo.setNiPosizione(new BigDecimal(udColl.getPosizione()));
		    }
		    entityManager.persist(fasUnitaDocFascicolo);
		    fascicolo.getFasUnitaDocFascicolos().add(fasUnitaDocFascicolo);
		}
	    }
	    //
	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio unità doc fascicolo: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void scriviLinkFascicolo(VersFascicoloExt versamento, FasFascicolo tmpFasFascicolo)
	    throws AppGenericPersistenceException {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	// salvo i riferimenti alle unità documentarie del fascicolo
	try {
	    // init
	    tmpFasFascicolo.setFasLinkFascicolos1(new ArrayList<>());

	    if (svf.getFascicoliLinked() != null) {
		for (FascicoloLink link : svf.getFascicoliLinked()) {
		    FasLinkFascicolo fasLinkFascicolo = new FasLinkFascicolo();
		    fasLinkFascicolo.setCdKeyFascicoloLink(link.getCsChiaveFasc().getNumero());
		    fasLinkFascicolo.setAaFascicoloLink(
			    BigDecimal.valueOf(link.getCsChiaveFasc().getAnno()));
		    fasLinkFascicolo.setDsLink(link.getDescCollegamento());
		    fasLinkFascicolo.setFasFascicolo(tmpFasFascicolo);
		    if (link.getIdLinkFasc() != null) {
			fasLinkFascicolo.setFasFascicoloLink(
				entityManager.find(FasFascicolo.class, link.getIdLinkFasc()));
		    }

		    entityManager.persist(fasLinkFascicolo);
		    tmpFasFascicolo.getFasLinkFascicolos1().add(fasLinkFascicolo);
		}
	    }
	    //
	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio unità doc fascicolo: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void scriviWarningFascicolo(VersFascicoloExt versamento, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	// salvo i warning della sessione di versamento
	// Nota: al momento della stesura di questo codice il web service
	// non prevede la restituzione di warning, Questo codice è quindi
	// abbastanza inattivo.
	int progErrore = 1;
	try {
	    // init
	    fascicolo.setFasWarnFascicolos(new ArrayList<>());
	    FasWarnFascicolo fasWarnFascicolo;
	    for (VoceDiErrore tmpVoceDiErrore : versamento.getErroriTrovati()) {
		fasWarnFascicolo = new FasWarnFascicolo();
		fasWarnFascicolo.setFasFascicolo(fascicolo);
		fasWarnFascicolo.setPgWarn(new BigDecimal(progErrore));
		fasWarnFascicolo.setDecErrSacer(
			messaggiWSHelper.caricaDecErrore(tmpVoceDiErrore.getErrorCode()));
		fasWarnFascicolo.setDsWarn(
			LogSessioneUtils.getDsErrAtMaxLen(tmpVoceDiErrore.getErrorMessage()));
		//
		entityManager.persist(fasWarnFascicolo);
		fascicolo.getFasWarnFascicolos().add(fasWarnFascicolo);
		progErrore++;
	    }
	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio warning fascicolo: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void salvaWarningAATipoFascicolo(VersFascicoloExt versamento,
	    BlockingFakeSession sessione) throws AppGenericPersistenceException {
	try {
	    long numWarningAggiornati;
	    final String queryStr = "update DecWarnAaTipoFascicolo al "
		    + "set al.flWarnAaTipoFascicolo = :flWarnAaTipoFascicolo "
		    + "where al.decAaTipoFascicolo.idAaTipoFascicolo = :idAaTipoFascicolo "
		    + "and al.aaTipoFascicolo = :aaTipoFascicolo ";

	    // eseguo l'update dell'eventuale record relativo all'anno
	    Query query = entityManager.createQuery(queryStr);
	    query.setParameter("flWarnAaTipoFascicolo", CostantiDB.Flag.TRUE);
	    query.setParameter("idAaTipoFascicolo",
		    versamento.getStrutturaComponenti().getConfigNumFasc().getIdAaNumeroFasc());
	    query.setParameter("aaTipoFascicolo", new BigDecimal(
		    sessione.getIndiceSIPFascicolo().getIntestazione().getChiave().getAnno()));
	    numWarningAggiornati = query.executeUpdate();
	    // se non ho aggiornato alcun record, vuol dire che lo devo creare...
	    if (numWarningAggiornati == 0) {
		DecWarnAaTipoFascicolo tmpDecWarnAaTipoFascicolo = new DecWarnAaTipoFascicolo();
		tmpDecWarnAaTipoFascicolo.setAaTipoFascicolo(new BigDecimal(
			sessione.getIndiceSIPFascicolo().getIntestazione().getChiave().getAnno()));
		tmpDecWarnAaTipoFascicolo.setDecAaTipoFascicolo(
			entityManager.find(DecAaTipoFascicolo.class, versamento
				.getStrutturaComponenti().getConfigNumFasc().getIdAaNumeroFasc()));
		tmpDecWarnAaTipoFascicolo.setFlWarnAaTipoFascicolo(CostantiDB.Flag.TRUE);
		entityManager.persist(tmpDecWarnAaTipoFascicolo);
		entityManager.flush();
	    }
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio warning aa tipo fascicolo: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void ereditaVersamentiKoFascicolo(FasFascicolo fascicolo, VrsFascicoloKo fascicoloKo)
	    throws AppGenericPersistenceException {
	try {
	    // aggiorno tutte le sessione fallite relative al fascicolo KO
	    // ancora lockato: pongo a null la FK al fascicolo KO, imposto la
	    // FK al fascicolo appena creato e pongo lo stato a RISOLTO
	    for (VrsSesFascicoloKo vsfk : fascicoloKo.getVrsSesFascicoloKos()) {
		vsfk.setFasFascicolo(fascicolo);
		vsfk.setVrsFascicoloKo(null);
		vsfk.setTiStatoSes(CostantiDB.StatoVrsFascicoloKo.RISOLTO.toString());
	    }
	    entityManager.flush();
	    //
	    // rimuovo il fascicolo KO
	    entityManager.remove(fascicoloKo);
	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    LOG_FASCICOLO_ERR + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void scriviDatiSpecGen(VersFascicoloExt versamento, FasFascicolo tmpFasFascicolo)
	    throws AppGenericPersistenceException {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	try {
	    for (DatoSpecifico tmpDS : svf.getDatiSpecifici().values()) {
		FasValoreAttribFascicolo valoreAttribFascicolo = new FasValoreAttribFascicolo();
		valoreAttribFascicolo.setDecUsoModelloXsdFasc(entityManager
			.find(DecUsoModelloXsdFasc.class, svf.getIdRecXsdUsoProfiloSpec()));
		valoreAttribFascicolo.setDecAttribFascicolo(
			entityManager.find(DecAttribFascicolo.class, tmpDS.getIdDatoSpec()));
		valoreAttribFascicolo.setDecAaTipoFascicolo(
			entityManager.find(DecAaTipoFascicolo.class, svf.getIdAATipoFasc()));
		valoreAttribFascicolo.setFasFascicolo(tmpFasFascicolo);
		valoreAttribFascicolo.setDlValore(tmpDS.getValore());
		// inserisco su DB
		entityManager.persist(valoreAttribFascicolo);
	    }
	    //
	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio dati specifici fascicolo: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    private String generaXmlProfilo(Node profilo, boolean canonicalize)
	    throws TransformerException {
	String xml = null;

	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	// to be compliant, prohibit the use of all protocols by external entities:
	transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
	transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
	Transformer transformer = transformerFactory.newTransformer();
	DOMSource source = new DOMSource(profilo);
	StreamResult result = new StreamResult(new StringWriter());
	transformer.transform(source, result);
	xml = result.getWriter().toString();
	return canonicalize ? XmlUtils.doCanonicalizzazioneXml(xml, false) : xml;
    }

    private String generaXmlProfiloSpec(JAXBElement<ProfiloSpecificoType> profile)
	    throws JAXBException {
	StringWriter result = new StringWriter();
	// 3. Marshall and validate
	Marshaller m = xmlFascCache.getVersReqFascicoloCtxProfiloSpec().createMarshaller();
	m.marshal(profile, result);
	return result.toString();
    }

}
