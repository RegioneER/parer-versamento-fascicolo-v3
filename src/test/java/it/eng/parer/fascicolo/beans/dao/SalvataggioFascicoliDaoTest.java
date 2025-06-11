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

import static it.eng.parer.DatabaseInit.ID_DEC_AA_TIPO_FASCICOLO;
import static it.eng.parer.DatabaseInit.ID_DEC_MODELLO_XSD_DATI_SPEC;
import static it.eng.parer.DatabaseInit.ID_DEC_MODELLO_XSD_PROFILO_ARCHIVISTICO;
import static it.eng.parer.DatabaseInit.ID_DEC_MODELLO_XSD_PROFILO_GENERALE;
import static it.eng.parer.DatabaseInit.ID_DEC_MODELLO_XSD_PROFILO_NORMATIVO;
import static it.eng.parer.DatabaseInit.ID_FAS_FASCICOLO;
import static it.eng.parer.DatabaseInit.ID_ORG_STRUT;
import static it.eng.parer.DatabaseInit.ID_VRS_FASCICOLO_KO;
import static it.eng.parer.fascicolo.beans.utils.converter.DateUtilsConverter.convert;
import static it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle.ERR_666P;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.DatabaseInit;
import it.eng.parer.Profiles;
import it.eng.parer.TestFactory;
import it.eng.parer.fascicolo.beans.ISalvataggioFascicoliDao;
import it.eng.parer.fascicolo.beans.dto.ConfigNumFasc;
import it.eng.parer.fascicolo.beans.dto.FascicoloLink;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.UnitaDocLink;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.DatoSpecifico;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGEvento;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DXPGSoggetto;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DatiXmlProfiloGenerale;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DXPNSoggetto;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.jpa.entity.DecWarnAaTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasValoreAttribFascicolo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloKo;
import it.eng.parer.ws.xml.versfascicoloV3.ObjectFactory;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloArchivisticoType;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloGeneraleType;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloNormativoType;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloSpecificoType;
import it.eng.parer.ws.xml.versfascicoloV3.TipoConservazioneType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@QuarkusTest
@TestProfile(Profiles.H2.class)
class SalvataggioFascicoliDaoTest {
    @Inject
    ISalvataggioFascicoliDao dao;

    @Inject
    DatabaseInit dbInit;

    @Inject
    EntityManager em;

    private TestFactory testFactory = new TestFactory();

    @Test
    @TestTransaction
    void scriviFascicolo_senzaProfili() throws AppGenericPersistenceException {
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	sessione.getIndiceSIPFascicolo().getParametri()
		.setTipoConservazione(TipoConservazioneType.IN_ARCHIVIO);
	// flags di VersFascicoloExt tutti a false di default
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder().build();
	final int anno = 2018;
	versamento.getUtente().setIdUtente(5000L);
	versamento.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setAnno(anno);
	final String numero = "0909090";
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setNumero(numero);
	versamento.getStrutturaComponenti().setCdKeyFascicoloNormalized("0909090");
	versamento.getStrutturaComponenti().setIdTipoFascicolo(1L);
	versamento.getStrutturaComponenti().setIdVoceTitol(11923L);
	versamento.getStrutturaComponenti().setKeyOrdCalcolata("2018-01");
	final List<UnitaDocLink> unitaDocElencate = Arrays.asList(new UnitaDocLink(),
		new UnitaDocLink());
	versamento.getStrutturaComponenti().setUnitaDocElencate(unitaDocElencate);

	final Executable executable = () -> dao.scriviFascicolo(versamento, sessione, null);
	final AppGenericPersistenceException appException = assertThrows(
		AppGenericPersistenceException.class, executable,
		"Should fail throwing AppGenericPersistenceException");
	assertEquals(ERR_666P, appException.getCodErr());
    }

