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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.arc.Arc;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.Profiles;
import it.eng.parer.TestFactory;
import it.eng.parer.fascicolo.beans.ISalvataggioFascicoliService;
import it.eng.parer.fascicolo.beans.MessaggiWSCache;
import it.eng.parer.fascicolo.beans.dao.LogSessioneFascicoliDao;
import it.eng.parer.fascicolo.beans.dao.SalvataggioFascicoliDao;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.exceptions.ObjectStorageException;
import it.eng.parer.fascicolo.jpa.entity.DecErrSacer;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoGeneraleType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestProfile(Profiles.H2.class)
class SalvataggioFascicoliServiceRollbackTest {

    public static final int ANNO_FASCICOLO_MOCK = 2099;
    public static final String MOCK_ERR_CODE = "007";
    public static final String MOCK_ERR_MSG = "Errore mock";
    @Inject
    ISalvataggioFascicoliService service;
    @InjectMock
    private LogSessioneFascicoliDao logSessioneFascicoliDao;
    @InjectMock
    private SalvataggioFascicoliDao salvataggioFascicoliDao;
    @InjectMock
    private ObjectStorageService objectStorageService;
    @InjectMock
    private ElencoVersamentoFascicoliService elencoVersamentoFascicoliService;
    @Inject
    EntityManager entityManager;

    @BeforeEach
    void setUp() throws AppGenericPersistenceException, ObjectStorageException {
	// ripulisco il database
	resetData();
	mockAllReturnRispostaControlliTrue();
	initErrors();
    }

