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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.fascicolo.jpa.entity.AplParamApplic;
import it.eng.parer.fascicolo.jpa.entity.AplValoreParamApplic;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransactionalService implements ITransactionalService {

    @Inject
    IAplParamApplicTestDao paramDao;

    @Inject
    IAplValoreParamApplicTestDao valoreDao;

    private static final Logger log = LoggerFactory.getLogger(TransactionalService.class);

    @Override
    @Transactional
    public void saveThrowsRuntimeExceptions(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception) {
	saveAll(aplParamApplic, aplValoreParamApplic, exception);
    }

    @Override
    @Transactional
    public void saveThrowsCustomExceptions(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic) throws TransactionTestingException {
	saveAll(aplParamApplic, aplValoreParamApplic, null);
	throw new TransactionTestingException();
    }

    @Override
    @Transactional(rollbackOn = TransactionRuntimeTestingException.class)
    public void saveThrowsRuntimeExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception) {
	saveAll(aplParamApplic, aplValoreParamApplic, exception);
    }

    @Override
    @Transactional(rollbackOn = TransactionRuntimeTestingException.class)
    public void saveThrowsCustomExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic) throws TransactionTestingException {
	saveAll(aplParamApplic, aplValoreParamApplic, null);
	throw new TransactionTestingException();
    }

    @Override
    @Transactional(dontRollbackOn = TransactionRuntimeTestingException.class)
    public void saveThrowsRuntimeExceptionsWithDontRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception) {
	saveAll(aplParamApplic, aplValoreParamApplic, exception);
    }

    @Override
    @Transactional
    public void saveHandleExceptions(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception) {
	try {
	    saveAll(aplParamApplic, aplValoreParamApplic, exception);
	} catch (TransactionRuntimeTestingException e) {
	    log.info("Eccezione gestita");
	}
    }

    @Override
    @Transactional(rollbackOn = TransactionRuntimeTestingException.class)
    public void saveHanldeExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception) {
	try {
	    saveAll(aplParamApplic, aplValoreParamApplic, exception);
	} catch (TransactionRuntimeTestingException e) {
	    log.info("Eccezione gestita");
	}
    }

    @Override
    @Transactional(dontRollbackOn = TransactionRuntimeTestingException.class)
    public void saveHanldeExceptionsWithDontRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception) {
	try {
	    saveAll(aplParamApplic, aplValoreParamApplic, exception);
	} catch (TransactionRuntimeTestingException e) {
	    log.info("Eccezione gestita");
	}
    }

    private void saveAll(AplParamApplic aplParamApplic, AplValoreParamApplic aplValoreParamApplic,
	    RuntimeException exception) {
	final AplParamApplic savedAplParamApplic = paramDao.save(aplParamApplic);
	aplValoreParamApplic.setAplParamApplic(savedAplParamApplic);
	valoreDao.save(aplValoreParamApplic);
	if (exception != null) {
	    throw exception;
	}
    }
}
