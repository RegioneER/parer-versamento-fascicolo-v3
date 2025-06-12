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

package it.eng.parer.fascicolo.runner.rest;

import static it.eng.parer.fascicolo.runner.util.EndPointCostants.URL_FASCICOLO_BASE;
import static it.eng.parer.fascicolo.runner.util.EndPointCostants.URL_OAUTH_2_FASCICOLO_V3;
import static it.eng.parer.fascicolo.runner.util.EndPointCostants.URL_PUBLIC_FASCICOLO_V3;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.http.HttpServerRequest;
import it.eng.parer.fascicolo.beans.IVersFascicoloService;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS.SeverityEnum;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.utils.AvanzamentoWs;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import it.eng.parer.fascicolo.beans.utils.xml.XmlDateUtility;
import it.eng.parer.fascicolo.runner.rest.input.VersamentoFascicoloOauth2MultipartForm;
import it.eng.parer.fascicolo.runner.rest.input.VersamentoFascicoloStdMultipartForm;
import it.eng.parer.fascicolo.runner.util.RequestPrsr;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoPosNegType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoXSDType;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoVersamentoFascicolo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Tag(name = "Versamento Fascicolo (sip xml versione 3)", description = "Servizio di versamento fascicoli (sip xml versione 3)")
@SecurityScheme(securitySchemeName = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
@RequestScoped
@Path(URL_FASCICOLO_BASE)
public class VersamentoFascicoloEndpoint {

    /* constants */
    private static final String ETAG = "fasc-v3.0";

    /* interfaces */
    private final IVersFascicoloService versFascicoloService;
    private final SecurityContext securityCtx;
    private final HttpServerRequest request;

    private RispostaWSFascicolo rispostaWSFascicolo;
    private VersFascicoloExt versFascicoloExt;
    private BlockingFakeSession blockingFakeSession;
    private AvanzamentoWs avanzamentoWs;

    @ConfigProperty(name = "quarkus.uuid")
    String instanceUUID;

    @Inject
    public VersamentoFascicoloEndpoint(IVersFascicoloService versFascicoloService,
	    SecurityContext securityCtx, HttpServerRequest request) {
	this.versFascicoloService = versFascicoloService;
	this.securityCtx = securityCtx;
	this.request = request;
    }

    @Operation(summary = "Versamento fascicolo v3 (con OAuth2)", description = "Versamento fascicolo versione 3 autenticato con token OAuth2")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
	    @APIResponse(responseCode = "200", description = "Versamento effettuato con successo", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = EsitoVersamentoFascicolo.class))),
	    @APIResponse(responseCode = "400", description = "Richiesta non valida (XML non valido, errore di validazione con xsd)", content = @Content(mediaType = "application/problem+xml", schema = @Schema(implementation = EsitoVersamentoFascicolo.class))),
	    @APIResponse(responseCode = "401", description = "Autenticazione fallita"),
	    @APIResponse(responseCode = "403", description = "Non autorizzato ad accedere al servizio"),
	    @APIResponse(responseCode = "500", description = "Errore generico (richiesta non valida secondo specifiche)", content = @Content(mediaType = "application/problem+xml", schema = @Schema(implementation = EsitoVersamentoFascicolo.class))) })
    @POST
    @Path(URL_OAUTH_2_FASCICOLO_V3)
    @Produces("application/xml; charset=UTF-8")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Blocking
    public Response oauth2fascicolov3(@Valid VersamentoFascicoloOauth2MultipartForm formData) {

	// init
	LocalDateTime start = init();
	// result
	EsitoVersamentoFascicolo result = doVersamento(start, null, formData);
	//
	return Response.ok(result)
		.lastModified(Date.from(
			result.getDataEsitoVersamentoFascicolo().toGregorianCalendar().toInstant()))
		.tag(new EntityTag(ETAG)).build();
    }

    @Operation(summary = "Versamento fascicolo v3 (pubblico)", description = "Versamento fascicolo versione 3 pubblico, meccanismi di sicurezza standard")
    @APIResponses(value = {
	    @APIResponse(responseCode = "200", description = "Versamento effettuato con successo", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = EsitoVersamentoFascicolo.class))),
	    @APIResponse(responseCode = "400", description = "Richiesta non valida (XML non valido, errore di validazione con xsd)", content = @Content(mediaType = "application/problem+xml", schema = @Schema(implementation = EsitoVersamentoFascicolo.class))),
	    @APIResponse(responseCode = "500", description = "Errore generico (richiesta non valida secondo specifiche)", content = @Content(mediaType = "application/problem+xml", schema = @Schema(implementation = EsitoVersamentoFascicolo.class))) })
    @POST
    @Path(URL_PUBLIC_FASCICOLO_V3)
    @Produces("application/xml; charset=UTF-8")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Blocking
    public Response publicfascicolov3(@Valid VersamentoFascicoloStdMultipartForm formData) {

	// init
	LocalDateTime start = init();
	// result
	EsitoVersamentoFascicolo result = doVersamento(start, formData, null);
	//
	return Response.ok(result)
		.lastModified(Date.from(
			result.getDataEsitoVersamentoFascicolo().toGregorianCalendar().toInstant()))
		.tag(new EntityTag(ETAG)).build();
    }

    private EsitoVersamentoFascicolo doVersamento(LocalDateTime start,
	    VersamentoFascicoloStdMultipartForm unsecuredFormData,
	    VersamentoFascicoloOauth2MultipartForm securedformData) {
	// blocco try/catch per gestire qualunque eccezione runtime non prevista dalla
	// logica
	// il provider provvederà a fornire una risposta 666-standard
	try {
	    CompRapportoVersFascicolo myEsito = rispostaWSFascicolo.getCompRapportoVersFascicolo();

	    if (rispostaWSFascicolo.getSeverity() == IRispostaWS.SeverityEnum.OK) {

		avanzamentoWs.setFase("inzio logica di versamento").logAvanzamento();

		avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.TRASFERIMENTO_PAYLOAD_IN)
			.setFase("pronto a ricevere").logAvanzamento();
		// validazione formale dell'oggetta multipart
		// effettuta per validare la presenza dei campi (==null)
		if (unsecuredFormData != null) {
		    RequestPrsr.parseStdForm(blockingFakeSession, avanzamentoWs, unsecuredFormData);
		} else {
		    RequestPrsr.parseOAuth2Form(blockingFakeSession, avanzamentoWs, securedformData,
			    securityCtx.getUserPrincipal());
		}
		//
		avanzamentoWs
			.setCheckPoint(AvanzamentoWs.CheckPoints.VERIFICA_STRUTTURA_CHIAMATA_WS)
			.setFase("completata").logAvanzamento();

		/*
		 * *****************************************************************************
		 * fine della verifica della struttura/signature del web service. Verifica dei dati
		 * effettivamente versati
		 * ***************************************************************************** ***
		 */
		if (rispostaWSFascicolo.getSeverity() == SeverityEnum.OK) {
		    // dopo questo punto posso tentare di salvare la sessione di versamento
		    rispostaWSFascicolo
			    .setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.ERRATA);
		    //
		    versFascicoloExt.setDatiXml(blockingFakeSession.getDatiIndiceSipXml());
		    versFascicoloExt.setStrutturaComponenti(new StrutturaVersFascicolo());
		    versFascicoloExt.getStrutturaComponenti().setDataVersamento(XmlDateUtility
			    .xmlGregorianCalendarToDateOrNull(myEsito.getDataRapportoVersamento()));

		}

		/*
		 * Logica di verifica della versione SIP
		 */
		if (rispostaWSFascicolo.getSeverity() == SeverityEnum.OK) {
		    avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.VERIFICA_SEMANTICA)
			    .setFase("verifica versione").logAvanzamento();

		    versFascicoloService.verificaVersione(blockingFakeSession.getVersioneWS(),
			    rispostaWSFascicolo, versFascicoloExt);
		}

		/*
		 * Logica condivisa tra verifica "standard" e con token (OAut2+...futuri
		 * improvements)
		 */
		if (rispostaWSFascicolo.getSeverity() == SeverityEnum.OK) {
		    avanzamentoWs.setFase("verifica credenziali").logAvanzamento();
		    versFascicoloService.verificaCredenziali(blockingFakeSession.getLoginName(),
			    blockingFakeSession.getPassword(), blockingFakeSession.getIpChiamante(),
			    rispostaWSFascicolo, versFascicoloExt);
		}

		// verifica formale e semantica dell'XML di versamento
		if (rispostaWSFascicolo.getSeverity() == SeverityEnum.OK) {
		    myEsito.setEsitoXSD(new ECEsitoXSDType());
		    myEsito.getEsitoXSD().setCodiceEsito(ECEsitoPosNegType.POSITIVO);
		    //
		    avanzamentoWs.setFase("verifica xml").logAvanzamento();
		    versFascicoloService.parseXML(blockingFakeSession, rispostaWSFascicolo,
			    versFascicoloExt);
		}

		blockingFakeSession.setTmChiusura(ZonedDateTime.now());
		versFascicoloService.salvaTutto(blockingFakeSession, rispostaWSFascicolo,
			versFascicoloExt);

	    }

	    long totalTime = Duration.between(start, LocalDateTime.now()).toMillis();
	    avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.FINE).setFase("")
		    .setTotalTime(totalTime).logAvanzamento(true);
	    return myEsito.produciEsitoFascicolo();
	} catch (Exception e) {
	    throw new AppGenericRuntimeException(
		    "Errore generico in fase di versamento VersamentoFascicoloEndpoint.doVersamento",
		    e, ErrorCategory.INTERNAL_ERROR);
	}
    }

    private LocalDateTime init() {
	// blocco try/catch per gestire qualunque eccezione runtime non prevista dalla logica
	// il provider provvederà a fornire una risposta 666-standard
	try {
	    final LocalDateTime start = LocalDateTime.now();

	    // init
	    rispostaWSFascicolo = new RispostaWSFascicolo();
	    versFascicoloExt = new VersFascicoloExt();
	    blockingFakeSession = new BlockingFakeSession();

	    // init
	    avanzamentoWs = versFascicoloService.init(rispostaWSFascicolo, versFascicoloExt);
	    // visual log
	    avanzamentoWs.logAvanzamento(true);
	    //
	    blockingFakeSession.setTmApertura(ZonedDateTime.now());
	    blockingFakeSession.setIpChiamante(RequestPrsr.leggiIpVersante(request, avanzamentoWs));

	    return start;
	} catch (Exception e) {
	    throw new AppGenericRuntimeException(
		    "Errore generico in fase di versamento VersamentoFascicoloEndpoint.init", e,
		    ErrorCategory.INTERNAL_ERROR);
	}
    }

}
