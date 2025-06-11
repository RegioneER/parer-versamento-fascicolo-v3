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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.http.HttpServerRequest;
import it.eng.parer.fascicolo.beans.IVersFascicoloService;
import it.eng.parer.fascicolo.beans.dto.CompRapportoVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.IRispostaWS;
import it.eng.parer.fascicolo.beans.dto.base.VoceDiErrore;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.exceptions.xml.IXmlSipValidationException;
import it.eng.parer.fascicolo.beans.exceptions.xml.XmlSipNotWellFormedException;
import it.eng.parer.fascicolo.beans.exceptions.xml.XmlSipUnmarshalException;
import it.eng.parer.fascicolo.beans.utils.AvanzamentoWs;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.xml.XmlDateUtility;
import it.eng.parer.fascicolo.runner.util.RequestPrsr;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoChiamataWSType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoPosNegNesType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoPosNegType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoXSDType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/*
 * ExceptionMapper che gestisce gli errori di tipoo WebApplicationException, nel caso specifico si
 * deve verificare la logica del provider CustomJaxbMessageBodyReader il quale, come da default,
 * utilizza questo tipo di eccezioni.
 */
@Provider
public class AppGenericRuntimeExceptionMapperProvider
	implements ExceptionMapper<AppGenericRuntimeException> {

    private static final Logger log = LoggerFactory
	    .getLogger(AppGenericRuntimeExceptionMapperProvider.class);

    @Inject
    IVersFascicoloService versFascicoloSync;

    @Context
    HttpServerRequest request;

    private RispostaWSFascicolo rispostaWSFascicolo;
    private VersFascicoloExt versFascicoloExt;
    private BlockingFakeSession blockingFakeSession;

    @Override
    public Response toResponse(AppGenericRuntimeException exception) {
	// log
	log.atError().log("AppGenericRuntimeExceptionMapperProvider errore registrato", exception);

	// default response status
	Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

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

	// xml not well formed
	if (exception.getCause() instanceof XmlSipNotWellFormedException) {
	    // status
	    status = Response.Status.BAD_REQUEST;
	    //
	    versFascicoloExt.listErrAddError(StringUtils.EMPTY, MessaggiWSBundle.FAS_XSD_001_001,
		    MessaggiWSBundle.getString(MessaggiWSBundle.FAS_XSD_001_001,
			    ExceptionUtils.getRootCauseMessage(exception)));

	    // se non riesco a convalidare l'XML magari posso provare a leggerlo
	    // in modo meno preciso e tentare di riscostruire la struttura, la chiave ed il
	    // tipo fascicolo
	    // e trasformare la sessione errata in una sessione fallita
	    rispostaWSFascicolo
		    .setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	} else if (exception.getCause() instanceof XmlSipUnmarshalException) {
	    // status
	    status = Response.Status.BAD_REQUEST;
	    //
	    versFascicoloExt.listErrAddError(StringUtils.EMPTY, MessaggiWSBundle.FAS_XSD_001_002,
		    MessaggiWSBundle.getString(MessaggiWSBundle.FAS_XSD_001_002,
			    ExceptionUtils.getRootCauseMessage(exception)));

	    // se non riesco a convalidare l'XML magari posso provare a leggerlo
	    // in modo meno preciso e tentare di riscostruire la struttura, la chiave ed il
	    // tipo fascicolo
	    // e trasformare la sessione errata in una sessione fallita
	    rispostaWSFascicolo
		    .setStatoSessioneVersamento(IRispostaWS.StatiSessioneVersEnum.DUBBIA);
	} else {
	    // default
	    versFascicoloExt.listErrAddError(StringUtils.EMPTY, MessaggiWSBundle.ERR_666,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
			    ExceptionUtils.getRootCauseMessage(exception)));

	}

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

	// xml del sip recuperato dalla custom exception
	if (exception.getCause() instanceof IXmlSipValidationException) {
	    String xmlSip = ((IXmlSipValidationException) exception.getCause()).getXmlSip();
	    versFascicoloExt.setDatiXml(xmlSip);
	} else {
	    versFascicoloExt.setDatiXml(StringUtils.EMPTY);
	}
	versFascicoloExt.setStrutturaComponenti(new StrutturaVersFascicolo());
	versFascicoloExt.getStrutturaComponenti().setDataVersamento(XmlDateUtility
		.xmlGregorianCalendarToDateOrNull(myEsito.getDataRapportoVersamento()));

	// esito chiamata NEGATIVO / il resto non eseguito
	myEsito.setEsitoChiamataWS(new ECEsitoChiamataWSType());
	myEsito.getEsitoChiamataWS().setCodiceEsito(ECEsitoPosNegNesType.NEGATIVO);
	myEsito.getEsitoChiamataWS().setCredenzialiOperatore(ECEsitoPosNegNesType.NON_ESEGUITO);
	myEsito.getEsitoChiamataWS().setVersioneWSCorretta(ECEsitoPosNegNesType.NON_ESEGUITO);
	//
	myEsito.setEsitoXSD(new ECEsitoXSDType());
	myEsito.getEsitoXSD().setCodiceEsito(ECEsitoPosNegType.NEGATIVO);
	// gestione errori ulteriori
	myEsito.setErroriUlteriori(versFascicoloExt.produciEsitoErroriSec());
	myEsito.setWarningUlteriori(versFascicoloExt.produciEsitoWarningSec());

	avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.VERIFICA_XML).setFase("completata")
		.logAvanzamento();

	// salva sessione
	versFascicoloSync.salvaTutto(blockingFakeSession, rispostaWSFascicolo, versFascicoloExt);

	return Response.status(status).entity(myEsito.produciEsitoFascicolo()).build();
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
