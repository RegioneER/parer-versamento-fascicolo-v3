/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.runner.util;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.http.HttpServerRequest;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.utils.AvanzamentoWs;
import it.eng.parer.fascicolo.runner.rest.input.VersamentoFascicoloOauth2MultipartForm;
import it.eng.parer.fascicolo.runner.rest.input.VersamentoFascicoloStdMultipartForm;

/**
 * Parser di request di tipo POST/MULTIPART FORM DATA
 *
 */
public class RequestPrsr {

    private static final Logger log = LoggerFactory.getLogger(RequestPrsr.class);

    private RequestPrsr() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Revisited: il metodo si limita a impostare le property dell'oggetto {@link BlockingFakeSession} recepecita dal
     * wrapper del multipart versato {@link VersamentoFascicoloStdMultipartForm}
     *
     * @param syncFakeSession
     *            oggetto di sessione
     * @param avanzamentoWs
     *            oggetto che traccia l'avanzamento logico dell'endpoint
     * @param formData
     *            oggetto multipart/form-data generico
     *
     */
    public static void parseStdForm(BlockingFakeSession syncFakeSession, AvanzamentoWs avanzamentoWs,
            VersamentoFascicoloStdMultipartForm formData) {

        avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.TRASFERIMENTO_PAYLOAD_IN)
                .setFase("Payload verificato ricevuto").logAvanzamento();

        /*
         * verifica della struttura della chiamata al WS: non è un WS SOAP perciò la signature del WS va controllata a
         * mano, leggendo quanto effettivamente versato.
         */
        // verifica strutturale del campo VERSIONE e memorizzazione dello stesso nella
        // sessione finta

        String versione = formData.VERSIONE;
        log.debug("VERSIONE {}", versione);
        syncFakeSession.setVersioneWS(versione);

        // verifica strutturale del campo LOGINNAME e memorizzazione dello stesso nella
        // sessione finta
        String loginame = formData.LOGINNAME;
        log.debug("LOGINNAME {}", loginame);
        syncFakeSession.setLoginName(loginame);
        avanzamentoWs.setVrsUser(loginame).logAvanzamento();

        // verifica strutturale del campo PASSWORD e memorizzazione dello stesso nella
        // sessione finta
        String pwd = formData.PASSWORD;
        log.debug("PASSWORD {}", pwd);
        syncFakeSession.setPassword(pwd);

        // verifica strutturale del campo XMLSIP e memorizzazione dello stesso nella
        // sessione finta
        String xmlsip = formData.XMLSIP;
        log.debug("XMLSIP {}", xmlsip);
        syncFakeSession.setDatiIndiceSipXml(xmlsip);
        syncFakeSession.setDatiDaSalvareIndiceSip(xmlsip);

        // indice sip (xml serialized)
        syncFakeSession.setIndiceSIPFascicolo(formData.indiceSIPFascicolo);
    }

    public static void parseOAuth2Form(BlockingFakeSession syncFakeSession, AvanzamentoWs avanzamentoWs,
            VersamentoFascicoloOauth2MultipartForm formData, Principal principal) {

        avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.TRASFERIMENTO_PAYLOAD_IN)
                .setFase("Payload verificato ricevuto").logAvanzamento();

        /*
         * verifica della struttura della chiamata al WS: non è un WS SOAP perciò la signature del WS va controllata a
         * mano, leggendo quanto effettivamente versato.
         */
        // verifica strutturale del campo VERSIONE e memorizzazione dello stesso nella
        // sessione finta

        String versione = formData.VERSIONE;
        log.debug("VERSIONE {}", versione);
        syncFakeSession.setVersioneWS(versione);

        // verifica strutturale del campo LOGINNAME e memorizzazione dello stesso nella
        // sessione finta
        String loginame = principal.getName();
        log.debug("OAUTH2 LOGINNAME {}", loginame);
        syncFakeSession.setLoginName(loginame);
        avanzamentoWs.setVrsUser(loginame).logAvanzamento();

        // verifica strutturale del campo XMLSIP e memorizzazione dello stesso nella
        // sessione finta
        String xmlsip = formData.XMLSIP;
        log.debug("XMLSIP {}", xmlsip);
        syncFakeSession.setDatiIndiceSipXml(xmlsip);
        syncFakeSession.setDatiDaSalvareIndiceSip(xmlsip);

        // indice sip (xml serialized)
        syncFakeSession.setIndiceSIPFascicolo(formData.indiceSIPFascicolo);
    }

    /**
     * lettura dell'indirizzo IP del chiamante. Si presuppone che il load balancer o il reverse proxy impostino la
     * variabile RERFwFor tra gli header HTTP della request. Questo è un tag custom messo a punto dalla RER per
     * compensare ai possibili rischi legati all'uso dell'header X-FORWARDED-FOR di uso più comune. Da notare che
     * qualora l'header RERFwFor non fosse valorizzato, il codice ripiegherà cercando X-FORWARDED-FOR tra gli header
     * HTTP della request. Questo è l'unico sistema per recepire l'IP nel caso in cui l'application server non sia
     * esposto direttamente. NOTA: è ovvio che l'application server è esposto direttamente solo sui PC di sviluppo.
     *
     * @param request
     *            standard {@link HttpServerRequest}
     * @param avanzamentoWs
     *            avanzamento ws
     *
     * @return ip client letto ottenuto da request
     */
    public static String leggiIpVersante(HttpServerRequest request, AvanzamentoWs avanzamentoWs) {
        String ipVers = request.getHeader("RERFwFor");
        // cerco l'header custom della RER
        if (ipVers == null || ipVers.isEmpty()) {
            ipVers = request.getHeader("X-FORWARDED-FOR");
            // se non c'e`, uso l'header standard
        }
        if (ipVers == null || ipVers.isEmpty()) {
            ipVers = request.remoteAddress().hostAddress();
            // se non c'e` perche' la macchina e' esposta direttamente,
            // leggo l'IP fisico del chiamante
        }
        log.debug("Request, indirizzo di provenienza - IP: {}", ipVers);
        avanzamentoWs.setCheckPoint(AvanzamentoWs.CheckPoints.LETTURA_IP_VERSANTE)
                .setFase("Lettura Ip Versante da header chiamata").setIndirizzoIp(ipVers).logAvanzamento(true);
        return ipVers;
    }

}
