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
     * @Test void scriviElvFascDaElabElenco() { service.scriviElvFascDaElabElenco(new VersFascicoloExt(),new
     * FasFascicolo()); }
     */

    /*
     * @Test void scriviStatoConservFascicolo() { }
     *
     * @Test void scriviStatoFascicoloElenco() { }
     */
}