    @Test
    @TestTransaction
    void scriviFascicolo_soloProfiloArch() throws AppGenericPersistenceException {
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	sessione.getIndiceSIPFascicolo().getParametri()
		.setTipoConservazione(TipoConservazioneType.IN_ARCHIVIO);
	// flags di VersFascicoloExt tutti a false di default
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder()
		.addDatiXmlProfiloArchivistico().build();
	final int anno = 2018;
	versamento.getUtente().setIdUtente(5000L);
	versamento.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setAnno(anno);
	final String numero = "0909090";
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setNumero(numero);
	versamento.getStrutturaComponenti().setCdKeyFascicoloNormalized("0909090");
	versamento.getStrutturaComponenti().setIdTipoFascicolo(1L);
	versamento.getStrutturaComponenti().setIdVoceTitol(11923L);
	versamento.getStrutturaComponenti().setKeyOrdCalcolata("2018-01");
	final List<UnitaDocLink> unitaDocElencate = Arrays.asList(new UnitaDocLink(),
		new UnitaDocLink());
	versamento.getStrutturaComponenti().setUnitaDocElencate(unitaDocElencate);
	// DatiXmlProfiloArchivistico
	final String indiceClassificazione = "Indice classificazione";
	versamento.getStrutturaComponenti().getDatiXmlProfiloArchivistico()
		.setIndiceClassificazione(indiceClassificazione);
	versamento.getStrutturaComponenti().getDatiXmlProfiloArchivistico()
		.setDescIndiceClassificazione("Descrizione indice classificazioni");
	final BigDecimal tempoConservazione = BigDecimal.TEN;
	versamento.getStrutturaComponenti().getDatiXmlProfiloArchivistico()
		.setTempoConservazione(tempoConservazione);

	final RispostaControlli rispostaControlli = dao.scriviFascicolo(versamento, sessione, null);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	FasFascicolo fascicolo = (FasFascicolo) rispostaControlli.getrObject();
	assertNotNull(fascicolo.getIdFascicolo());
	assertEquals(anno, fascicolo.getAaFascicolo().longValue());
	assertEquals(numero, fascicolo.getCdKeyFascicolo());
	assertEquals(unitaDocElencate.size(), fascicolo.getNiUnitaDoc().intValue());
	assertEquals(tempoConservazione, fascicolo.getNiAaConservazione());
	assertEquals(indiceClassificazione, fascicolo.getCdIndiceClassif());
    }

