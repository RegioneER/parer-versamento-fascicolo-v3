/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

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
     * Restituisce una lista (nel peggiore dei casi vuota) di fascicoli da inserire (non
     * controllati)
     */
    RispostaControlli buildCollegamentiFascicolo(
	    @NotNull(message = "IControlliCollFascicoloService.buildCollegamentiFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IControlliCollFascicoloService.buildCollegamentiFascicolo: versamento non inizializzato") ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo);
}
