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

package it.eng.parer.fascicolo.beans.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.eng.parer.fascicolo.beans.IElencoVersamentoFascicoliService;
import jakarta.inject.Inject;

@QuarkusTest
class ElencoVersamentoFascicoliServiceTest {

    @Inject
    IElencoVersamentoFascicoliService service;

    @Test
    void wip() {
	assertTrue(true);
    }
    /*
     * @Test void scriviElvFascDaElabElenco() { service.scriviElvFascDaElabElenco(new
     * VersFascicoloExt(),new FasFascicolo()); }
     */

    /*
     * @Test void scriviStatoConservFascicolo() { }
     *
     * @Test void scriviStatoFascicoloElenco() { }
     */
}
