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
package it.eng.parer.fascicolo.runner.providers;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.http.HttpServerRequest;
import it.eng.parer.fascicolo.beans.IVersFascicoloService;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.VoceDiErrore;
import it.eng.parer.fascicolo.beans.utils.AvanzamentoWs;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.runner.util.IVersamentoFascicoloMultipartForm;
import it.eng.parer.fascicolo.runner.util.RequestPrsr;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoChiamataWSType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoPosNegNesType;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/*
 * ExceptionMapper delegato alla raccolta degli errori di tipo ConstraintViolationException che si
 * possono verificare a vari livelli (validazione form, chiamata a metodo mancante parametri
 * previsti, ecc.).
 */
@Provider
public class ConstraintViolationExceptionMapperProvider
	implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger log = LoggerFactory
	    .getLogger(ConstraintViolationExceptionMapperProvider.class);

    @Inject
    IVersFascicoloService versFascicoloSync;

    @Context
    HttpServerRequest request;

    private RispostaWSFascicolo rispostaWSFascicolo;
    private VersFascicoloExt versFascicoloExt;
    private BlockingFakeSession blockingFakeSession;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
	// log
	log.atError().log("ConstraintViolationExceptionMapperProvider errore registrato",
		exception);
	// init
	AvanzamentoWs avanzamentoWs = init();
	// esito
	CompRapportoVersFascicolo myEsito = rispostaWSFascicolo.getCompRapportoVersFascicolo();

	// check possible error after init
	if (rispostaWSFascicolo.getSeverity() == IRispostaWS.SeverityEnum.ERROR) {
	    versFascicoloExt.listErrAddError(StringUtils.EMPTY,
		    myEsito.getEsitoGenerale().getCodiceErrore(),
		    myEsito.getEsitoGenerale().getMessaggioErrore());
	}

	// default
	rispostaWSFascicolo.setErrorType(IRispostaWS.ErrorTypeEnum.WS_SIGNATURE);

	// check violation errors
	exception.getConstraintViolations().forEach(c -> {
	    if (c.getLeafBean() instanceof IVersamentoFascicoloMultipartForm) {
		versFascicoloExt.listErrAddError(StringUtils.EMPTY, MessaggiWSBundle.WS_CHECK,
			c.getMessage());
	    } else {
		// default
		versFascicoloExt.listErrAddError(StringUtils.EMPTY, MessaggiWSBundle.ERR_666,
			c.getMessage());
	    }
	});

	// calcolo errore principale
	VoceDiErrore tmpVdE = versFascicoloExt.calcolaErrorePrincipale();
	rispostaWSFascicolo.setSeverity(IRispostaWS.SeverityEnum.ERROR);
	rispostaWSFascicolo.setEsitoWsError(tmpVdE.getErrorCode(), tmpVdE.getErrorMessage());
	rispostaWSFascicolo.setErrorMessage(
		MessaggiWSBundle.getString(tmpVdE.getErrorCode(), tmpVdE.getErrorMessage()));

	// gestione sessione
	blockingFakeSession.setTmApertura(ZonedDateTime.now());
	blockingFakeSession.setIpChiamante(RequestPrsr.leggiIpVersante(request, avanzamentoWs));
	blockingFakeSession.setTmChiusura(ZonedDateTime.now());

	// esito chiamata NEGATIVO / il resto non eseguito
	myEsito.setEsitoChiamataWS(new ECEsitoChiamataWSType());
	myEsito.getEsitoChiamataWS().setCodiceEsito(ECEsitoPosNegNesType.NEGATIVO);
	myEsito.getEsitoChiamataWS().setCredenzialiOperatore(ECEsitoPosNegNesType.NON_ESEGUITO);
	myEsito.getEsitoChiamataWS().setVersioneWSCorretta(ECEsitoPosNegNesType.NON_ESEGUITO);

	// gestione errori ulteriori
	myEsito.setErroriUlteriori(versFascicoloExt.produciEsitoErroriSec());
	myEsito.setWarningUlteriori(versFascicoloExt.produciEsitoWarningSec());

	avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.VERIFICA_STRUTTURA_CHIAMATA_WS)
		.setFase("completata").logAvanzamento();

	// salva sessione
	versFascicoloSync.salvaTutto(blockingFakeSession, rispostaWSFascicolo, versFascicoloExt);

	return Response.status(Response.Status.BAD_REQUEST).entity(myEsito.produciEsitoFascicolo())
		.build();
    }

    /**
     *
     */
    private AvanzamentoWs init() {
	rispostaWSFascicolo = new RispostaWSFascicolo();
	versFascicoloExt = new VersFascicoloExt();
	blockingFakeSession = new BlockingFakeSession();

	// init
	AvanzamentoWs avanzamentoWs = versFascicoloSync.init(rispostaWSFascicolo, versFascicoloExt);
	avanzamentoWs.logAvanzamento(true);
	return avanzamentoWs;
    }

}
