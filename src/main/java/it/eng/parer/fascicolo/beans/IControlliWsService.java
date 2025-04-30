/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.util.Map;

import it.eng.parer.fascicolo.beans.dto.base.IWSDesc;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.security.User;
import jakarta.validation.constraints.NotNull;

public interface IControlliWsService {

    RispostaControlli checkVersione(String versione,
            @NotNull(message = "IControlliWsService.checkVersione: versioniWsKey non inizializzato") String versioniWsKey,
            Map<String, String> xmlDefaults);

    RispostaControlli checkCredenziali(String loginName, String password,
            @NotNull(message = "IControlliWsService.checkCredenziali: versione non inizializzato") String indirizzoIP);

    RispostaControlli checkAuthWS(
            @NotNull(message = "IControlliWsService.checkAuthWS: utente non inizializzato") User utente,
            @NotNull(message = "IControlliWsService.checkAuthWS: descrizione non inizializzato") IWSDesc descrizione);

    RispostaControlli checkAuthWSNoOrg(
            @NotNull(message = "IControlliWsService.checkAuthWSNoOrg: utente non inizializzato") User utente,
            @NotNull(message = "IControlliWsService.checkAuthWSNoOrg: descrizione non inizializzato") IWSDesc descrizione);

    RispostaControlli loadWsVersions(
            @NotNull(message = "IControlliWsService.loadWsVersions: descrizione non inizializzato") IWSDesc descrizione);

}