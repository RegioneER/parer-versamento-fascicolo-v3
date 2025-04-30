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