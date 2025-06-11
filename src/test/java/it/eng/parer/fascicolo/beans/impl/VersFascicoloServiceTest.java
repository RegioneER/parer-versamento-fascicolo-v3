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

import it.eng.parer.fascicolo.beans.IVersFascicoloService;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class VersFascicoloServiceTest {
    @Test
    void parseXML_emptyUser_fails() {
	IVersFascicoloService service = new VersFascicoloService();
	// il mock di RispostaWSFascicolo deve fare solo la get/set della severity
	final RispostaWSFascicolo rispostaWs = mock(RispostaWSFascicolo.class);
	doCallRealMethod().when(rispostaWs).setSeverity(any());
	when(rispostaWs.getSeverity()).thenCallRealMethod();

	final VersFascicoloExt versamento = new VersFascicoloExt();
	versamento.setLoginName("");
	service.parseXML(new BlockingFakeSession(), rispostaWs, versamento);
	Assertions.assertEquals(IRispostaWS.SeverityEnum.ERROR, rispostaWs.getSeverity());
    }
}
