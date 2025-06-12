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

import static it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle.FAS_CONFIG_002_003;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.eng.parer.fascicolo.beans.IControlliWsService;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import jakarta.inject.Inject;

@QuarkusTest
class ControlliWsServiceTest {

    @Inject
    IControlliWsService service;

    @Test
    void checkCredenziali_ok() {
	final RispostaControlli rispostaControlli = service.checkCredenziali("admin_generale",
		"password", "127.0.0.1");
	Assertions.assertTrue(rispostaControlli.isrBoolean());
	Assertions.assertNull(rispostaControlli.getCodErr());
    }

    @Test
    void checkCredenziali_wrongUsername() {
	final RispostaControlli rispostaControlli = service.checkCredenziali("non_esiste_username",
		"password", "127.0.0.1");
	Assertions.assertFalse(rispostaControlli.isrBoolean());
	Assertions.assertEquals(FAS_CONFIG_002_003, rispostaControlli.getCodErr());
    }

    @Test
    void checkCredenziali_wrongPassword() {
	final RispostaControlli rispostaControlli = service.checkCredenziali("admin_generale",
		"wrong_password", "127.0.0.1");
	Assertions.assertFalse(rispostaControlli.isrBoolean());
	Assertions.assertEquals(FAS_CONFIG_002_003, rispostaControlli.getCodErr());
    }
}
