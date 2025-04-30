/**
 *
 */
package it.eng.parer.fascicolo.runner.filters;

import java.util.Optional;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import it.eng.parer.fascicolo.beans.utils.UUIDMdcLogUtil;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.container.ContainerRequestContext;

/*
 * Filtro che intercetta le chiamate di tipo POST e inietta sull'MCD un UUID
 */
public class UUIDFilter {

    @ServerRequestFilter
    public Optional<RestResponse<Void>> getFilter(ContainerRequestContext ctx) {

        if (ctx.getMethod().equals(HttpMethod.POST)) {
            UUIDMdcLogUtil.genUuid();
        }

        return Optional.empty();
    }
}
