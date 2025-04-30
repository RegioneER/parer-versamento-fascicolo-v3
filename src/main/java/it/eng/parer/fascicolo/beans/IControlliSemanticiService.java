/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.util.Date;

import it.eng.parer.fascicolo.beans.dto.base.CSChiave;
import it.eng.parer.fascicolo.beans.dto.base.CSVersatore;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.utils.Costanti.TipiGestioneUDAnnullate;
import jakarta.validation.constraints.NotNull;

public interface IControlliSemanticiService {

    RispostaControlli caricaDefaultDaDB(
            @NotNull(message = "IControlliSemanticiService.caricaDefaultDaDB: tipoPar non inizializzato") String tipoPar);

    RispostaControlli checkIdStrut(
            @NotNull(message = "IControlliSemanticiService.checkIdStrut: vers non inizializzato") CSVersatore vers,
            @NotNull(message = "IControlliSemanticiService.checkIdStrut: dataVersamento non inizializzato") Date dataVersamento);

    RispostaControlli checkChiave(
            @NotNull(message = "IControlliSemanticiService.checkChiave: key non inizializzato") CSChiave key,
            long idStruttura,
            @NotNull(message = "IControlliSemanticiService.checkChiave: tguda non inizializzato") TipiGestioneUDAnnullate tguda);

}