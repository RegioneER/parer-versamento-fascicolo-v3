/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import jakarta.validation.constraints.NotNull;

public interface IElencoVersamentoFascicoliService {

    void scriviElvFascDaElabElenco(
            @NotNull(message = "IElencoVersamentoFascicoliService.scriviElvFascDaElabElenco: versamento non inizializzato") VersFascicoloExt versamento,
            FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void scriviStatoConservFascicolo(
            @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoConservFascicolo: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoConservFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            BlockingFakeSession sessione, FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void scriviStatoFascicoloElenco(
            @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoFascicoloElenco: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoFascicoloElenco: versamento non inizializzato") VersFascicoloExt versamento,
            BlockingFakeSession sessione, FasFascicolo fascicolo) throws AppGenericPersistenceException;

}