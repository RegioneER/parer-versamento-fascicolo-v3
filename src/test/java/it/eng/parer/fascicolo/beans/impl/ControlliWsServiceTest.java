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
        final RispostaControlli rispostaControlli = service.checkCredenziali("admin_generale", "password", "127.0.0.1");
        Assertions.assertTrue(rispostaControlli.isrBoolean());
        Assertions.assertNull(rispostaControlli.getCodErr());
    }

    @Test
    void checkCredenziali_wrongUsername() {
        final RispostaControlli rispostaControlli = service.checkCredenziali("non_esiste_username", "password",
                "127.0.0.1");
        Assertions.assertFalse(rispostaControlli.isrBoolean());
        Assertions.assertEquals(FAS_CONFIG_002_003, rispostaControlli.getCodErr());
    }

    @Test
    void checkCredenziali_wrongPassword() {
        final RispostaControlli rispostaControlli = service.checkCredenziali("admin_generale", "wrong_password",
                "127.0.0.1");
        Assertions.assertFalse(rispostaControlli.isrBoolean());
        Assertions.assertEquals(FAS_CONFIG_002_003, rispostaControlli.getCodErr());
    }
}