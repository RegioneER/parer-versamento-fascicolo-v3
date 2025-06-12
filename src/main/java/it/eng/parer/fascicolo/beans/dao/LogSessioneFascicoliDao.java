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

package it.eng.parer.fascicolo.beans.dao;

import static it.eng.parer.fascicolo.beans.utils.converter.DateUtilsConverter.convert;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import it.eng.parer.fascicolo.beans.AppServerInstance;
import it.eng.parer.fascicolo.beans.IlogSessioneFascicoliDao;
import it.eng.parer.fascicolo.beans.XmlFascCache;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.base.VoceDiErrore;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.utils.CostantiDB;
import it.eng.parer.fascicolo.beans.utils.LogSessioneUtils;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSHelper;
import it.eng.parer.fascicolo.jpa.entity.DecErrSacer;
import it.eng.parer.fascicolo.jpa.entity.DecTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.IamUser;
import it.eng.parer.fascicolo.jpa.entity.MonContaFascicoliKo;
import it.eng.parer.fascicolo.jpa.entity.OrgStrut;
import it.eng.parer.fascicolo.jpa.entity.VrsErrSesFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloErr;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsXmlSesFascicoloErr;
import it.eng.parer.fascicolo.jpa.entity.VrsXmlSesFascicoloKo;
import it.eng.parer.fascicolo.jpa.viewEntity.VrsVUpdFascicoloKo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class LogSessioneFascicoliDao implements IlogSessioneFascicoliDao {

    @Inject
    MessaggiWSHelper messaggiWSHelper;

    @Inject
    XmlFascCache xmlFascCache;

    @Inject
    AppServerInstance appServerInstance;

    @Inject
    EntityManager entityManager;

    // costanti per la tabella VrsSesFascicoloErr
    private static final String SESSIONE_NON_VERIFICATA = "NON_VERIFICATO";

    //
    private static final String TIPO_ERR_FATALE = "FATALE";
    private static final String TIPO_ERR_WARNING = "WARNING";
    private static final String CD_DS_ERR_DIVERSI = "Diversi";

    @Override
    public RispostaControlli scriviFascicoloErr(RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento, BlockingFakeSession sessione)
	    throws AppGenericPersistenceException {
	RispostaControlli tmpRispostaControlli = new RispostaControlli();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	// salvo sessione errata
	tmpRispostaControlli.setrBoolean(false);
	try {
	    VrsSesFascicoloErr tmpFascicoloErr = new VrsSesFascicoloErr();
	    tmpFascicoloErr.setCdVersioneWs(sessione.getVersioneWS());
	    tmpFascicoloErr.setTsIniSes(sessione.getTmApertura().toLocalDateTime());
	    tmpFascicoloErr.setTsFineSes(sessione.getTmChiusura().toLocalDateTime());
	    tmpFascicoloErr.setNmUseridWs(sessione.getLoginName());
	    // salvo il nome del server/istanza nel cluster che sta salvando i dati e ha
	    // gestito il versamento
	    tmpFascicoloErr.setCdIndServer(appServerInstance.getName());
	    // salvo l'indirizzo IP del sistema che ha effettuato la richiesta di
	    // versamento/aggiunta
	    tmpFascicoloErr.setCdIndIpClient(sessione.getIpChiamante());
	    tmpFascicoloErr.setTiStatoSes(SESSIONE_NON_VERIFICATA);
	    if (svf.getVersatoreNonverificato() != null) {
		tmpFascicoloErr.setNmAmbiente(svf.getVersatoreNonverificato().getAmbiente());
		tmpFascicoloErr.setNmEnte(svf.getVersatoreNonverificato().getEnte());
		tmpFascicoloErr.setNmStrut(svf.getVersatoreNonverificato().getStruttura());
	    }
	    if (svf.getIdStruttura() > 0) {
		tmpFascicoloErr
			.setOrgStrut(entityManager.find(OrgStrut.class, svf.getIdStruttura()));
	    }
	    if (svf.getChiaveNonVerificata() != null) {
		tmpFascicoloErr
			.setAaFascicolo(new BigDecimal(svf.getChiaveNonVerificata().getAnno()));
		tmpFascicoloErr.setCdKeyFascicolo(svf.getChiaveNonVerificata().getNumero());
	    }
	    if (svf.getIdTipoFascicolo() > 0) {
		tmpFascicoloErr.setDecTipoFascicolo(
			entityManager.find(DecTipoFascicolo.class, svf.getIdTipoFascicolo()));
	    }
	    tmpFascicoloErr.setNmTipoFascicolo(svf.getTipoFascicoloNonverificato());
	    //
	    CompRapportoVersFascicolo esito = rispostaWs.getCompRapportoVersFascicolo();
	    if (rispostaWs.getSeverity() == IRispostaWS.SeverityEnum.ERROR) {
		tmpFascicoloErr.setDecErrSacer(messaggiWSHelper
			.caricaDecErrore(esito.getEsitoGenerale().getCodiceErrore()));
		tmpFascicoloErr.setDsErr(LogSessioneUtils
			.getDsErrAtMaxLen(esito.getEsitoGenerale().getMessaggioErrore()));
	    }

	    entityManager.persist(tmpFascicoloErr);
	    tmpRispostaControlli.setrObject(tmpFascicoloErr);
	    tmpRispostaControlli.setrBoolean(true);
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio sessione errata: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return tmpRispostaControlli;
    }

    /**
     * @param rispostaWs
     * @param versamento
     * @param fascicoloErr
     * @param backendMetadata
     * @param sipBlob
     *
     * @throws AppGenericPersistenceException
     */
    @Override
    public void scriviXmlFascicoloErr(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
	    VrsSesFascicoloErr fascicoloErr, BackendStorage backendMetadata,
	    Map<String, String> sipBlob) throws AppGenericPersistenceException {
	// salvo xml di request e response sessione errata
	CompRapportoVersFascicolo esito = rispostaWs.getCompRapportoVersFascicolo();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	try {
	    VrsXmlSesFascicoloErr tmpXmlSesFascicoloErr = new VrsXmlSesFascicoloErr();
	    tmpXmlSesFascicoloErr.setVrsSesFascicoloErr(fascicoloErr);
	    tmpXmlSesFascicoloErr.setTiXml(CostantiDB.TipiXmlDati.RICHIESTA.toString());
	    tmpXmlSesFascicoloErr.setCdVersioneXml(svf.getVersioneIndiceSipNonVerificata());
	    // MEV#30786
	    String blXml = versamento.getDatiXml().length() == 0 ? "--" : versamento.getDatiXml();
	    if (backendMetadata.isDataBase()) {
		tmpXmlSesFascicoloErr.setBlXml(blXml);
	    } else {
		sipBlob.put(CostantiDB.TipiXmlDati.RICHIESTA.toString(), blXml);
	    }
	    // end MEV#30786
	    entityManager.persist(tmpXmlSesFascicoloErr);

	    String xmlesito = this.generaRapportoVersamento(rispostaWs);

	    tmpXmlSesFascicoloErr = new VrsXmlSesFascicoloErr();
	    tmpXmlSesFascicoloErr.setVrsSesFascicoloErr(fascicoloErr);
	    tmpXmlSesFascicoloErr.setTiXml(CostantiDB.TipiXmlDati.RISPOSTA.toString());
	    tmpXmlSesFascicoloErr.setCdVersioneXml(esito.getVersioneRapportoVersamento());
	    // MEV#30786
	    if (backendMetadata.isDataBase()) {
		tmpXmlSesFascicoloErr.setBlXml(xmlesito);
	    } else {
		sipBlob.put(CostantiDB.TipiXmlDati.RISPOSTA.toString(), xmlesito);
	    }
	    // end MEV#30786
	    entityManager.persist(tmpXmlSesFascicoloErr);

	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio  dati xml sessione errata: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public RispostaControlli cercaFascicoloKo(VersFascicoloExt versamento)
	    throws AppGenericPersistenceException {
	RispostaControlli tmpRispostaControlli = new RispostaControlli();
	// cerco e recupero il fascicolo fallito
	tmpRispostaControlli.setrBoolean(false);
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	List<VrsFascicoloKo> fasFascicolos;

	try {
	    final String queryStr = "select ud " + "from VrsFascicoloKo ud join ud.orgStrut org "
		    + "where org.idStrut = :idStrutIn "
		    + " and ud.cdKeyFascicolo = :cdKeyFascicolo "
		    + " and ud.aaFascicolo = :aaFascicolo ";
	    TypedQuery<VrsFascicoloKo> query = entityManager.createQuery(queryStr,
		    VrsFascicoloKo.class);
	    query.setParameter("idStrutIn", svf.getIdStruttura());
	    query.setParameter("cdKeyFascicolo", svf.getChiaveNonVerificata().getNumero());
	    query.setParameter("aaFascicolo",
		    new BigDecimal(svf.getChiaveNonVerificata().getAnno()));
	    fasFascicolos = query.getResultList();
	    // rendo comunque true, per indicare che la query è andata bene:
	    // è perfettamente normale non trovare nulla se questo è il primo
	    // errore per il fascicolo. in questo caso dovrò creare la riga in tabella
	    // nel caso di scrittura di sessione fallita.
	    tmpRispostaControlli.setrBoolean(true);
	    if (!fasFascicolos.isEmpty()) {
		// se poi ho anche trovato qualcosa di utile, lo restituisco al
		// chiamante dopo averlo bloccato in modo esclusivo
		Map<String, Object> properties = new HashMap<>();
		properties.put("jakarta.persistence.lock.timeout", 25000L);
		VrsFascicoloKo tmpFascicoloKo = entityManager.find(VrsFascicoloKo.class,
			fasFascicolos.get(0).getIdFascicoloKo(), LockModeType.PESSIMISTIC_WRITE,
			properties);

		// questo stesso lock viene usato sia in fase di scrittura sessione KO
		// che di scrittura sessione buona. In questo secondo caso la riga verrà
		// cancellata. Dal momento che per condizioni sfortunate di concorrenza è
		// possibile che la query iniziale renda dei dati ma la find non
		// trovi nulla, verifico che nel frattempo la riga non sia stata cancellata.
		if (tmpFascicoloKo != null) {
		    tmpRispostaControlli.setrLong(tmpFascicoloKo.getIdFascicoloKo());
		    tmpRispostaControlli.setrObject(tmpFascicoloKo);
		} else {
		    throw new AppGenericPersistenceException(
			    "Errore di concorrenza nel salvataggio della sessione",
			    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
				    "Errore di concorrenza nel salvataggio della sessione:"
					    + "il fascicolo KO è stato rimosso. "
					    + "Si consiglia di ritentare il versamento."));
		}
	    }
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase ricerca Fascicolo KO (SF): "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return tmpRispostaControlli;
    }

    @Override
    public RispostaControlli scriviFascicoloKo(RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento, BlockingFakeSession sessione)
	    throws AppGenericPersistenceException {
	RispostaControlli tmpRispostaControlli = new RispostaControlli();
	// scrive una nuova istanza di Fascicolo KO per il versamento
	tmpRispostaControlli.setrBoolean(false);
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	CompRapportoVersFascicolo esito = rispostaWs.getCompRapportoVersFascicolo();

	try {
	    VrsFascicoloKo tmpFascicoloKo = new VrsFascicoloKo();
	    tmpFascicoloKo.setOrgStrut(entityManager.find(OrgStrut.class, svf.getIdStruttura()));
	    tmpFascicoloKo.setAaFascicolo(new BigDecimal(svf.getChiaveNonVerificata().getAnno()));
	    tmpFascicoloKo.setCdKeyFascicolo(svf.getChiaveNonVerificata().getNumero());
	    tmpFascicoloKo.setTsIniFirstSes(sessione.getTmApertura().toLocalDateTime());
	    tmpFascicoloKo.setTsIniLastSes(sessione.getTmApertura().toLocalDateTime()); // coincidono:
											// c'è una
											// sola
											// sessione
	    tmpFascicoloKo.setDecErrSacer(
		    messaggiWSHelper.caricaDecErrore(esito.getEsitoGenerale().getCodiceErrore()));
	    tmpFascicoloKo.setDsErrPrinc(LogSessioneUtils
		    .getDsErrAtMaxLen(esito.getEsitoGenerale().getMessaggioErrore()));
	    tmpFascicoloKo.setDecTipoFascicolo(entityManager.find(DecTipoFascicolo.class,
		    versamento.getStrutturaComponenti().getIdTipoFascicolo()));
	    tmpFascicoloKo.setTiStatoFascicoloKo(
		    CostantiDB.StatoVrsFascicoloKo.NON_VERIFICATO.toString());

	    // gli altri due campi li potrò popolare solo una volta persistita la sessione
	    // KO
	    entityManager.persist(tmpFascicoloKo);
	    tmpRispostaControlli.setrObject(tmpFascicoloKo);

	    entityManager.flush();
	    tmpRispostaControlli.setrBoolean(true);

	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio fascicoloKO: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return tmpRispostaControlli;
    }

    @Override
    public RispostaControlli scriviSessioneFascicoloKo(RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento, BlockingFakeSession sessione,
	    VrsFascicoloKo vrsFascicoloKo, FasFascicolo fascicoloOk)
	    throws AppGenericPersistenceException {
	RispostaControlli tmpRispostaControlli = new RispostaControlli();
	tmpRispostaControlli.setrBoolean(false);
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	CompRapportoVersFascicolo esito = rispostaWs.getCompRapportoVersFascicolo();

	try {
	    String tmpStatoSessione = null;
	    VrsSesFascicoloKo tmpSesFascicoloKo = new VrsSesFascicoloKo();
	    if (fascicoloOk != null) {
		// se sto aggiungendo una sessione fallita ad un fascicolo
		// già presente, questa è _risolta_ per definizione
		tmpSesFascicoloKo.setFasFascicolo(fascicoloOk);
		tmpStatoSessione = CostantiDB.StatoVrsFascicoloKo.RISOLTO.toString();
	    } else {
		// altrimenti è _non verificata_
		tmpSesFascicoloKo.setVrsFascicoloKo(vrsFascicoloKo);
		tmpStatoSessione = CostantiDB.StatoVrsFascicoloKo.NON_VERIFICATO.toString();
	    }
	    tmpSesFascicoloKo.setDecTipoFascicolo(
		    entityManager.find(DecTipoFascicolo.class, svf.getIdTipoFascicolo()));
	    tmpSesFascicoloKo.setTsIniSes(sessione.getTmApertura().toLocalDateTime());
	    tmpSesFascicoloKo.setTsFineSes(sessione.getTmChiusura().toLocalDateTime());
	    // è possibile avere una sessione fallita anche senza che sia indicata
	    // la versione del versamento
	    if (StringUtils.isEmpty(sessione.getVersioneWS())) {
		tmpSesFascicoloKo.setCdVersioneWs("N/A");
	    } else {
		tmpSesFascicoloKo.setCdVersioneWs(sessione.getVersioneWS());
	    }
	    // salvo l'utente solo se identificato, altrimenti il campo può essere NULL
	    // anche in questo caso è possibile avere una versione fallita senza aver
	    // indentificato
	    // l'utente versante
	    if (versamento.getUtente() != null) {
		tmpSesFascicoloKo.setIamUser(
			entityManager.find(IamUser.class, versamento.getUtente().getIdUtente()));
	    }
	    // salvo il nome del server/istanza nel cluster che sta salvando i dati e ha
	    // gestito il versamento
	    tmpSesFascicoloKo.setCdIndServer(appServerInstance.getName());
	    // salvo l'indirizzo IP del sistema che ha effettuato la richiesta di
	    // versamento/aggiunta
	    tmpSesFascicoloKo.setCdIndIpClient(sessione.getIpChiamante());
	    tmpSesFascicoloKo.setDecErrSacer(
		    messaggiWSHelper.caricaDecErrore(esito.getEsitoGenerale().getCodiceErrore()));
	    tmpSesFascicoloKo.setDsErrPrinc(LogSessioneUtils
		    .getDsErrAtMaxLen(esito.getEsitoGenerale().getMessaggioErrore()));
	    tmpSesFascicoloKo.setTiStatoSes(tmpStatoSessione);
	    tmpSesFascicoloKo.setOrgStrut(entityManager.find(OrgStrut.class, svf.getIdStruttura()));
	    tmpSesFascicoloKo
		    .setAaFascicolo(new BigDecimal(svf.getChiaveNonVerificata().getAnno()));
	    entityManager.persist(tmpSesFascicoloKo);
	    tmpRispostaControlli.setrObject(tmpSesFascicoloKo);
	    entityManager.flush();

	    // se sto aggiungendo una sessione ad un fascicolo KO, lo devo aggiornare
	    // inoltre devo aggiornare - forse - la tabella MON_CONTA_FASCICOLI_KO.
	    // la stessa cosa la dovrò fare quando creo un nuovo fascicolo per il
	    // quale esistono sessioni fallite
	    if (vrsFascicoloKo != null) {
		// init
		vrsFascicoloKo.setVrsSesFascicoloKos(new ArrayList<>());

		if (convert(vrsFascicoloKo.getTsIniLastSes())
			.before(convert(sessione.getTmApertura().toLocalDate()))) {
		    aggiornaConteggioMonContaFasKo(vrsFascicoloKo);
		}
		//
		VrsVUpdFascicoloKo updFasKo = entityManager.find(VrsVUpdFascicoloKo.class,
			vrsFascicoloKo.getIdFascicoloKo());
		if (updFasKo != null) {
		    if (updFasKo.getIdErrSacerPrinc() != null) {
			vrsFascicoloKo.setDecErrSacer(entityManager.find(DecErrSacer.class,
				updFasKo.getIdErrSacerPrinc().longValue()));
			vrsFascicoloKo.setDsErrPrinc(
				LogSessioneUtils.getDsErrAtMaxLen(updFasKo.getDsErrPrinc()));
		    } else {
			vrsFascicoloKo.setDecErrSacer(null);
			vrsFascicoloKo.setDsErrPrinc(
				LogSessioneUtils.getDsErrAtMaxLen(CD_DS_ERR_DIVERSI));
		    }
		    vrsFascicoloKo.setTiStatoFascicoloKo(updFasKo.getTiStatoFascicoloKo());
		    // questo ha senso solo se questa è la prima sessione fallita per il fascicolo
		    vrsFascicoloKo.setIdSesFascicoloKoFirst(updFasKo.getIdSesFascicoloKoFirst());
		}
		vrsFascicoloKo.setDecTipoFascicolo(entityManager.find(DecTipoFascicolo.class,
			versamento.getStrutturaComponenti().getIdTipoFascicolo()));
		vrsFascicoloKo.setTsIniLastSes(sessione.getTmApertura().toLocalDateTime());
		vrsFascicoloKo.setIdSesFascicoloKoLast(
			new BigDecimal(tmpSesFascicoloKo.getIdSesFascicoloKo()));
		// aggiungo la sessione appena creata al fascicolo KO
		vrsFascicoloKo.getVrsSesFascicoloKos().add(tmpSesFascicoloKo);
	    } else if (fascicoloOk != null) {
		// aggiunto controllo (fascicoloOk != null) solo per sicurezza ma, vedi logica
		// chiamante, questo oggetto
		// NON sarà mai null
		// altrimenti aggiorno il flag di errore del fascicolo OK,
		// che forse è già alzato, ma male non fa...
		fascicoloOk.setFlSesFascicoloKo("1");
	    }

	    entityManager.flush();
	    tmpRispostaControlli.setrBoolean(true);

	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio  dati xml sessione KO: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return tmpRispostaControlli;
    }

    private void aggiornaConteggioMonContaFasKo(VrsFascicoloKo fascicoloKo) {
	List<MonContaFascicoliKo> mcfks;

	final String queryStr = "select ud " + "from MonContaFascicoliKo ud "
		+ "join ud.orgStrut org " + "join ud.decTipoFascicolo tf "
		+ "where org.idStrut = :idStrutIn " + " and ud.dtRifConta = :dtRifConta "
		+ "and ud.aaFascicolo = :aaFascicolo "
		+ " and ud.tiStatoFascicoloKo = :tiStatoFascicoloKo "
		+ "and tf.idTipoFascicolo = :idTipoFascicolo ";
	Query query = entityManager.createQuery(queryStr, MonContaFascicoliKo.class);
	query.setParameter("idStrutIn", fascicoloKo.getOrgStrut().getIdStrut());
	query.setParameter("dtRifConta", fascicoloKo.getTsIniLastSes().toLocalDate());
	query.setParameter("aaFascicolo", fascicoloKo.getAaFascicolo());
	query.setParameter("tiStatoFascicoloKo", fascicoloKo.getTiStatoFascicoloKo());
	query.setParameter("idTipoFascicolo",
		fascicoloKo.getDecTipoFascicolo().getIdTipoFascicolo());

	mcfks = query.getResultList();
	if (!mcfks.isEmpty()) {
	    mcfks.get(0).setNiFascicoliKo(mcfks.get(0).getNiFascicoliKo().subtract(BigDecimal.ONE));
	    entityManager.flush();
	}
    }

    @Override
    public void scriviXmlFascicoloKo(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
	    BlockingFakeSession sessione, VrsSesFascicoloKo sessFascicoloKo,
	    BackendStorage backendMetadata, Map<String, String> sipBlob)
	    throws AppGenericPersistenceException {
	// salvo xml di request e response sessione fallita
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	CompRapportoVersFascicolo esito = rispostaWs.getCompRapportoVersFascicolo();

	try {
	    // init
	    sessFascicoloKo.setVrsXmlSesFascicoloKos(new ArrayList<>());
	    //
	    VrsXmlSesFascicoloKo tmpXmlSesFascicoloErr = new VrsXmlSesFascicoloKo();

	    tmpXmlSesFascicoloErr.setVrsSesFascicoloKo(sessFascicoloKo);
	    tmpXmlSesFascicoloErr.setTiXml(CostantiDB.TipiXmlDati.RICHIESTA.toString());
	    tmpXmlSesFascicoloErr.setCdVersioneXml(svf.getVersioneIndiceSipNonVerificata());
	    // MEV#30786
	    String blXml = versamento.getDatiXml().length() == 0 ? "--" : versamento.getDatiXml();
	    if (backendMetadata.isDataBase()) {
		tmpXmlSesFascicoloErr.setBlXml(blXml);
	    } else {
		sipBlob.put(CostantiDB.TipiXmlDati.RICHIESTA.toString(), blXml);
	    }
	    // end MEV#30786
	    tmpXmlSesFascicoloErr.setIdStrut(new BigDecimal(svf.getIdStruttura()));
	    tmpXmlSesFascicoloErr.setDtRegXmlSesKo(sessione.getTmApertura().toLocalDate());

	    entityManager.persist(tmpXmlSesFascicoloErr);
	    sessFascicoloKo.getVrsXmlSesFascicoloKos().add(tmpXmlSesFascicoloErr);

	    String xmlesito = this.generaRapportoVersamento(rispostaWs);

	    tmpXmlSesFascicoloErr = new VrsXmlSesFascicoloKo();
	    tmpXmlSesFascicoloErr.setVrsSesFascicoloKo(sessFascicoloKo);
	    tmpXmlSesFascicoloErr.setTiXml(CostantiDB.TipiXmlDati.RISPOSTA.toString());
	    tmpXmlSesFascicoloErr.setCdVersioneXml(esito.getVersioneRapportoVersamento());
	    // MEV#30786
	    if (backendMetadata.isDataBase()) {
		tmpXmlSesFascicoloErr.setBlXml(xmlesito);
	    } else {
		sipBlob.put(CostantiDB.TipiXmlDati.RISPOSTA.toString(), xmlesito);
	    }
	    // end MEV#30786
	    tmpXmlSesFascicoloErr.setIdStrut(new BigDecimal(svf.getIdStruttura()));
	    tmpXmlSesFascicoloErr.setDtRegXmlSesKo(sessione.getTmApertura().toLocalDate());

	    entityManager.persist(tmpXmlSesFascicoloErr);
	    sessFascicoloKo.getVrsXmlSesFascicoloKos().add(tmpXmlSesFascicoloErr);

	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio  dati xml sessione fallita: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public void scriviErroriFascicoloKo(VersFascicoloExt versamento,
	    VrsSesFascicoloKo sessFascicoloKo) throws AppGenericPersistenceException {
	int progErrore = 1;
	try {
	    // init
	    sessFascicoloKo.setVrsErrSesFascicoloKos(new ArrayList<>());

	    VrsErrSesFascicoloKo tmpErrSessioneVers;
	    for (VoceDiErrore tmpVoceDiErrore : versamento.getErroriTrovati()) {
		tmpErrSessioneVers = new VrsErrSesFascicoloKo();
		tmpErrSessioneVers.setVrsSesFascicoloKo(sessFascicoloKo);
		//
		tmpErrSessioneVers.setPgErr(new BigDecimal(progErrore));
		if (tmpVoceDiErrore.getSeverity() == IRispostaWS.SeverityEnum.ERROR) {
		    tmpErrSessioneVers.setTiErr(TIPO_ERR_FATALE);
		} else {
		    tmpErrSessioneVers.setTiErr(TIPO_ERR_WARNING);
		}

		tmpErrSessioneVers.setDecErrSacer(
			messaggiWSHelper.caricaDecErrore(tmpVoceDiErrore.getErrorCode()));
		tmpErrSessioneVers.setDsErr(
			LogSessioneUtils.getDsErrAtMaxLen(tmpVoceDiErrore.getErrorMessage()));
		tmpErrSessioneVers
			.setFlErrPrinc(tmpVoceDiErrore.isElementoPrincipale() ? CostantiDB.Flag.TRUE
				: CostantiDB.Flag.FALSE);
		//
		entityManager.persist(tmpErrSessioneVers);
		sessFascicoloKo.getVrsErrSesFascicoloKos().add(tmpErrSessioneVers);
		progErrore++;
	    }
	    entityManager.flush();
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio errori sessione fallita: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    public String generaRapportoVersamento(RispostaWSFascicolo rispostaWs) throws JAXBException {
	// questo metodo viene invocato sia internamente alla classe (_non_ attraverso
	// il
	// container) che dall'EJB di salvataggio fascicolo. Dal momento che la
	// transazione
	// è sempre REQUIRED, non c'è molta differenza agli effetti pratici.
	StringWriter tmpStreamWriter = new StringWriter();
	CompRapportoVersFascicolo esito = rispostaWs.getCompRapportoVersFascicolo();
	JAXBContext tmpcontesto = xmlFascCache.getVersRespFascicoloCtx();
	Marshaller tmpMarshaller = tmpcontesto.createMarshaller();
	tmpMarshaller.marshal(esito.produciEsitoFascicolo(), tmpStreamWriter);

	return tmpStreamWriter.toString();
    }
}
