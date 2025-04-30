/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import jakarta.validation.constraints.NotNull;

public interface ISalvataggioFascicoliService {

    void salvaFascicolo(
            @NotNull(message = "ISalvataggioFascicoliService.salvaFascicolo: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "ISalvataggioFascicoliService.salvaFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "ISalvataggioFascicoliService.salvaFascicolo: sessione non inizializzato") BlockingFakeSession sessione)
            throws AppGenericPersistenceException;

}