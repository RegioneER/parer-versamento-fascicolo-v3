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