    @Test
    @TestTransaction
    void scriviFascicolo() throws AppGenericPersistenceException {
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	sessione.getIndiceSIPFascicolo().getParametri()
		.setTipoConservazione(TipoConservazioneType.IN_ARCHIVIO);
	// flags di VersFascicoloExt tutti a false di default
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder()
		.addDatiXmlProfiloArchivistico().addDatiXmlProfiloGenerale().build();
	final int anno = 2018;
	versamento.getUtente().setIdUtente(5000L);
	versamento.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setAnno(anno);
	final String numero = "0909090";
	versamento.getStrutturaComponenti().getChiaveNonVerificata().setNumero(numero);
	versamento.getStrutturaComponenti().setCdKeyFascicoloNormalized("0909090");
	versamento.getStrutturaComponenti().setIdTipoFascicolo(1L);
	versamento.getStrutturaComponenti().setIdVoceTitol(11923L);
	versamento.getStrutturaComponenti().setKeyOrdCalcolata("2018-01");
	final List<UnitaDocLink> unitaDocElencate = Arrays.asList(new UnitaDocLink(),
		new UnitaDocLink());
	versamento.getStrutturaComponenti().setUnitaDocElencate(unitaDocElencate);
	// DatiXmlProfiloGenerale
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale()
		.setOggettoFascicolo("Test " + this.getClass().getSimpleName());
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale()
		.setDataApertura(convert(LocalDateTime.now()));
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale()
		.setDataChiusura(convert(LocalDateTime.now()));
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale()
		.setNoteFascicolo("Note di test");
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale()
		.setLvlRiservatezza("non definito");
	// ProcAmm
	final String codiceProcAmm = "1";
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale().getProcAmm()
		.setCodice(codiceProcAmm);
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale().getProcAmm()
		.setMateriaArgStrut("Numeri");
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale().getProcAmm()
		.setDenominazione("UNO");
	// DXPGEvento
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale().getEventi()
		.add(new DXPGEvento());
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale().getEventi().get(0)
		.setDataInizio(convert(LocalDateTime.now()));
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale().getEventi().get(0)
		.setDataFine(convert(LocalDateTime.now()));
	// DatiXmlProfiloArchivistico
	final String indiceClassificazione = "Indice classificazione";
	versamento.getStrutturaComponenti().getDatiXmlProfiloArchivistico()
		.setIndiceClassificazione(indiceClassificazione);
	versamento.getStrutturaComponenti().getDatiXmlProfiloArchivistico()
		.setDescIndiceClassificazione("Descrizione indice classificazioni");
	final BigDecimal tempoConservazione = BigDecimal.TEN;
	versamento.getStrutturaComponenti().getDatiXmlProfiloArchivistico()
		.setTempoConservazione(tempoConservazione);

	final String descrizioneEvento = "Testare il fascicolo";
	versamento.getStrutturaComponenti().getDatiXmlProfiloGenerale().getEventi().get(0)
		.setDescrizione(descrizioneEvento);
	final RispostaControlli rispostaControlli = dao.scriviFascicolo(versamento, sessione, null);
	assertTrue(rispostaControlli.isrBoolean());
	assertNotNull(rispostaControlli.getrObject());
	FasFascicolo fascicolo = (FasFascicolo) rispostaControlli.getrObject();
	assertNotNull(fascicolo.getIdFascicolo());
	assertEquals(anno, fascicolo.getAaFascicolo().longValue());
	assertEquals(numero, fascicolo.getCdKeyFascicolo());
	assertEquals(unitaDocElencate.size(), fascicolo.getNiUnitaDoc().intValue());
	assertEquals(1, fascicolo.getFasEventoFascicolos().size());
	assertEquals(descrizioneEvento,
		fascicolo.getFasEventoFascicolos().get(0).getDsDenomEvento());
	assertEquals(tempoConservazione, fascicolo.getNiAaConservazione());
	assertEquals(codiceProcAmm, fascicolo.getCdProcAmmin());
	assertEquals(indiceClassificazione, fascicolo.getCdIndiceClassif());
    }

    @Test
    @TestTransaction
    void scriviSogggetti_senzaProfili() throws AppGenericPersistenceException {
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	final FasFascicolo fasFascicolo = testFactory.createFasFascicolo();
	dao.scriviSogggetti(versFascicoloExt, fasFascicolo);
	assertTrue(fasFascicolo.getFasSogFascicolos().isEmpty());
    }

    @Test
    @TestTransaction
    void scriviSogggetti() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.addDatiXmlProfiloNormativo().build();
	final FasFascicolo fasFascicolo = em.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	// soggetti profilo generale
	versFascicoloExt.getStrutturaComponenti()
		.setDatiXmlProfiloGenerale(new DatiXmlProfiloGenerale());

