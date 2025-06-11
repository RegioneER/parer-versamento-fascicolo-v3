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

package it.eng.parer.lab;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.Profiles;
import it.eng.parer.fascicolo.beans.utils.ParametroApplDB;
import it.eng.parer.fascicolo.jpa.entity.AplParamApplic;
import it.eng.parer.fascicolo.jpa.entity.AplValoreParamApplic;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestProfile(Profiles.Lab.class)
class TransactionRollbackTest {

    @Inject
    ITransactionalService service;

    @Inject
    EntityManager em;

    /**
     * @Transaction => in caso di una qualunque RuntimeException fa rollback
     */
    @Test
    void savingThrowsRuntimeException_shouldRollback() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	try {
	    service.saveThrowsRuntimeExceptions(aplParamApplic, aplValoreParamApplic,
		    new TransactionRuntimeTestingException());
	} catch (Exception e) {
	    Assertions.assertNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	    Assertions.assertNull(
		    findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	}
    }

    /**
     * @Transaction => in caso di un'eccezione NON RuntimeException NON viene fatto rollback
     */
    @Test
    void savingThrowsCustomException_shouldSave() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	try {
	    service.saveThrowsCustomExceptions(aplParamApplic, aplValoreParamApplic);
	} catch (Exception e) {
	    Assertions.assertNotNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	    Assertions.assertNotNull(
		    findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	    delete(aplParamApplic.getIdParamApplic(),
		    aplValoreParamApplic.getIdValoreParamApplic());
	}
    }

    /**
     * @Transactional(rollbackOn = TransactionTestingException.class) => se viene sollevata
     *                           l'eccezione TransactionTestingException va in rollback
     */
    @Test
    void savingThrowsExceptionRollbackOn_shouldRollback() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	try {
	    service.saveThrowsRuntimeExceptionsWithRollbackOn(aplParamApplic, aplValoreParamApplic,
		    new TransactionRuntimeTestingException());
	} catch (Exception e) {
	    Assertions.assertNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	    Assertions.assertNull(
		    findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	}
    }

    /**
     * @Transactional(rollbackOn = TransactionTestingException.class) => se viene sollevata una
     *                           eccezione diversa da TransactionTestingException che NON estende
     *                           RuntimeException non fa nulla
     */
    @Test
    void savingThrowsAnotherExceptionRollbackOn_shouldSave() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	try {
	    service.saveThrowsCustomExceptionsWithRollbackOn(aplParamApplic, aplValoreParamApplic);
	} catch (Exception e) {
	    Assertions.assertNotNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	    Assertions.assertNotNull(
		    findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	    delete(aplParamApplic.getIdParamApplic(),
		    aplValoreParamApplic.getIdValoreParamApplic());
	}
    }

    /**
     * @Transactional(rollbackOn = TransactionTestingException.class) => se viene sollevata una T
     *                           extend RuntimeException, anche se diversa da quella indicata dal
     *                           rollbackOn, fa comunque rollback
     */
    @Test
    void savingThrowsAnyRuntimeExceptionRollbackOn_shouldRollback() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	try {
	    service.saveThrowsRuntimeExceptionsWithRollbackOn(aplParamApplic, aplValoreParamApplic,
		    new AnotherRuntimeTestingException());
	} catch (Exception e) {
	    Assertions.assertNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	    Assertions.assertNull(
		    findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	}
    }

    /**
     * @Transactional(dontRollbackOn = TransactionTestingException.class) => se viene sollevata una
     *                               TransactionTestingException non fa nulla
     */
    @Test
    void savingThrowsExceptionDontRollbackOn_shouldSave() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	try {
	    service.saveThrowsRuntimeExceptionsWithDontRollbackOn(aplParamApplic,
		    aplValoreParamApplic, new TransactionRuntimeTestingException());
	} catch (Exception e) {
	    Assertions.assertNotNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	    Assertions.assertNotNull(
		    findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	    delete(aplParamApplic.getIdParamApplic(),
		    aplValoreParamApplic.getIdValoreParamApplic());
	}
    }

