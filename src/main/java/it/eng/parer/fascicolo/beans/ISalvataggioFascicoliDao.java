/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.util.Map;

import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import jakarta.validation.constraints.NotNull;

public interface ISalvataggioFascicoliDao {

    RispostaControlli scriviFascicolo(
            @NotNull(message = "ISalvataggioFascicoliDao.scriviFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviFascicolo: sessione non inizializzato") BlockingFakeSession sessione,
            VrsFascicoloKo fascicoloKo) throws AppGenericPersistenceException;

    /*
     * Soggetti ed eventi da profilo GENERALE / NORMATIVO
     */
    void scriviSogggetti(
            @NotNull(message = "ISalvataggioFascicoliDao.scriviSogggetti: versamento non inizializzato") VersFascicoloExt versamento,
            FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void scriviRequestResponseFascicolo(
            @NotNull(message = "ISalvataggioFascicoliDao.scriviRequestResponseFascicolo: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviRequestResponseFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviRequestResponseFascicolo: sessione non inizializzato") BlockingFakeSession sessione,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviRequestResponseFascicolo: backendMetadata non inizializzato") BackendStorage backendMetadata,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviRequestResponseFascicolo: sipBlob non inizializzato") Map<String, String> sipBlob,
            FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void scriviProfiliXMLFascicolo(
            @NotNull(message = "ISalvataggioFascicoliDao.scriviProfiliXMLFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviProfiliXMLFascicolo: sessione non inizializzato") BlockingFakeSession sessione,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviProfiliXMLFascicolo: backendMetadata non inizializzato") BackendStorage backendMetadata,
            @NotNull(message = "ISalvataggioFascicoliDao.scriviProfiliXMLFascicolo: profBlob non inizializzato") Map<String, String> profBlob,
            FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void scriviUnitaDocFascicolo(
            @NotNull(message = "ISalvataggioFascicoliDao.scriviUnitaDocFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void scriviLinkFascicolo(
            @NotNull(message = "ISalvataggioFascicoliDao.scriviLinkFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            FasFascicolo tmpFasFascicolo) throws AppGenericPersistenceException;

    void scriviWarningFascicolo(
            @NotNull(message = "ISalvataggioFascicoliDao.scriviWarningFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void salvaWarningAATipoFascicolo(
            @NotNull(message = "ISalvataggioFascicoliDao.salvaWarningAATipoFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "ISalvataggioFascicoliDao.salvaWarningAATipoFascicolo: sessione non inizializzato") BlockingFakeSession sessione)
            throws AppGenericPersistenceException;

    void ereditaVersamentiKoFascicolo(FasFascicolo fascicolo,
            @NotNull(message = "ISalvataggioFascicoliDao.ereditaVersamentiKoFascicolo: fascicoloKo non inizializzato") VrsFascicoloKo fascicoloKo)
            throws AppGenericPersistenceException;

    void scriviDatiSpecGen(
            @NotNull(message = "ISalvataggioFascicoliDao.ereditaVersamentiKoFascicolo: rispostaWs non inizializzato") VersFascicoloExt versamento,
            FasFascicolo tmpFasFascicolo) throws AppGenericPersistenceException;

}