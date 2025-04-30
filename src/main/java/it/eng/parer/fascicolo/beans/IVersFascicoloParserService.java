/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import jakarta.validation.constraints.NotNull;

public interface IVersFascicoloParserService {

    void parseXML(
            @NotNull(message = "IVersFascicoloParserService.parseXML: sessione non inizializzato") BlockingFakeSession sessione,
            @NotNull(message = "IVersFascicoloParserService.parseXML: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "IVersFascicoloParserService.parseXML: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs);

}