/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import jakarta.validation.constraints.NotNull;

public interface ILogSessioneFascicoliService {

    void registraSessioneErrata(
            @NotNull(message = "ILogSessioneFascicoliService.registraSessioneErrata: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "ILogSessioneFascicoliService.registraSessioneErrata: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "ILogSessioneFascicoliService.registraSessioneErrata: sessione non inizializzato") BlockingFakeSession sessione)
            throws AppGenericPersistenceException;

    void registraSessioneFallita(
            @NotNull(message = "ILogSessioneFascicoliService.registraSessioneFallita: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "ILogSessioneFascicoliService.registraSessioneFallita: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "ILogSessioneFascicoliService.registraSessioneFallita: sessione non inizializzato") BlockingFakeSession sessione)
            throws AppGenericPersistenceException;

}