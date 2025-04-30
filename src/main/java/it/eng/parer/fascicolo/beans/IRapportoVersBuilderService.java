/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import jakarta.validation.constraints.NotNull;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;

public interface IRapportoVersBuilderService {

    /**
     * Canonicalizzazione dell'XML SIP recuperato da sessione.getDatiDaSalvareIndiceSip (viene restituito l'xml secondo
     * transformazione e secondo il "formato" comunicato dal client)
     *
     * @param sessione
     *            di tipo {@link BlockingFakeSession}
     *
     * @return RispostaControlli con indice sip canonicalizzato
     */
    RispostaControlli canonicalizzaDaSalvareIndiceSip(
            @NotNull(message = "IRapportoVersBuilderService.canonicalizzaDaSalvareIndiceSip: sessione non inizializzato") BlockingFakeSession sessione);

}