    /**
     * @Transactional(dontRollbackOn = TransactionTestingException.class) => se viene sollevata
     *                               un'eccezione diversa da TransactionTestingException fa rollback
     */
    @Test
    void savingThrowsAnotherExceptionDontRollbackOn_shouldRollback() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	try {
	    service.saveThrowsRuntimeExceptionsWithDontRollbackOn(aplParamApplic,
		    aplValoreParamApplic, new AnotherRuntimeTestingException());
	} catch (Exception e) {
	    Assertions.assertNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	    Assertions.assertNull(
		    findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	}
    }

    /**
     * @Transaction => quando l'eccezione è gestita in un try-catch non entrano in gioco i rollback
     */
    @Test
    void savingHandledException_shouldSave() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	service.saveHandleExceptions(aplParamApplic, aplValoreParamApplic,
		new TransactionRuntimeTestingException());
	Assertions.assertNotNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	Assertions.assertNotNull(
		findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	delete(aplParamApplic.getIdParamApplic(), aplValoreParamApplic.getIdValoreParamApplic());
    }

    /**
     * @Transactional(rollbackOn = TransactionTestingException.class) => quando l'eccezione è
     *                           gestita in un try-catch non entrano in gioco i rollback
     */
    @Test
    void savingHandledExceptionWithRollbackOn_shouldSave() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	service.saveHanldeExceptionsWithRollbackOn(aplParamApplic, aplValoreParamApplic,
		new TransactionRuntimeTestingException());
	Assertions.assertNotNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	Assertions.assertNotNull(
		findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	delete(aplParamApplic.getIdParamApplic(), aplValoreParamApplic.getIdValoreParamApplic());
    }

    /**
     * @Transactional(dontRollbackOn = TransactionTestingException.class) => quando l'eccezione è
     *                               gestita in un try-catch non entrano in gioco i rollback
     */
    @Test
    void savingHandledExceptionWithDontRollbackOn_shouldSave() {
	final AplParamApplic aplParamApplic = createAplParamApplic();
	final AplValoreParamApplic aplValoreParamApplic = createAplValoreParamApplic();
	service.saveHanldeExceptionsWithDontRollbackOn(aplParamApplic, aplValoreParamApplic,
		new TransactionRuntimeTestingException());
	Assertions.assertNotNull(findAplParamApplic(aplParamApplic.getIdParamApplic()));
	Assertions.assertNotNull(
		findAplValoreParamApplic(aplValoreParamApplic.getIdValoreParamApplic()));
	delete(aplParamApplic.getIdParamApplic(), aplValoreParamApplic.getIdValoreParamApplic());
    }

    private AplParamApplic createAplParamApplic() {
	final AplParamApplic aplParamApplic = new AplParamApplic();
	aplParamApplic.setNmParamApplic("TEST_TRANSACTION_" + UUID.randomUUID());
	aplParamApplic.setDsParamApplic("Per testare la rollback delle tranasazioni");
	aplParamApplic.setTiParamApplic(ParametroApplDB.TipoParametroAppl.LOG_APPLIC);
	aplParamApplic.setTiGestioneParam("amministrazione");
	aplParamApplic.setFlAppartApplic("0");
	aplParamApplic.setFlAppartAmbiente("0");
	aplParamApplic.setFlAppartStrut("0");
	aplParamApplic.setFlAppartTipoUnitaDoc("0");
	aplParamApplic.setFlAppartAaTipoFascicolo("0");
	aplParamApplic.setFlMulti("0");
	return aplParamApplic;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    AplParamApplic findAplParamApplic(Long idParamApplic) {
	return em.find(AplParamApplic.class, idParamApplic);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    AplValoreParamApplic findAplValoreParamApplic(Long idValore) {
	return em.find(AplValoreParamApplic.class, idValore);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void delete(Long idParamApplic, Long idValoreParamApplic) {
	em.remove(em.find(AplValoreParamApplic.class, idValoreParamApplic));
	em.remove(em.find(AplParamApplic.class, idParamApplic));
    }

    private AplValoreParamApplic createAplValoreParamApplic() {
	AplValoreParamApplic aplValoreParamApplic = new AplValoreParamApplic();
	aplValoreParamApplic.setDsValoreParamApplic("Per testare la rollback delle tranasazioni");
	aplValoreParamApplic.setTiAppart("APPLIC");
	return aplValoreParamApplic;
    }

}
