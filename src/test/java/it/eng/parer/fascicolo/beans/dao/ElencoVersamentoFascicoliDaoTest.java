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

package it.eng.parer.fascicolo.beans.dao;

import static it.eng.parer.DatabaseInit.ID_DEC_TIPO_FASCICOLO;
import static it.eng.parer.DatabaseInit.ID_FAS_FASCICOLO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.DatabaseInit;
import it.eng.parer.Profiles;
import it.eng.parer.fascicolo.beans.IElencoVersamentoFascicoliDao;
import it.eng.parer.fascicolo.jpa.entity.ElvFascDaElabElenco;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoConservFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoFascicoloElenco;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@QuarkusTest
@TestProfile(Profiles.H2.class)
class ElencoVersamentoFascicoliDaoTest {

    @Inject
    IElencoVersamentoFascicoliDao dao;
    @Inject
    EntityManager entityManager;
    @Inject
    DatabaseInit dbInit;

    @Test
    @TestTransaction
    void insertFascicoloOnCodaDaElab() {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	FasFascicolo fascicolo = entityManager.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	final it.eng.parer.fascicolo.jpa.entity.constraint.ElvFascDaElabElenco.TiStatoFascDaElab stato = it.eng.parer.fascicolo.jpa.entity.constraint.ElvFascDaElabElenco.TiStatoFascDaElab.IN_ATTESA_SCHED;
	final ElvFascDaElabElenco elencoInserito = dao.insertFascicoloOnCodaDaElab(fascicolo,
		ID_DEC_TIPO_FASCICOLO, stato);
	assertNotNull(elencoInserito);
	assertEquals(fascicolo.getIdFascicolo(), elencoInserito.getFasFascicolo().getIdFascicolo());
	assertEquals(ID_DEC_TIPO_FASCICOLO, elencoInserito.getIdTipoFascicolo().longValue());
	assertEquals(stato, elencoInserito.getTiStatoFascDaElab());
    }

    @Test
    @TestTransaction
    void insertFascicoloOnStatoCons() {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	FasFascicolo fascicolo = entityManager.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	final it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoConservFascicolo.TiStatoConservazione stato = it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoConservFascicolo.TiStatoConservazione.IN_ARCHIVIO;
	final FasStatoConservFascicolo statoInserito = dao.insertFascicoloOnStatoCons(fascicolo,
		stato);
	assertNotNull(statoInserito);
	assertEquals(fascicolo.getIdFascicolo(), statoInserito.getFasFascicolo().getIdFascicolo());
	assertEquals(fascicolo.getIdFascicolo(), statoInserito.getFasFascicolo().getIdFascicolo());
	assertEquals(stato, statoInserito.getTiStatoConservazione());

    }

    @Test
    @TestTransaction
    void insertFascicoloOnStatoElenco() {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	FasFascicolo fascicolo = entityManager.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	final it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoFascicoloElenco.TiStatoFascElenco stato = it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoFascicoloElenco.TiStatoFascElenco.IN_ATTESA_SCHED;
	final FasStatoFascicoloElenco statoInserito = dao.insertFascicoloOnStatoElenco(fascicolo,
		stato);
	assertNotNull(statoInserito);
	assertEquals(fascicolo.getIdFascicolo(), statoInserito.getFasFascicolo().getIdFascicolo());
	assertEquals(stato, statoInserito.getTiStatoFascElencoVers());
    }
}
