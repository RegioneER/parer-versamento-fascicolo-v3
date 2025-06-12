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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.Profiles;
import it.eng.parer.TestFactory;
import it.eng.parer.fascicolo.beans.dao.LogSessioneFascicoliDao;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.exceptions.ObjectStorageException;
import it.eng.parer.fascicolo.jpa.entity.AplParamApplic;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloErr;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoGeneraleType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestProfile(Profiles.H2.class)
class LogSessioneFascicoliServiceRollbackTest {

    @Inject
    private LogSessioneFascicoliService logSessioneFascicoliService;
    @Inject
    private EntityManager em;
    @InjectMock
    private LogSessioneFascicoliDao logSessioneFascicoliDao;
    @InjectMock
    private ObjectStorageService objectStorageService;

    @BeforeEach
    @Transactional
    void init() throws AppGenericPersistenceException, ObjectStorageException {
	resetData();
	mockAll();
    }

    @Transactional
    void resetData() {
	listAllMockData().forEach(v -> em.remove(v));
    }

    void mockAll() throws AppGenericPersistenceException, ObjectStorageException {
	/********** registraSessioneErrata **********/
	reset(logSessioneFascicoliDao);
	when(logSessioneFascicoliDao.scriviFascicoloErr(any(), any(), any())).then(
		args -> writeMockDataThenReturnRipostaControlliTrue(new VrsSesFascicoloErr()));
	/*********** registraSessioneFallita ***************/
	when(logSessioneFascicoliDao.cercaFascicoloKo(any())).thenReturn(rispostaControlliTrue());
	when(logSessioneFascicoliDao.scriviFascicoloKo(any(), any(), any())).then(args -> {
	    RispostaControlli r = rispostaControlliTrue();
	    r.setrObject(new VrsFascicoloKo());
	    return r;
	});
	when(logSessioneFascicoliDao.scriviSessioneFascicoloKo(any(), any(), any(), any(), any()))
		.then(args -> writeMockDataThenReturnRipostaControlliTrue());
	when(objectStorageService.lookupBackendVrsSessioniErrKo()).then(args -> {
	    TestFactory testFactory = new TestFactory();
	    BackendStorage backendMetadata = testFactory.createBackendStorage();
	    return backendMetadata;
	});
    }

    @Test
    void registraSessioneErrata_success_commit() throws AppGenericPersistenceException {
	logSessioneFascicoliService.registraSessioneErrata(rispostaWs(), new VersFascicoloExt(),
		new BlockingFakeSession());
	Assertions.assertFalse(listAllMockData().isEmpty());
    }

