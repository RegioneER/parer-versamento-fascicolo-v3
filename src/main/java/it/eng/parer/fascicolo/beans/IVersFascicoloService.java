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
