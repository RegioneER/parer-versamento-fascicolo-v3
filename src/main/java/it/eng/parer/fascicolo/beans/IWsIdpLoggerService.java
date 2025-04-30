/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.idpjaas.logutils.LogDto;
import jakarta.validation.constraints.NotNull;

public interface IWsIdpLoggerService {

    void scriviLog(@NotNull(message = "IWsIdpLoggerService.scriviLog: logDto non inizializzato") LogDto logDto);

}