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

/**
 *
 */
package it.eng.parer.lab;

import it.eng.parer.fascicolo.jpa.entity.AplParamApplic;
import it.eng.parer.fascicolo.jpa.entity.AplValoreParamApplic;

public interface ITransactionalService {

    void saveThrowsRuntimeExceptions(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

    void saveThrowsCustomExceptions(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic) throws TransactionTestingException;

    void saveThrowsRuntimeExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

    void saveThrowsCustomExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic) throws TransactionTestingException;

    void saveThrowsRuntimeExceptionsWithDontRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

    void saveHandleExceptions(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

    void saveHanldeExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

    void saveHanldeExceptionsWithDontRollbackOn(AplParamApplic aplParamApplic,
	    AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

}