	final DXPGSoggetto dxpgSoggetto = new DXPGSoggetto();
	dxpgSoggetto.addDenominazione("denominazione");
	dxpgSoggetto.setNome("Test");
	dxpgSoggetto.setCognome("Junit");
	dxpgSoggetto.setCittadinanza("Java");
	dxpgSoggetto.setLuogoNascita("BO");
	dxpgSoggetto.setTipoRapporto("tester");
	versFascicoloExt.getStrutturaComponenti().getDatiXmlProfiloGenerale().getSoggetti()
		.add(dxpgSoggetto);
	// soggetti profilo normativo
	final DXPNSoggetto dxpnSoggetto = new DXPNSoggetto();
	dxpnSoggetto.addDenominazione("denominazione");
	dxpnSoggetto.setNome("Test");
	dxpnSoggetto.setCognome("Junit");
	dxpnSoggetto.setTipoRapporto("tester");
	versFascicoloExt.getStrutturaComponenti().getDatiXmlProfiloNormativo().getSoggetti()
		.add(dxpnSoggetto);
	dao.scriviSogggetti(versFascicoloExt, fasFascicolo);
	assertEquals(2, fasFascicolo.getFasSogFascicolos().size());
	assertNotNull(fasFascicolo.getFasSogFascicolos().get(0));
	assertNotNull(fasFascicolo.getFasSogFascicolos().get(1));
    }

    @Test
    @TestTransaction
    void scriviRequestResponseFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	final RispostaWSFascicolo rispostaWSFascicolo = testFactory.createRispostaWSFascicolo();
	rispostaWSFascicolo.getCompRapportoVersFascicolo().setVersioneRapportoVersamento("1.2.3");
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	versFascicoloExt.getStrutturaComponenti().setUrnPartChiaveFascicolo("prova:urn:test");
	versFascicoloExt.getStrutturaComponenti()
		.setUrnPartChiaveFascicoloNormalized("prova:urn:test");
	versFascicoloExt.getStrutturaComponenti().setUrnPartChiaveFascicolo("prova:urn:test");
	versFascicoloExt.getStrutturaComponenti()
		.setUrnPartChiaveFascicoloNormalized("prova:urn:test");
	versFascicoloExt.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	final BlockingFakeSession session = testFactory.createBlockingFakeSession();
	session.setDatiDaSalvareIndiceSip("");
	final FasFascicolo fasFascicolo = em.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	dao.scriviRequestResponseFascicolo(rispostaWSFascicolo, versFascicoloExt, session,
		testFactory.createBackendStorage(), Collections.emptyMap(), fasFascicolo);
	assertEquals(2, fasFascicolo.getFasXmlVersFascicolos().size());
	assertNotNull(fasFascicolo.getFasXmlVersFascicolos().get(0));
	assertNotNull(fasFascicolo.getFasXmlVersFascicolos().get(1));
    }

    @Test
    @TestTransaction
    void scriviProfiliXMLFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	dbInit.insertDecModelloXsd();
	ObjectFactory factory = new ObjectFactory();

	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder()
		.addDatiXmlProfiloGenerale().addDatiXmlProfiloArchivistico()
		.addDatiXmlProfiloNormativo().addDatiSpecifici().build();
	versamento.getStrutturaComponenti()
		.setIdRecxsdProfiloGenerale(ID_DEC_MODELLO_XSD_PROFILO_GENERALE);
	versamento.getStrutturaComponenti()
		.setIdRecXsdProfiloArchivistico(ID_DEC_MODELLO_XSD_PROFILO_ARCHIVISTICO);
	versamento.getStrutturaComponenti()
		.setIdRecXsdProfiloNormativo(ID_DEC_MODELLO_XSD_PROFILO_NORMATIVO);
	versamento.getStrutturaComponenti().setIdRecXsdDatiSpec(ID_DEC_MODELLO_XSD_DATI_SPEC);
	versamento.getStrutturaComponenti().setIdStruttura(ID_ORG_STRUT);
	final FasFascicolo fasFascicolo = em.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	// profilo generale
	final ProfiloGeneraleType profiloGeneraleType = new ProfiloGeneraleType();
	profiloGeneraleType.setAny(testFactory.createProfiloGenerale());
	sessione.getIndiceSIPFascicolo().setProfiloGenerale(profiloGeneraleType);
	// profilo archivistico
	final ProfiloArchivisticoType profiloArchivisticoType = factory
		.createProfiloArchivisticoType();
	profiloArchivisticoType.setAny(testFactory.createProfiloArchivistico());
	sessione.getIndiceSIPFascicolo().setProfiloArchivistico(profiloArchivisticoType);
	// profilo normativo
	final ProfiloNormativoType profiloNormativoType = factory.createProfiloNormativoType();
	profiloNormativoType.setAny(testFactory.createProfiloNormativo());
	sessione.getIndiceSIPFascicolo().setProfiloNormativo(
		factory.createIndiceSIPFascicoloProfiloNormativo(profiloNormativoType));
	// profilo specifico
	final ProfiloSpecificoType profiloSpecificoType = factory.createProfiloSpecificoType();
	profiloArchivisticoType.setAny(testFactory.createProfiloSpecifico());
	sessione.getIndiceSIPFascicolo().setProfiloSpecifico(
		factory.createIndiceSIPFascicoloProfiloSpecifico(profiloSpecificoType));
	dao.scriviProfiliXMLFascicolo(versamento, sessione, testFactory.createBackendStorage(),
		Collections.emptyMap(), fasFascicolo);
	assertEquals(4, fasFascicolo.getFasXmlFascicolos().size());
	assertNotNull(fasFascicolo.getFasXmlFascicolos().get(0));
	assertNotNull(fasFascicolo.getFasXmlFascicolos().get(1));
	assertNotNull(fasFascicolo.getFasXmlFascicolos().get(2));
	assertNotNull(fasFascicolo.getFasXmlFascicolos().get(3));
    }

    @Test
    @TestTransaction
    void scriviUnitaDocFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	final FasFascicolo fascicolo = testFactory.createFasFascicolo();
	fascicolo.setIdFascicolo(ID_FAS_FASCICOLO);
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder()
		.addUnitaDocElencate().build();
	final UnitaDocLink unitaDocLink = new UnitaDocLink();
	unitaDocLink.setIdLinkUnitaDoc(28L);
	unitaDocLink.setPosizione(BigInteger.valueOf(100));
	unitaDocLink.setDataInserimentoFas(convert(LocalDateTime.now()));
	versamento.getStrutturaComponenti().getUnitaDocElencate().add(unitaDocLink);
	dao.scriviUnitaDocFascicolo(versamento, fascicolo);
	assertEquals(1, fascicolo.getFasUnitaDocFascicolos().size());
	assertNotNull(fascicolo.getFasUnitaDocFascicolos().get(0).getIdUnitaDocFascicolo());
    }

    @Test
    @TestTransaction
    void scriviLinkFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder()
		.addFascicoliLinked().build();
	final FascicoloLink fascicoloLink = new FascicoloLink();
	fascicoloLink.setCsChiaveFasc(new CSChiaveFasc());
	fascicoloLink.getCsChiaveFasc().setNumero("numero");
	fascicoloLink.getCsChiaveFasc().setAnno(2020);
	fascicoloLink.setDescCollegamento("descCollegamento");
	fascicoloLink.setIdLinkFasc(ID_FAS_FASCICOLO);
	versamento.getStrutturaComponenti().getFascicoliLinked().add(fascicoloLink);
	final FasFascicolo fascicolo = em.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	dao.scriviLinkFascicolo(versamento, fascicolo);
	assertEquals(1, fascicolo.getFasLinkFascicolos1().size());
	assertNotNull(fascicolo.getFasLinkFascicolos1().get(0).getIdLinkFascicolo());
    }

    @Test
    @TestTransaction
    void scriviWarningFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	dbInit.initErrors(ERR_666P);
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder().build();
	versamento.addWarning("errore di test", ERR_666P, "solo per test");
	final FasFascicolo fascicolo = em.find(FasFascicolo.class, ID_FAS_FASCICOLO);
	dao.scriviWarningFascicolo(versamento, fascicolo);
	assertEquals(1, fascicolo.getFasWarnFascicolos().size());
	assertNotNull(fascicolo.getFasWarnFascicolos().get(0).getIdWarnFascicolo());
    }

    @Test
    @TestTransaction
    void salvaWarningAATipoFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertDecAATipoFasciclo();
	final BigDecimal anno = BigDecimal.valueOf(2022);
	final VersFascicoloExt versamento = testFactory.createVersFascicoloExtBuilder().build();
	versamento.getStrutturaComponenti()
		.setConfigNumFasc(new ConfigNumFasc(ID_DEC_AA_TIPO_FASCICOLO));
	final BlockingFakeSession sessione = testFactory.createBlockingFakeSession();
	sessione.getIndiceSIPFascicolo().getIntestazione().getChiave().setAnno(anno.intValue());
	dao.salvaWarningAATipoFascicolo(versamento, sessione);
	TypedQuery<DecWarnAaTipoFascicolo> queryCheck = em.createQuery(
		"select a FROM DecWarnAaTipoFascicolo a where a.decAaTipoFascicolo.idAaTipoFascicolo = :idAaTipoFascicolo and a.aaTipoFascicolo = :aaTipoFascicolo",
		DecWarnAaTipoFascicolo.class);
	queryCheck.setParameter("idAaTipoFascicolo", ID_DEC_AA_TIPO_FASCICOLO);
	queryCheck.setParameter("aaTipoFascicolo", anno);
	assertEquals(1, queryCheck.getResultList().size());
    }

    @Test
    @TestTransaction
    void ereditaVersamentiKoFascicolo() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	dbInit.insertVrsFascicoloKo();
	final FasFascicolo fasFascicolo = testFactory.createFasFascicolo();
	fasFascicolo.setIdFascicolo(ID_FAS_FASCICOLO);
	final VrsFascicoloKo fascicoloKo = em.find(VrsFascicoloKo.class, ID_VRS_FASCICOLO_KO);
	final int numFascKo = fascicoloKo.getVrsSesFascicoloKos().size();
	dao.ereditaVersamentiKoFascicolo(fasFascicolo, fascicoloKo);
	TypedQuery<VrsSesFascicoloKo> checkQuery = em.createQuery(
		"SELECT v FROM VrsSesFascicoloKo v WHERE v.fasFascicolo=:fasFascicolo",
		VrsSesFascicoloKo.class);
	checkQuery.setParameter("fasFascicolo", fasFascicolo);
	// tutti le VrsSesFascicoloKo associate a VrsSesFascicoloKo devono avere il FasFascicolo
	// passato come parametro
	// alla ereditaVersamentiKoFascicolo
	assertEquals(numFascKo, checkQuery.getResultList().size());
	// VrsSesFascicoloKo dev'essere stato cancellato
	assertNull(em.find(VrsFascicoloKo.class, ID_VRS_FASCICOLO_KO));
    }

    @Test
    @TestTransaction
    void scriviDatiSpecGen() throws AppGenericPersistenceException {
	dbInit.insertOrgStrut();
	dbInit.insertFasFasciclo();
	final VersFascicoloExt versFascicoloExt = testFactory.createVersFascicoloExtBuilder()
		.build();
	final DatoSpecifico datoSpecifico = new DatoSpecifico();
	datoSpecifico.setIdDatoSpec(472L);
	datoSpecifico.setValore("valore");
	versFascicoloExt.getStrutturaComponenti().getDatiSpecifici().put("1", datoSpecifico);
	versFascicoloExt.getStrutturaComponenti().setIdRecXsdUsoProfiloSpec(149);
	versFascicoloExt.getStrutturaComponenti().setIdAATipoFasc(1L);
	final FasFascicolo fasFascicolo = testFactory.createFasFascicolo();
	fasFascicolo.setIdFascicolo(ID_FAS_FASCICOLO);
	dao.scriviDatiSpecGen(versFascicoloExt, fasFascicolo);
	TypedQuery<FasValoreAttribFascicolo> checkQuery = em.createQuery(
		"SELECT v FROM FasValoreAttribFascicolo v WHERE v.fasFascicolo=:fasFascicolo",
		FasValoreAttribFascicolo.class);
	checkQuery.setParameter("fasFascicolo", fasFascicolo);
	assertEquals(1, checkQuery.getResultList().size());
    }

}