    @Test
    void registraSessioneErrata_scriviFascicoloErrFails_rollback()
	    throws AppGenericPersistenceException {
	RispostaWSFascicolo rispostaWs = rispostaWs();

	// dato che il mock di scriviFascicoloErr viene definito come transazionale (usa
	// entityManager) nella
	// mockAllRispostaControlliTrue
	// è necessario resettarlo per fargli ritornare un semplice oggetto RispostaControlli
	reset(logSessioneFascicoliDao);
	when(logSessioneFascicoliDao.scriviFascicoloErr(any(), any(), any()))
		.thenThrow(appGenericPersistenceException());
	final AppGenericPersistenceException appException = assertTrhowsAppGenericPersistenceException(
		() -> logSessioneFascicoliService.registraSessioneErrata(rispostaWs,
			new VersFascicoloExt(), new BlockingFakeSession()));
	Assertions.assertTrue(listAllMockData().isEmpty());
	Assertions.assertEquals(appException.getDsErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
	Assertions.assertEquals(appException.getCodErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
    }

    private AppGenericPersistenceException assertTrhowsAppGenericPersistenceException(
	    Executable executable) {
	final AppGenericPersistenceException appException = assertThrows(
		AppGenericPersistenceException.class, executable,
		"Should fail throwing AppGenericPersistenceException");
	return appException;
    }

    @Test
    void registraSessioneErrata_scriviXmlFascicoloErrFails_rollback()
	    throws AppGenericPersistenceException {
	RispostaWSFascicolo rispostaWs = rispostaWs();
	doThrow(AppGenericPersistenceException.class).when(logSessioneFascicoliDao)
		.scriviXmlFascicoloErr(any(), any(), any(), any(), any());
	final AppGenericPersistenceException appException = assertTrhowsAppGenericPersistenceException(
		() -> logSessioneFascicoliService.registraSessioneErrata(rispostaWs,
			new VersFascicoloExt(), new BlockingFakeSession()));
	Assertions.assertTrue(listAllMockData().isEmpty());
	Assertions.assertEquals(appException.getDsErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
	Assertions.assertEquals(appException.getCodErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
    }

    @Test
    void registraSessioneFallita_success_commit() throws AppGenericPersistenceException {
	logSessioneFascicoliService.registraSessioneFallita(rispostaWs(), new VersFascicoloExt(),
		new BlockingFakeSession());
	Assertions.assertFalse(listAllMockData().isEmpty());
    }

    @Test
    void registraSessioneFallita_scriviSessioneFascicoloKo_rollback()
	    throws AppGenericPersistenceException {
	// dato che il mock di scriviSessioneFascicoloKo viene definito come transazionale (usa
	// entityManager) nella
	// mockAllRispostaControlliTrue
	// è necessario resettarlo per fargli ritornare un semplice oggetto RispostaControlli
	reset(logSessioneFascicoliDao);
	when(logSessioneFascicoliDao.scriviSessioneFascicoloKo(any(), any(), any(), any(), any()))
		.thenThrow(appGenericPersistenceException());

	when(logSessioneFascicoliDao.cercaFascicoloKo(any())).thenReturn(rispostaControlliTrue());
	when(logSessioneFascicoliDao.scriviFascicoloKo(any(), any(), any())).then(args -> {
	    RispostaControlli r = rispostaControlliTrue();
	    r.setrObject(new VrsFascicoloKo());
	    return r;
	});
	final RispostaWSFascicolo rispostaWs = rispostaWs();
	final AppGenericPersistenceException appException = assertTrhowsAppGenericPersistenceException(
		() -> logSessioneFascicoliService.registraSessioneFallita(rispostaWs,
			new VersFascicoloExt(), new BlockingFakeSession()));
	Assertions.assertTrue(listAllMockData().isEmpty());
	Assertions.assertEquals(appException.getDsErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
	Assertions.assertEquals(appException.getCodErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
    }

    @Test
    void registraSessioneFallita_cercaFascicoloKoFails_rollback()
	    throws AppGenericPersistenceException {
	RispostaWSFascicolo rispostaWs = rispostaWs();

	doThrow(appGenericPersistenceException()).when(logSessioneFascicoliDao)
		.cercaFascicoloKo(any());

	final AppGenericPersistenceException appException = assertTrhowsAppGenericPersistenceException(
		() -> logSessioneFascicoliService.registraSessioneFallita(rispostaWs,
			new VersFascicoloExt(), new BlockingFakeSession()));
	Assertions.assertTrue(listAllMockData().isEmpty());
	Assertions.assertEquals(appException.getDsErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
	Assertions.assertEquals(appException.getCodErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
    }

    @Test
    void registraSessioneFallita_scriviFascicoloKoFails_rollback()
	    throws AppGenericPersistenceException {
	RispostaWSFascicolo rispostaWs = rispostaWs();

	doThrow(appGenericPersistenceException()).when(logSessioneFascicoliDao)
		.scriviFascicoloKo(any(), any(), any());

	final AppGenericPersistenceException appException = assertTrhowsAppGenericPersistenceException(
		() -> logSessioneFascicoliService.registraSessioneFallita(rispostaWs,
			new VersFascicoloExt(), new BlockingFakeSession()));
	Assertions.assertTrue(listAllMockData().isEmpty());
	Assertions.assertEquals(appException.getDsErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
	Assertions.assertEquals(appException.getCodErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
    }

    @Test
    void registraSessioneFallita_scriviXmlFascicoloKoFails_rollback()
	    throws AppGenericPersistenceException {
	RispostaWSFascicolo rispostaWs = rispostaWs();

	doThrow(appGenericPersistenceException()).when(logSessioneFascicoliDao)
		.scriviXmlFascicoloKo(any(), any(), any(), any(), any(), any());
	final AppGenericPersistenceException appException = assertTrhowsAppGenericPersistenceException(
		() -> logSessioneFascicoliService.registraSessioneFallita(rispostaWs,
			new VersFascicoloExt(), new BlockingFakeSession()));
	Assertions.assertTrue(listAllMockData().isEmpty());
	Assertions.assertEquals(appException.getDsErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
	Assertions.assertEquals(appException.getCodErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
    }

    @Test
    void registraSessioneFallita_scriviErroriFascicoloKoFails_rollback()
	    throws AppGenericPersistenceException {
	RispostaWSFascicolo rispostaWs = rispostaWs();

	doThrow(appGenericPersistenceException()).when(logSessioneFascicoliDao)
		.scriviErroriFascicoloKo(any(), any());

	final AppGenericPersistenceException appException = assertTrhowsAppGenericPersistenceException(
		() -> logSessioneFascicoliService.registraSessioneFallita(rispostaWs,
			new VersFascicoloExt(), new BlockingFakeSession()));
	Assertions.assertTrue(listAllMockData().isEmpty());
	Assertions.assertEquals(appException.getDsErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
	Assertions.assertEquals(appException.getCodErr(),
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
    }

    private AppGenericPersistenceException appGenericPersistenceException() {
	return new AppGenericPersistenceException("007", "007 test error message");
    }

    private RispostaControlli writeMockDataThenReturnRipostaControlliTrue() {
	return writeMockDataThenReturnRipostaControlliTrue(null);
    }

    private RispostaControlli writeMockDataThenReturnRipostaControlliTrue(Object rispostaObject) {
	final RispostaControlli rispostaControlli = rispostaControlliTrue();
	AplParamApplic aplParamApplic = new AplParamApplic();
	aplParamApplic.setNmParamApplic("TESTING");
	em.persist(aplParamApplic);
	rispostaControlli.setrObject(rispostaObject);
	return rispostaControlli;
    }

    private RispostaControlli rispostaControlliTrue() {
	final RispostaControlli rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(true);
	return rispostaControlli;
    }

    public List<AplParamApplic> listAllMockData() {
	return em.createQuery("SELECT v FROM AplParamApplic v", AplParamApplic.class)
		.getResultList();
    }

    private RispostaWSFascicolo rispostaWs() {
	final RispostaWSFascicolo rispostaWSFascicolo = new RispostaWSFascicolo();
	rispostaWSFascicolo.setCompRapportoVersFascicolo(new CompRapportoVersFascicolo());
	rispostaWSFascicolo.getCompRapportoVersFascicolo()
		.setEsitoGenerale(new EsitoGeneraleType());
	return rispostaWSFascicolo;
    }
}
