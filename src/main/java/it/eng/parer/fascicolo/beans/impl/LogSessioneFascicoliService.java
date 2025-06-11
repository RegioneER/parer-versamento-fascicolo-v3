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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.fascicolo.beans.ILogSessioneFascicoliService;
import it.eng.parer.fascicolo.beans.IObjectStorageService;
import it.eng.parer.fascicolo.beans.IlogSessioneFascicoliDao;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageResource;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloErr;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloKo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class LogSessioneFascicoliService implements ILogSessioneFascicoliService {

    private static final Logger log = LoggerFactory.getLogger(LogSessioneFascicoliService.class);
    //
    @Inject
    IlogSessioneFascicoliDao logSessioneFascicoliDao;

    @Inject
    EntityManager entityManager;

    @Inject
    IObjectStorageService objectStorageService;

    @Override
    @Transactional(value = TxType.REQUIRES_NEW, rollbackOn = {
	    AppGenericPersistenceException.class })
    public void registraSessioneErrata(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
	    BlockingFakeSession sessione) throws AppGenericPersistenceException {
	// nota l'errore critico di persistenza viene contrassegnato con la lettera P
	// per dare la possibilità all'eventuale chiamante di ripetere il tentativo
	// quando possibile (non è infatti un errore "definitivo" dato dall'input, ma
	// bensì
	// un errore interno provocato da problemi al database)
	RispostaControlli rcSessErr = null;
	VrsSesFascicoloErr tmpSesFascicoloErr = null;
	// MEV#30786
	BackendStorage backendMetadata = null;
	Map<String, String> sipBlob = new HashMap<>();
	// end MEV#30786
	try {
	    backendMetadata = objectStorageService.lookupBackendVrsSessioniErrKo();

	    // salvo sessione errata:
	    rcSessErr = logSessioneFascicoliDao.scriviFascicoloErr(rispostaWs, versamento,
		    sessione);

	    // salvo gli xml di request & response
	    tmpSesFascicoloErr = (VrsSesFascicoloErr) rcSessErr.getrObject();
	    //
	    logSessioneFascicoliDao.scriviXmlFascicoloErr(rispostaWs, versamento,
		    tmpSesFascicoloErr, backendMetadata, sipBlob);
	    //
	    // MEV#30786
	    /*
	     * Se backendMetadata di tipo O.S. si effettua il salvataggio (con link su apposita
	     * entity)
	     */
	    if (backendMetadata.isObjectStorage()) {
		ObjectStorageResource res = objectStorageService.createSipInSessioniErr(
			backendMetadata.getBackendName(), sipBlob,
			tmpSesFascicoloErr.getIdSesFascicoloErr(), getIdStrut(versamento));
		log.debug("Salvati i SIP nel bucket {} con chiave {} ", res.getBucket(),
			res.getKey());
	    }
	    // end MEV#30786
	    entityManager.flush();
	} catch (AppGenericPersistenceException agpex) {
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(agpex.getCodErr(), agpex.getDsErr());
	    versamento.aggiungErroreFatale(
		    rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale());
	    throw agpex;
	} catch (Exception ex) {
	    final String msg = "Errore interno nella fase di salvataggio sessione errata del fascicolo: ";
	    // l'errore di persistenza viene aggiunto alla pila
	    // di errori esistenti e in seguito serializzato nell'xml
	    // di risposta. Inoltre tenterò di salvare una sessione errata con questi stessi
	    // dati.
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.ERR_666P,
		    msg + ExceptionUtils.getRootCauseMessage(ex));
	    versamento.aggiungErroreFatale(
		    rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale());
	    // si scatena una AppGenericPersistenceException contenente
	    // l'eccezione runtime non gestita dalla catch precedente
	    throw new AppGenericPersistenceException(ex,
		    msg + ExceptionUtils.getRootCauseMessage(ex));
	}
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW, rollbackOn = {
	    AppGenericPersistenceException.class })
    public void registraSessioneFallita(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
	    BlockingFakeSession sessione) throws AppGenericPersistenceException {
	// nota l'errore critico di persistenza viene contrassegnato con la lettera P
	// per dare la possibilità all'eventuale chiamante di ripetere il tentativo
	// quando possibile (non è infatti un errore "definitivo" dato dall'input, ma
	// bensì
	// un errore interno provocato da problemi al database)
	//
	// gli errori di persistenza in questa fase vengono aggiunti alla pila
	// di errori esistenti e in seguito serializzati nell'xml
	// di risposta. Inoltre tenterò di salvare una sessione errata con questi stessi
	// dati.
	RispostaControlli rcSessFailed = null;
	FasFascicolo tmpFasFascicolo = null;
	VrsFascicoloKo tmpFascicoloKo = null;
	VrsSesFascicoloKo tmpSesFascicoloKo = null;
	// MEV#30786
	BackendStorage backendMetadata = null;
	Map<String, String> sipBlob = new HashMap<>();
	// end MEV#30786
	try {
	    backendMetadata = objectStorageService.lookupBackendVrsSessioniErrKo();

	    // se sono in errore di fascicolo doppio -> cerco il fascicolo originale
	    if (rispostaWs.isErroreElementoDoppio()) {
		tmpFasFascicolo = entityManager.find(FasFascicolo.class,
			rispostaWs.getIdElementoDoppio());
	    } else {
		// altrimenti -> cerco se esiste una precedente registrazione fallita dello
		// stesso fascicolo e se esiste, lo blocco in modo esclusivo: lo devo modificare
		rcSessFailed = logSessioneFascicoliDao.cercaFascicoloKo(versamento);

		if (rcSessFailed.getrObject() != null) {
		    tmpFascicoloKo = (VrsFascicoloKo) rcSessFailed.getrObject();
		} else {
		    // se non ho fascicoli buoni o cattivi già creati -> creo fascicolo fallito
		    rcSessFailed = logSessioneFascicoliDao.scriviFascicoloKo(rispostaWs, versamento,
			    sessione);
		    //
		    tmpFascicoloKo = (VrsFascicoloKo) rcSessFailed.getrObject();
		}
	    }

	    // salvo sessione fallita
	    rcSessFailed = logSessioneFascicoliDao.scriviSessioneFascicoloKo(rispostaWs, versamento,
		    sessione, tmpFascicoloKo, tmpFasFascicolo);
	    //
	    tmpSesFascicoloKo = (VrsSesFascicoloKo) rcSessFailed.getrObject();
	    // salvo xml di request & response
	    logSessioneFascicoliDao.scriviXmlFascicoloKo(rispostaWs, versamento, sessione,
		    tmpSesFascicoloKo, backendMetadata, sipBlob);

	    // salvo i warning e gli errori ulteriori
	    logSessioneFascicoliDao.scriviErroriFascicoloKo(versamento, tmpSesFascicoloKo);
	    //
	    // MEV#30786
	    /*
	     * Se backendMetadata di tipo O.S. si effettua il salvataggio (con link su apposita
	     * entity)
	     */
	    if (backendMetadata.isObjectStorage()) {
		ObjectStorageResource res = objectStorageService.createSipInSessioniKo(
			backendMetadata.getBackendName(), sipBlob,
			tmpSesFascicoloKo.getIdSesFascicoloKo(), getIdStrut(versamento));
		log.debug("Salvati i SIP nel bucket {} con chiave {} ", res.getBucket(),
			res.getKey());
	    }
	    // end MEV#30786
	    entityManager.flush();
	} catch (AppGenericPersistenceException agpex) {
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(agpex.getCodErr(), agpex.getDsErr());
	    versamento.aggiungErroreFatale(
		    rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale());
	    throw agpex;
	} catch (Exception ex) {
	    final String msg = "Errore interno nella fase di salvataggio sessione fallita del fascicolo: ";
	    // l'errore di persistenza viene aggiunto alla pila
	    // di errori esistenti e in seguito serializzato nell'xml
	    // di risposta. Inoltre tenterò di salvare una sessione errata con questi stessi
	    // dati.
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.ERR_666P,
		    msg + ExceptionUtils.getRootCauseMessage(ex));
	    versamento.aggiungErroreFatale(
		    rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale());
	    // si scatena una AppGenericPersistenceException contenente
	    // l'eccezione runtime non gestita dalla catch precedente
	    throw new AppGenericPersistenceException(ex,
		    msg + ExceptionUtils.getRootCauseMessage(ex));
	}
    }

    private BigDecimal getIdStrut(VersFascicoloExt versamento) {
	if (versamento.getStrutturaComponenti() != null
		&& versamento.getStrutturaComponenti().getIdStruttura() != 0) {
	    return BigDecimal.valueOf(versamento.getStrutturaComponenti().getIdStruttura());
	}
	return null;
    }
}
