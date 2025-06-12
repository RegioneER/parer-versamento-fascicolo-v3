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

import it.eng.parer.fascicolo.beans.IElencoVersamentoFascicoliService;
import it.eng.parer.fascicolo.beans.IObjectStorageService;
import it.eng.parer.fascicolo.beans.ISalvataggioFascicoliDao;
import it.eng.parer.fascicolo.beans.ISalvataggioFascicoliService;
import it.eng.parer.fascicolo.beans.IlogSessioneFascicoliDao;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageResource;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.utils.Costanti;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSFormat;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class SalvataggioFascicoliService implements ISalvataggioFascicoliService {
    //
    private static final Logger log = LoggerFactory.getLogger(SalvataggioFascicoliService.class);
    //
    @Inject
    ISalvataggioFascicoliDao salvataggioFascicoliDao;

    @Inject
    IlogSessioneFascicoliDao logSessioneFascicoliDao;

    @Inject
    IElencoVersamentoFascicoliService elencoVersamentoFascicoliService;

    @Inject
    EntityManager entityManager;

    @Inject
    IObjectStorageService objectStorageService;

    @Override
    @Transactional(value = TxType.REQUIRES_NEW, rollbackOn = {
	    AppGenericPersistenceException.class })
    public void salvaFascicolo(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
	    BlockingFakeSession sessione) throws AppGenericPersistenceException {
	// salva i dati del fascicolo
	// eventualmente rimuove il fascicolo KO relativo a tentativi
	// precedenti di versamento
	RispostaControlli rcSaveFasc = null;
	FasFascicolo tmpFasFascicolo = null;
	VrsFascicoloKo tmpFascicoloKo = null;
	// MEV#30786
	BackendStorage backendMetadata = null;
	Map<String, String> sipBlob = new HashMap<>();
	Map<String, String> profBlob = new HashMap<>();
	// end MEV#30786
	try {
	    backendMetadata = objectStorageService.lookupBackendByServiceName(
		    versamento.getStrutturaComponenti().getIdAATipoFasc(),
		    Costanti.WS_VERS_FASCICOLO_NOME);

	    // cerco se esiste una precedente registrazione fallita dello
	    // stesso fascicolo e se esiste, lo blocco in modo esclusivo: lo devo rimuovere
	    // e devo riallocare tutte le sue sessioni al fascicolo che sto creando
	    rcSaveFasc = logSessioneFascicoliDao.cercaFascicoloKo(versamento);

	    if (rcSaveFasc.getrObject() != null) {
		tmpFascicoloKo = (VrsFascicoloKo) rcSaveFasc.getrObject();
	    }

	    // salvo fascicolo
	    rcSaveFasc = salvataggioFascicoliDao.scriviFascicolo(versamento, sessione,
		    tmpFascicoloKo);

	    if (rcSaveFasc.getrObject() != null) {
		tmpFasFascicolo = (FasFascicolo) rcSaveFasc.getrObject();
	    }

	    // salvo soggetti coinvolti (se esistono) da profilo generale
	    salvataggioFascicoliDao.scriviSogggetti(versamento, tmpFasFascicolo);

	    // salvo request e response
	    salvataggioFascicoliDao.scriviRequestResponseFascicolo(rispostaWs, versamento, sessione,
		    backendMetadata, sipBlob, tmpFasFascicolo);

	    // salvo valori dati specifici (se presenti)
	    salvataggioFascicoliDao.scriviDatiSpecGen(versamento, tmpFasFascicolo);

	    // salvo profili archivistico e generale
	    salvataggioFascicoliDao.scriviProfiliXMLFascicolo(versamento, sessione, backendMetadata,
		    profBlob, tmpFasFascicolo);

	    // salvo unità documentarie
	    salvataggioFascicoliDao.scriviUnitaDocFascicolo(versamento, tmpFasFascicolo);

	    // salvo fascicoli collegati
	    salvataggioFascicoliDao.scriviLinkFascicolo(versamento, tmpFasFascicolo);

	    // salvo warning
	    salvataggioFascicoliDao.scriviWarningFascicolo(versamento, tmpFasFascicolo);

	    // salvo warning aa
	    if (versamento.getStrutturaComponenti().isWarningFormatoNumero()) {
		salvataggioFascicoliDao.salvaWarningAATipoFascicolo(versamento, sessione);
	    }

	    // salvo su elenco coda elaborazione
	    elencoVersamentoFascicoliService.scriviElvFascDaElabElenco(versamento, tmpFasFascicolo);

	    // salvo su elenco stato conservazione
	    elencoVersamentoFascicoliService.scriviStatoConservFascicolo(rispostaWs, versamento,
		    sessione, tmpFasFascicolo);

	    // salvo su elenco stato fascicolo
	    elencoVersamentoFascicoliService.scriviStatoFascicoloElenco(rispostaWs, versamento,
		    sessione, tmpFasFascicolo);

	    // rimuove fascicolo ko se necessario
	    if (tmpFascicoloKo != null) {
		salvataggioFascicoliDao.ereditaVersamentiKoFascicolo(tmpFasFascicolo,
			tmpFascicoloKo);
	    }
	    //
	    // MEV#30786
	    /*
	     * Se backendMetadata di tipo O.S. si effettua il salvataggio (con link su apposita
	     * entity)
	     */
	    if (backendMetadata.isObjectStorage()) {
		// MAC#37280
		// calculate normalized URN per chiave o.s.
		final String urn = MessaggiWSFormat.formattaChiaveFascicoloKeyOs(
			MessaggiWSFormat.formattaUrnPartVersatoreKeyOs(
				versamento.getStrutturaComponenti().getVersatoreNonverificato()),
			MessaggiWSFormat.formattaUrnPartFascKeyOs(
				versamento.getStrutturaComponenti().getChiaveNonVerificata()));
		// end MAC#37280
		ObjectStorageResource res = objectStorageService.createResourcesInSipFascicoli(urn,
			backendMetadata.getBackendName(), sipBlob, tmpFasFascicolo.getIdFascicolo(),
			getIdStrut(versamento));
		log.debug("Salvati i SIP nel bucket {} con chiave {} ", res.getBucket(),
			res.getKey());
		res = objectStorageService.createResourcesInMetaProfFascicoli(urn,
			backendMetadata.getBackendName(), profBlob,
			tmpFasFascicolo.getIdFascicolo(), getIdStrut(versamento));
		log.debug("Salvati i METADATI di profilo nel bucket {} con chiave {} ",
			res.getBucket(), res.getKey());
	    }
	    // end MEV#30786
	    entityManager.flush();
	} catch (AppGenericPersistenceException agpex) {
	    // si imposta l'errore del metodo che ha generato eccezione
	    // che contiene il codice di errore 666P con relativa descrizione
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsError(agpex.getCodErr(), agpex.getDsErr());
	    versamento.aggiungErroreFatale(
		    rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale());
	    throw agpex;
	} catch (Exception ex) {
	    // l'errore di persistenza viene aggiunto alla pila
	    // di errori esistenti e in seguito serializzato nell'xml
	    // di risposta. Inoltre tenterò di salvare una sessione errata con questi stessi
	    // dati.
	    rispostaWs.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	    rispostaWs.setEsitoWsErrBundle(MessaggiWSBundle.ERR_666P,
		    "Errore interno nella fase di salvataggio fascicolo: "
			    + ExceptionUtils.getRootCauseMessage(ex));
	    versamento.aggiungErroreFatale(
		    rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale());
	    // si scatena una AppGenericPersistenceException contenente
	    // l'eccezione runtime non gestita dalla catch precedente
	    throw new AppGenericPersistenceException(ex,
		    "Errore interno nella fase di salvataggio fascicolo: "
			    + ExceptionUtils.getRootCauseMessage(ex));
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
