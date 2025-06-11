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
import static it.eng.parer.DatabaseInit.ID_ORG_STRUT;
import static it.eng.parer.DatabaseInit.ID_VRS_SES_FASCICOLO_ERR;
import static it.eng.parer.DatabaseInit.ID_VRS_SES_FASCICOLO_KO;
import static it.eng.parer.DatabaseInit.ID_VRS_V_UPD_FASCICOLO_KO;
import static it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle.ERR_666P;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.DatabaseInit;
import it.eng.parer.Profiles;
import it.eng.parer.TestFactory;
import it.eng.parer.fascicolo.beans.IlogSessioneFascicoliDao;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.CSVersatore;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.utils.CostantiDB;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloErr;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsXmlSesFascicoloErr;
import it.eng.parer.fascicolo.jpa.entity.VrsXmlSesFascicoloKo;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoGeneraleType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.xml.bind.JAXBException;

@QuarkusTest
@TestProfile(Profiles.H2.class)
class LogSessioneFascicoliDaoTest {

    @Inject
    IlogSessioneFascicoliDao dao;
    @Inject
    EntityManager em;
    @Inject
    DatabaseInit dbInit;

    private final TestFactory testFactory = new TestFactory();

    @Test
    @TestTransaction
    void cercaFascicoloKo_nessunFascicolo() throws AppGenericPersistenceException {
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2020);
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setNumero("999999");
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(0L);
	final RispostaControlli rispostaControlli = dao.cercaFascicoloKo(versFascicoloExt);
	assertTrue(rispostaControlli.isrBoolean());
    }

    @Test
    @TestTransaction
    void cercaFascicoloKo_unFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertDecTipoFascicolo();
	dbInit.insertVrsFascicoloKo();
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2017);
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata()
		.setNumero("1.1-2017-01-1");
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	final RispostaControlli rispostaControlli = dao.cercaFascicoloKo(versFascicoloExt);
	assertTrue(rispostaControlli.isrBoolean());
	assertTrue(rispostaControlli.getrLong() > 0);
	assertNotNull(rispostaControlli.getrObject());
    }

    @Test
    @TestTransaction
    void scriviFascicoloErr_severityWarning() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertDecTipoFascicolo();
	final RispostaWSFascicolo rispostaWSFascicolo = testFactory.createRispostaWSFascicolo();
	rispostaWSFascicolo.getCompRapportoVersFascicolo()
		.setEsitoGenerale(new EsitoGeneraleType());
	rispostaWSFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setCodiceErrore(ERR_666P);
	rispostaWSFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setMessaggioErrore("Errore di test Junit");
	rispostaWSFascicolo.setSeverity(IRispostaWS.SeverityEnum.WARNING);
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().setVersatoreNonverificato(new CSVersatore());
	versFascicoloExt.getStrutturaComponenti().getVersatoreNonverificato().setAmbiente("JUNIT");
	versFascicoloExt.getStrutturaComponenti().getVersatoreNonverificato().setEnte("TEST");
	versFascicoloExt.getStrutturaComponenti().getVersatoreNonverificato()
		.setStruttura("scriviFascicoloErr");
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versFascicoloExt.getStrutturaComponenti().setChiaveNonVerificata(new CSChiaveFasc());
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2022);
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setNumero("numero");
	versFascicoloExt.getStrutturaComponenti().setIdTipoFascicolo(ID_DEC_TIPO_FASCICOLO);
	versFascicoloExt.getStrutturaComponenti()
		.setTipoFascicoloNonverificato("tipo fascicolo non verificato");
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	final RispostaControlli rispostaControlli = dao.scriviFascicoloErr(rispostaWSFascicolo,
		versFascicoloExt, sessione);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	assertNotNull(((VrsSesFascicoloErr) rispostaControlli.getrObject()).getIdSesFascicoloErr());
    }

    @Test
    @TestTransaction
    void scriviFascicoloErr_severityError() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertDecTipoFascicolo();
	dbInit.initErrors(ERR_666P);
	final RispostaWSFascicolo rispostaWSFascicolo = testFactory.createRispostaWSFascicolo();
	rispostaWSFascicolo.getCompRapportoVersFascicolo()
		.setEsitoGenerale(new EsitoGeneraleType());
	rispostaWSFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setCodiceErrore(ERR_666P);
	rispostaWSFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setMessaggioErrore("Errore di test Junit");
	rispostaWSFascicolo.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().setVersatoreNonverificato(new CSVersatore());
	versFascicoloExt.getStrutturaComponenti().getVersatoreNonverificato().setAmbiente("JUNIT");
	versFascicoloExt.getStrutturaComponenti().getVersatoreNonverificato().setEnte("TEST");
	versFascicoloExt.getStrutturaComponenti().getVersatoreNonverificato()
		.setStruttura("scriviFascicoloErr");
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versFascicoloExt.getStrutturaComponenti().setChiaveNonVerificata(new CSChiaveFasc());
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2022);
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setNumero("numero");
	versFascicoloExt.getStrutturaComponenti().setIdTipoFascicolo(ID_DEC_TIPO_FASCICOLO);
	versFascicoloExt.getStrutturaComponenti()
		.setTipoFascicoloNonverificato("tipo fascicolo non verificato");
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	final RispostaControlli rispostaControlli = dao.scriviFascicoloErr(rispostaWSFascicolo,
		versFascicoloExt, sessione);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	assertNotNull(((VrsSesFascicoloErr) rispostaControlli.getrObject()).getIdSesFascicoloErr());
    }

    @Test
    @TestTransaction
    void scriviXmlFascicoloErr() throws AppGenericPersistenceException {
	dbInit.insertVrsSesFascicoloErr();
	final VrsSesFascicoloErr fascicoloErr = new VrsSesFascicoloErr();
	fascicoloErr.setIdSesFascicoloErr(ID_VRS_SES_FASCICOLO_ERR);
	final RispostaWSFascicolo rispostaWSFascicolo = testFactory.createRispostaWSFascicolo();
	dao.scriviXmlFascicoloErr(rispostaWSFascicolo,
		testFactory.createVersFascicoloExtBuilder().build(), fascicoloErr,
		testFactory.createBackendStorage(), Collections.emptyMap());
	TypedQuery<VrsXmlSesFascicoloErr> checkQuery = em.createQuery(
		"SELECT v FROM VrsXmlSesFascicoloErr v WHERE v.vrsSesFascicoloErr=:vrsSesFascicoloErr",
		VrsXmlSesFascicoloErr.class);
	checkQuery.setParameter("vrsSesFascicoloErr", fascicoloErr);
	// deve aver scritto due record, uno per la richiesta e uno per la risposta
	final List<VrsXmlSesFascicoloErr> insertedVrsXmlSesFascicoloErr = checkQuery
		.getResultList();
	assertEquals(2, insertedVrsXmlSesFascicoloErr.size());
	assertTrue(insertedVrsXmlSesFascicoloErr.stream()
		.anyMatch(v -> v.getTiXml().equals(CostantiDB.TipiXmlDati.RICHIESTA.toString())));
	assertTrue(insertedVrsXmlSesFascicoloErr.stream()
		.anyMatch(v -> v.getTiXml().equals(CostantiDB.TipiXmlDati.RISPOSTA.toString())));
    }

    @Test
    @TestTransaction
    void scriviFascicoloKo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	dbInit.initErrors(ERR_666P);
	final RispostaWSFascicolo rispostaFascicolo = testFactory.createRispostaWSFascicolo();
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setMessaggioErrore("JUNIT TEST");
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setCodiceErrore(ERR_666P);
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder().build();
	versamento.getStrutturaComponenti().setIdTipoFascicolo(ID_DEC_TIPO_FASCICOLO);
	versamento.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2022);
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setNumero("numero");
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	final RispostaControlli rispostaControlli = dao.scriviFascicoloKo(rispostaFascicolo,
		versamento, sessione);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	VrsFascicoloKo vrsFascicoloKo = (VrsFascicoloKo) rispostaControlli.getrObject();
	assertNotNull(vrsFascicoloKo.getIdFascicoloKo());
    }

    @Test
    @TestTransaction
    void scriviSessioneFascicoloKo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertVrsVUpdFascicoloKo();
	dbInit.insertFasFasciclo();
	dbInit.initErrors(ERR_666P);
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	final RispostaWSFascicolo rispostaFascicolo = testFactory.createRispostaWSFascicolo();
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setMessaggioErrore("JUNIT TEST");
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setCodiceErrore(ERR_666P);
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().setIdTipoFascicolo(ID_DEC_TIPO_FASCICOLO);
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2022);
	versFascicoloExt.getUtente().setIdUtente(5000L);
	final VrsFascicoloKo vrsFascicoloKo = testFactory.createVrsFascicoloKo();
	vrsFascicoloKo.setIdFascicoloKo(ID_VRS_V_UPD_FASCICOLO_KO);
	vrsFascicoloKo.getOrgStrut().setIdStrut(ID_ORG_STRUT);
	vrsFascicoloKo.setAaFascicolo(BigDecimal.valueOf(2022));
	vrsFascicoloKo.setTiStatoFascicoloKo("NON_VERIFICATO");
	vrsFascicoloKo.getDecTipoFascicolo().setIdTipoFascicolo(ID_DEC_TIPO_FASCICOLO);
	// la metto before "tmApertura della sesisone così esegue anche
	// aggiornaConteggioMonContaFasKo
	vrsFascicoloKo.setTsIniLastSes(sessione.getTmApertura().minusDays(2).toLocalDateTime());
	final FasFascicolo fasFascicolo = testFactory.createFasFascicolo();
	fasFascicolo.setIdFascicolo(ID_FAS_FASCICOLO);
	final RispostaControlli rispostaControlli = dao.scriviSessioneFascicoloKo(rispostaFascicolo,
		versFascicoloExt, sessione, vrsFascicoloKo, fasFascicolo);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	VrsSesFascicoloKo vrsSesFascicoloKo = (VrsSesFascicoloKo) rispostaControlli.getrObject();
	assertNotNull(vrsSesFascicoloKo.getIdSesFascicoloKo());
    }

    @Test
    @TestTransaction
    void scriviSessioneFascicoloKo_senzaFascicoloOk() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertDecTipoFascicolo();
	dbInit.insertVrsFascicoloKo();
	dbInit.insertVrsVUpdFascicoloKo();
	dbInit.initErrors();
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	final RispostaWSFascicolo rispostaFascicolo = testFactory.createRispostaWSFascicolo();
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setMessaggioErrore("JUNIT TEST");
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setCodiceErrore(ERR_666P);
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().setIdTipoFascicolo(ID_DEC_TIPO_FASCICOLO);
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2022);
	versFascicoloExt.getUtente().setIdUtente(5000L);
	final VrsFascicoloKo vrsFascicoloKo = testFactory.createVrsFascicoloKo();
	vrsFascicoloKo.setIdFascicoloKo(ID_VRS_V_UPD_FASCICOLO_KO);
	// la metto before "tmApertura della sesisone così esegue anche
	// aggiornaConteggioMonContaFasKo
	vrsFascicoloKo.setTsIniLastSes(sessione.getTmApertura().minusDays(2).toLocalDateTime());
	final RispostaControlli rispostaControlli = dao.scriviSessioneFascicoloKo(rispostaFascicolo,
		versFascicoloExt, sessione, vrsFascicoloKo, null);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	VrsSesFascicoloKo vrsSesFascicoloKo = (VrsSesFascicoloKo) rispostaControlli.getrObject();
	assertEquals(vrsFascicoloKo.getIdFascicoloKo(),
		vrsSesFascicoloKo.getVrsFascicoloKo().getIdFascicoloKo());

    }

    @Test
    @TestTransaction
    void scriviSessioneFascicoloKo_senzaFascicoloOk_senzaVrsFascicoloKo()
	    throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertDecTipoFascicolo();
	dbInit.initErrors(ERR_666P);
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	final RispostaWSFascicolo rispostaFascicolo = testFactory.createRispostaWSFascicolo();
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setMessaggioErrore("JUNIT TEST");
	rispostaFascicolo.getCompRapportoVersFascicolo().getEsitoGenerale()
		.setCodiceErrore(ERR_666P);
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().setIdTipoFascicolo(ID_DEC_TIPO_FASCICOLO);
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versFascicoloExt.getStrutturaComponenti().getChiaveNonVerificata().setAnno(2022);
	versFascicoloExt.getUtente().setIdUtente(5000L);
	final RispostaControlli rispostaControlli = dao.scriviSessioneFascicoloKo(rispostaFascicolo,
		versFascicoloExt, sessione, null, null);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	VrsSesFascicoloKo vrsSesFascicoloKo = (VrsSesFascicoloKo) rispostaControlli.getrObject();
	assertNull(vrsSesFascicoloKo.getVrsFascicoloKo());

    }

    @Test
    @TestTransaction
    void scriviXmlFascicoloKo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	dbInit.insertVrsSesFascicoloKo();
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	final VrsSesFascicoloKo sessFascicoloKo = testFactory.createVrsSesFascicoloKo();
	sessFascicoloKo.setIdSesFascicoloKo(ID_VRS_SES_FASCICOLO_KO);
	dao.scriviXmlFascicoloKo(testFactory.createRispostaWSFascicolo(), versFascicoloExt,
		testFactory.createBlockingFakeSession(), sessFascicoloKo,
		testFactory.createBackendStorage(), Collections.emptyMap());
	assertTrue(true);
	TypedQuery<VrsXmlSesFascicoloKo> checkQuery = em.createQuery(
		"SELECT v FROM VrsXmlSesFascicoloKo v WHERE v.vrsSesFascicoloKo=:vrsSesFascicoloKo",
		VrsXmlSesFascicoloKo.class);
	checkQuery.setParameter("vrsSesFascicoloKo", sessFascicoloKo);
	// deve aver scritto due record, uno per la richiesta e uno per la risposta
	final List<VrsXmlSesFascicoloKo> insertedVrsXmlSesFascicoloKos = checkQuery.getResultList();
	assertEquals(2, insertedVrsXmlSesFascicoloKos.size());
	assertTrue(insertedVrsXmlSesFascicoloKos.stream()
		.anyMatch(v -> v.getTiXml().equals(CostantiDB.TipiXmlDati.RICHIESTA.toString())));
	assertTrue(insertedVrsXmlSesFascicoloKos.stream()
		.anyMatch(v -> v.getTiXml().equals(CostantiDB.TipiXmlDati.RISPOSTA.toString())));
    }

    @Test
    @TestTransaction
    void scriviErroriFascicoloKo_errore() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	dbInit.insertVrsSesFascicoloKo();
	dbInit.initErrors();
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.addError("errore di test", ERR_666P, "solo per test");
	final VrsSesFascicoloKo vrsSesFascicoloKo = testFactory.createVrsSesFascicoloKo();
	vrsSesFascicoloKo.setIdSesFascicoloKo(ID_VRS_SES_FASCICOLO_KO);
	dao.scriviErroriFascicoloKo(versFascicoloExt, vrsSesFascicoloKo);
	assertEquals(1, vrsSesFascicoloKo.getVrsErrSesFascicoloKos().size());
	assertEquals("FATALE", vrsSesFascicoloKo.getVrsErrSesFascicoloKos().get(0).getTiErr());
    }

    @Test
    @TestTransaction
    void scriviErroriFascicoloKo_warning() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	dbInit.insertVrsSesFascicoloKo();
	dbInit.initErrors();
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.addWarning("errore di test", ERR_666P, "solo per test");
	final VrsSesFascicoloKo vrsSesFascicoloKo = testFactory.createVrsSesFascicoloKo();
	vrsSesFascicoloKo.setIdSesFascicoloKo(ID_VRS_SES_FASCICOLO_KO);
	dao.scriviErroriFascicoloKo(versFascicoloExt, vrsSesFascicoloKo);
	assertEquals(1, vrsSesFascicoloKo.getVrsErrSesFascicoloKos().size());
	assertEquals("WARNING", vrsSesFascicoloKo.getVrsErrSesFascicoloKos().get(0).getTiErr());
    }

    @Test
    void generaRapportoVersamento() throws JAXBException {
	final RispostaWSFascicolo rispostaWSFascicolo = testFactory.createRispostaWSFascicolo();
	final String rapportoVersamento = dao.generaRapportoVersamento(rispostaWSFascicolo);
	assertEquals(
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><EsitoVersamentoFascicolo><VersioneEsitoVersamentoFascicolo>1.0</VersioneEsitoVersamentoFascicolo><RapportoVersamentoFascicolo><VersioneRapportoVersamento>1.0</VersioneRapportoVersamento><EsitoGenerale/></RapportoVersamentoFascicolo></EsitoVersamentoFascicolo>",
		rapportoVersamento);
    }
}
