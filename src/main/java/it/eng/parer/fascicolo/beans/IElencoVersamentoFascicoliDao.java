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

import it.eng.parer.fascicolo.jpa.entity.ElvFascDaElabElenco;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoConservFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoFascicoloElenco;
import it.eng.parer.fascicolo.jpa.entity.constraint.ElvFascDaElabElenco.TiStatoFascDaElab;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoConservFascicolo.TiStatoConservazione;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoFascicoloElenco.TiStatoFascElenco;
import jakarta.validation.constraints.NotNull;

public interface IElencoVersamentoFascicoliDao {

    ElvFascDaElabElenco insertFascicoloOnCodaDaElab(
	    @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") FasFascicolo fascicolo,
	    long idTipoFasc,
	    @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: status non inizializzato") TiStatoFascDaElab status);

    FasStatoConservFascicolo insertFascicoloOnStatoCons(
	    @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") FasFascicolo fascicolo,
	    @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") TiStatoConservazione status);

    FasStatoFascicoloElenco insertFascicoloOnStatoElenco(
	    @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") FasFascicolo fascicolo,
	    @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") TiStatoFascElenco status);

}
