/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.utils.AvanzamentoWs;
import jakarta.validation.constraints.NotNull;

public interface IVersFascicoloService {

    AvanzamentoWs init(
            @NotNull(message = "IVersFascicoloService.init: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "IVersFascicoloService.init: versamento non inizializzato") VersFascicoloExt versamento);

    void verificaVersione(String versione,
            @NotNull(message = "IVersFascicoloService.verificaVersione: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "IVersFascicoloService.verificaVersione: versamento non inizializzato") VersFascicoloExt versamento);

    void verificaCredenziali(String loginName, String password, String indirizzoIp,
            @NotNull(message = "IVersFascicoloService.verificaCredenziali: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "IVersFascicoloService.verificaCredenziali: versamento non inizializzato") VersFascicoloExt versamento);

    void parseXML(
            @NotNull(message = "IVersFascicoloService.parseXML: sessione non inizializzato") BlockingFakeSession sessione,
            @NotNull(message = "IVersFascicoloService.parseXML: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "IVersFascicoloService.parseXML: versamento non inizializzato") VersFascicoloExt versamento);

    void salvaTutto(
            @NotNull(message = "IVersFascicoloService.salvaTutto: sessione non inizializzato") BlockingFakeSession sessione,
            @NotNull(message = "IVersFascicoloService.salvaTutto: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
            @NotNull(message = "IVersFascicoloService.salvaTutto: versamento non inizializzato") VersFascicoloExt versamento);

}