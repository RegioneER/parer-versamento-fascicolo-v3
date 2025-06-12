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
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import jakarta.validation.constraints.NotNull;

public interface IElencoVersamentoFascicoliService {

    void scriviElvFascDaElabElenco(
	    @NotNull(message = "IElencoVersamentoFascicoliService.scriviElvFascDaElabElenco: versamento non inizializzato") VersFascicoloExt versamento,
	    FasFascicolo fascicolo) throws AppGenericPersistenceException;

    void scriviStatoConservFascicolo(
	    @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoConservFascicolo: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoConservFascicolo: versamento non inizializzato") VersFascicoloExt versamento,
	    BlockingFakeSession sessione, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException;

    void scriviStatoFascicoloElenco(
	    @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoFascicoloElenco: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IElencoVersamentoFascicoliService.scriviStatoFascicoloElenco: versamento non inizializzato") VersFascicoloExt versamento,
	    BlockingFakeSession sessione, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException;

}
