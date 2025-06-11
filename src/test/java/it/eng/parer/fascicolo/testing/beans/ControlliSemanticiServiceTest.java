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
package it.eng.parer.fascicolo.testing.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.eng.parer.fascicolo.beans.IControlliSemanticiService;
import it.eng.parer.fascicolo.beans.utils.ParametroApplDB.TipoParametroAppl;
import jakarta.inject.Inject;

@QuarkusTest
public class ControlliSemanticiServiceTest {

    @Inject
    IControlliSemanticiService service;

    @Test
    public void testCaricaDefaultDaDB() {
	Assertions
		.assertTrue(service.caricaDefaultDaDB(TipoParametroAppl.VERSIONI_WS).isrBoolean());
    }

}
