/**
 *
 */
package it.eng.parer.lab;

import it.eng.parer.fascicolo.jpa.entity.AplParamApplic;
import it.eng.parer.fascicolo.jpa.entity.AplValoreParamApplic;

public interface ITransactionalService {

    void saveThrowsRuntimeExceptions(AplParamApplic aplParamApplic, AplValoreParamApplic aplValoreParamApplic,
            RuntimeException exception);

    void saveThrowsCustomExceptions(AplParamApplic aplParamApplic, AplValoreParamApplic aplValoreParamApplic)
            throws TransactionTestingException;

    void saveThrowsRuntimeExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
            AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

    void saveThrowsCustomExceptionsWithRollbackOn(AplParamApplic aplParamApplic,
            AplValoreParamApplic aplValoreParamApplic) throws TransactionTestingException;

    void saveThrowsRuntimeExceptionsWithDontRollbackOn(AplParamApplic aplParamApplic,
            AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

    void saveHandleExceptions(AplParamApplic aplParamApplic, AplValoreParamApplic aplValoreParamApplic,
            RuntimeException exception);

    void saveHanldeExceptionsWithRollbackOn(AplParamApplic aplParamApplic, AplValoreParamApplic aplValoreParamApplic,
            RuntimeException exception);

    void saveHanldeExceptionsWithDontRollbackOn(AplParamApplic aplParamApplic,
            AplValoreParamApplic aplValoreParamApplic, RuntimeException exception);

}
