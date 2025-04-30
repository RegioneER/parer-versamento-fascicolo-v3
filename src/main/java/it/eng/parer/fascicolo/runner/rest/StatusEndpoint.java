package it.eng.parer.fascicolo.runner.rest;

import static it.eng.parer.fascicolo.runner.util.EndPointCostants.URL_GET_STATUS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.health.SmallRyeHealthReporter;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

/*
 * Endpoint introdotto secondo specifiche standard di pubblicazione WSO2 (ApiManager) (vedi https://italia.github.io/api-oas-checker).
 * Espone lo stesso contenuto dell'API standard /q/health/live (vedi https://quarkus.io/guides/smallrye-health)
 */
@Tag(name = "Stato applicazione", description = "Stato dell'applicazione")
@SecurityScheme(securitySchemeName = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@RequestScoped
@Path(URL_GET_STATUS)
public class StatusEndpoint {

    private final SmallRyeHealthReporter reporter;

    public StatusEndpoint(SmallRyeHealthReporter reporter) {
        this.reporter = reporter;
    }

    @Operation(summary = "Stato applicazione", description = "Stato applicazione")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Stato applicazione", content = @Content(mediaType = "application/problem+json")),
            @APIResponse(responseCode = "500", description = "Errore interno"),
            @APIResponse(responseCode = "503", description = "Servizio non disponibile") })
    @GET
    @Produces("application/problem+json")
    @Blocking
    public Response status() {
        // Get liveness payload
        return Response.ok(reporter.getLiveness().getPayload())
                .lastModified(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())).build();
    }
}
