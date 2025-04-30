/**
 *
 */
package it.eng.parer.fascicolo.runner.providers;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/*
 * ExceptionMapper che gestisce l'http 404 qualora venga richiamata una risorsa non esistente (risposta vuota con il
 * relativo http error code)
 */
@Provider
public class NotFoundExeptionMapperProvider implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(404).build();
    }

}
