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
        Assertions.assertTrue(service.caricaDefaultDaDB(TipoParametroAppl.VERSIONI_WS).isrBoolean());
    }

}