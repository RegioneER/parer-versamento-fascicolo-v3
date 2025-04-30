/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.ws.xml.versfascicolorespV3.ECFascicoloType;
import jakarta.validation.constraints.NotNull;

public interface IControlliProfiliFascicoloService {

    boolean verificaProfiloArchivistico(
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloArchivistico: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloGenerale: fascicoloResp non inizializzato") ECFascicoloType fascicoloResp,
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloArchivistico: syncFakeSession non inizializzato") BlockingFakeSession syncFakeSession);

    boolean verificaProfiloGenerale(
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloGenerale: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloGenerale: fascicoloResp non inizializzato") ECFascicoloType fascicoloResp,
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloGenerale: syncFakeSession non inizializzato") BlockingFakeSession syncFakeSession);

    boolean verificaProfiloSpecifico(
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloSpecifico: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloSpecifico: syncFakeSession non inizializzato") BlockingFakeSession syncFakeSession);

    boolean verificaProfiloNormativo(
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloNormativo: versamento non inizializzato") VersFascicoloExt versamento,
            @NotNull(message = "IControlliProfiliFascicoloService.verificaProfiloNormativo: syncFakeSession non inizializzato") BlockingFakeSession syncFakeSession);

}