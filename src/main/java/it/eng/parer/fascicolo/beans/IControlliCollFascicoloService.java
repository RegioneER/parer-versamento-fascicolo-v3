/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.ws.xml.versfascicolorespV3.ECFascicoloType;
import jakarta.validation.constraints.NotNull;

public interface IControlliCollFascicoloService {

    boolean verificaUdFascicolo(
            @NotNull(message = "IControlliCollFascicoloService.verificaUdFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "IControlliCollFascicoloService.verificaUdFascicolo: fascicoloResp non inizializzato") ECFascicoloType fascicoloResp,
            @NotNull(message = "IControlliCollFascicoloService.verificaUdFascicolo: syncFakeSession non inizializzato") BlockingFakeSession syncFakeSession);

    /*
     * Restituisce una lista (nel peggiore dei casi vuota) di fascicoli da inserire (non controllati)
     */
    RispostaControlli buildCollegamentiFascicolo(
            @NotNull(message = "IControlliCollFascicoloService.buildCollegamentiFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "IControlliCollFascicoloService.buildCollegamentiFascicolo: versamento non inizializzato") ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo);
}