    @Transactional
    void initErrors() {
	// servono per far funzionare la MessaggiWSBundle.getString()
	List<String> errorCodes = Arrays.asList("666P", "WS-GENERIC-ERROR-UUID");
	TypedQuery<Long> query = entityManager
		.createQuery("SELECT count(e) FROM DecErrSacer e WHERE e.cdErr=:cdErr", Long.class);
	errorCodes.forEach(errCode -> {
	    query.setParameter("cdErr", errCode);
	    if (query.getSingleResult() == 0L) {
		DecErrSacer decErrSacer = new DecErrSacer();
		decErrSacer.setCdErr(errCode);
		decErrSacer.setDsErr("Errore test " + errCode);
		entityManager.persist(decErrSacer);
	    }
	});
	final MessaggiWSCache messaggiWSCache = Arc.container().instance(MessaggiWSCache.class)
		.get();
	messaggiWSCache.initSingleton();

    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void resetData() {
	getAllFasFascicolo().forEach(f -> entityManager.remove(f));
    }

    void mockAllReturnRispostaControlliTrue() throws AppGenericPersistenceException, ObjectStorageException {
        /*
         * Non mi interessa la logica di salvataggio dei fascicoli ma voglio solo testare che in caso di errore venga
         * fatto un rollback, quindi forzo questi comportamenti con dei mock - salvataggioFascicoliDao. scriviFascicolo
         * => scrive un record su Fas_FASCICOLO mettendo AA_FASCICOLO a 2099 - tutti gli altri metodi di
         * LogSessioneFascicoliDao, SalvataggioFascicoliDao e ElencoVersamentoFascicoliService di base ritornano una
         * RispostaControlli con esito positivo
         */
        when(logSessioneFascicoliDao.cercaFascicoloKo(any())).thenReturn(rispostaControlliTrue());
        when(salvataggioFascicoliDao.scriviFascicolo(any(), any(), any())).then(args -> {
            FasFascicolo fascicolo = new FasFascicolo();
            fascicolo.setAaFascicolo(BigDecimal.valueOf(ANNO_FASCICOLO_MOCK));
            entityManager.persist(fascicolo);
            return rispostaControlliTrue();
        });
        when(objectStorageService.lookupBackendByServiceName(anyLong(), any())).then(args -> {
            TestFactory testFactory = new TestFactory();
            BackendStorage backendMetadata = testFactory.createBackendStorage();
            return backendMetadata;
        });
    }

    /*
     * lasciando il comportamento mock di default (rispostaControlli => true) verifico che venga
     * scritto un FasFascicolo (lo fa il mock di salvataggioFascicoliDao. scriviFascicolo )
     */
    @Test
    void salvaFascicolo_success_commit() throws AppGenericPersistenceException {
	service.salvaFascicolo(rispostaWSFascicolo(), versFascicoloExt(),
		new BlockingFakeSession());
	final List<FasFascicolo> allFasFascicolo = getAllFasFascicolo();
	Assertions.assertEquals(1, allFasFascicolo.size());
	Assertions.assertEquals(ANNO_FASCICOLO_MOCK,
		getAllFasFascicolo().get(0).getAaFascicolo().longValue());
    }

    /**
     * forzo logSessioneFascicoliDao.cercaFascicoloKo a fornire un RispostaControlli => false e verifico il rollback
     * ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_cercaFascicoloKoFails_rollback() throws AppGenericPersistenceException {
        when(logSessioneFascicoliDao.cercaFascicoloKo(any())).thenThrow(appGenericPersistenceException());

        final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
        assertThrows(AppGenericPersistenceException.class,
                () -> service.salvaFascicolo(rispostaWs, versFascicoloExt(), new BlockingFakeSession()),
                "Should fail throwing AppGenericPersistenceException");
        Assertions.assertEquals(0, getAllFasFascicolo().size());
        Assertions.assertNotNull(rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
        Assertions.assertNotNull(rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.scriviSogggetti a fornire un RispostaControlli => false e
     * verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviSogggettiFails_rollback() throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.scriviSogggetti(any(), any());
	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.scriviRequestResponseFascicolo a fornire un RispostaControlli
     * => false e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviRequestResponseFascicoloFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.scriviRequestResponseFascicolo(any(), any(), any(), any(), any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.scriviDatiSpecGen a fornire un RispostaControlli => false e
     * verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviDatiSpecGenFails_rollback() throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.scriviDatiSpecGen(any(), any());
	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.scriviProfiliXMLFascicolo a fornire un RispostaControlli =>
     * false e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviProfiliXMLFascicoloFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.scriviProfiliXMLFascicolo(any(), any(), any(), any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.scriviUnitaDocFascicolo a fornire un RispostaControlli => false
     * e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviUnitaDocFascicoloFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.scriviUnitaDocFascicolo(any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.scriviLinkFascicolo a fornire un RispostaControlli => false e
     * verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviLinkFascicoloFails_rollback() throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.scriviLinkFascicolo(any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.scriviWarningFascicolo a fornire un RispostaControlli => false
     * e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviWarningFascicoloFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.scriviWarningFascicolo(any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    private AppGenericPersistenceException appGenericPersistenceException() {
	return new AppGenericPersistenceException(MOCK_ERR_CODE, MOCK_ERR_MSG);
    }

    /**
     * forzo elencoVersamentoFascicoliService.scriviElvFascDaElabElenco a fornire un
     * RispostaControlli => false e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviElvFascDaElabElencoFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(elencoVersamentoFascicoliService)
		.scriviElvFascDaElabElenco(any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo elencoVersamentoFascicoliService.scriviStatoConservFascicolo a fornire un
     * RispostaControlli => false e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviStatoConservFascicoloFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(elencoVersamentoFascicoliService)
		.scriviStatoConservFascicolo(any(), any(), any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo elencoVersamentoFascicoliService.scriviStatoFascicoloElenco a fornire un
     * RispostaControlli => false e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_scriviStatoFascicoloElencoFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(elencoVersamentoFascicoliService)
		.scriviStatoFascicoloElenco(any(), any(), any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo - logSessioneFascicoliDao.cercaFascicoloKo a restituire un VrsFascicoloKo -
     * salvataggioFascicoliDao.ereditaVersamentiKoFascicolo a fornire un RispostaControlli => false
     * e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_ereditaVersamentiKoFails_rollback() throws AppGenericPersistenceException {
	final RispostaControlli rispostaControlli = rispostaControlliTrue();
	rispostaControlli.setrObject(new VrsFascicoloKo());
	when(logSessioneFascicoliDao.cercaFascicoloKo(any())).thenReturn(rispostaControlli);
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.ereditaVersamentiKoFascicolo(any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    /**
     * forzo salvataggioFascicoliDao.salvaWarningAATipoFascicolo a fornire un RispostaControlli =>
     * false e verifico il rollback ovvero la NON scrittura su FasFascicoli
     */
    @Test
    void salvaFascicolo_salvaWarningAATipoFascicoloFails_rollback()
	    throws AppGenericPersistenceException {
	doThrow(appGenericPersistenceException()).when(salvataggioFascicoliDao)
		.salvaWarningAATipoFascicolo(any(), any());

	final RispostaWSFascicolo rispostaWs = rispostaWSFascicolo();
	assertThrows(AppGenericPersistenceException.class,
		() -> service.salvaFascicolo(rispostaWs, versFascicoloExt(),
			new BlockingFakeSession()),
		"Should fail throwing AppGenericPersistenceException");
	Assertions.assertEquals(0, getAllFasFascicolo().size());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getCodiceErrore());
	Assertions.assertNotNull(
		rispostaWs.getCompRapportoVersFascicolo().getEsitoGenerale().getMessaggioErrore());
    }

    private VersFascicoloExt versFascicoloExt() {
	final VersFascicoloExt versFascicoloExt = new VersFascicoloExt();
	versFascicoloExt.setStrutturaComponenti(strutturaComponenti());
	return versFascicoloExt;
    }

    private StrutturaVersFascicolo strutturaComponenti() {
	final StrutturaVersFascicolo strut = new StrutturaVersFascicolo();
	strut.setWarningFormatoNumero(true);
	return strut;
    }

    private List<FasFascicolo> getAllFasFascicolo() {
	TypedQuery<FasFascicolo> query = entityManager.createQuery("SELECT f FROM FasFascicolo f",
		FasFascicolo.class);
	return query.getResultList();
    }

    private RispostaControlli rispostaControlliTrue() {
	final RispostaControlli rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(true);
	return rispostaControlli;
    }

    private RispostaWSFascicolo rispostaWSFascicolo() {
	final RispostaWSFascicolo rispostaWSFascicolo = new RispostaWSFascicolo();
	rispostaWSFascicolo.setCompRapportoVersFascicolo(new CompRapportoVersFascicolo());
	final EsitoGeneraleType esitoGenerale = new EsitoGeneraleType();
	rispostaWSFascicolo.getCompRapportoVersFascicolo().setEsitoGenerale(esitoGenerale);
	return rispostaWSFascicolo;
    }
}
