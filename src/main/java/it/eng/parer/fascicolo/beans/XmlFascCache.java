/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.Lock;
import io.quarkus.runtime.Startup;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;
import it.eng.parer.ws.xml.versfascicoloV3.ProfiloSpecificoType;
import it.eng.parer.ws.xml.versfascicolorespV3.EsitoVersamentoFascicolo;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import jakarta.xml.bind.JAXBContext;

/**
 *
 * @author fioravanti_f
 */
@Lock
@Singleton
@Startup
public class XmlFascCache {

    private static final Logger log = LoggerFactory.getLogger(XmlFascCache.class);

    private static final String URL_SCHEMA_REQUEST_FASCICOLO = "xsd/WSRequestIndiceSIPFascicolo.xsd";

    //
    JAXBContext versReqFascicoloCtx;
    JAXBContext versRespFascicoloCtx;
    // profilo spec
    JAXBContext versReqFascicoloCtxProfiloSpec;

    Schema versReqFascicoloSchema;

    @PostConstruct
    protected void initSingleton() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(URL_SCHEMA_REQUEST_FASCICOLO)) {
            log.atInfo().log("Inizializzazione singleton XmlFascCache...");

            versReqFascicoloCtx = JAXBContext.newInstance(IndiceSIPFascicolo.class);
            versRespFascicoloCtx = JAXBContext.newInstance(EsitoVersamentoFascicolo.class);

            // profili
            versReqFascicoloCtxProfiloSpec = JAXBContext.newInstance(ProfiloSpecificoType.class);

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            versReqFascicoloSchema = sf.newSchema(new StreamSource(is));
            log.atInfo().log("Inizializzazione singleton XmlFascCache... completata.");
        } catch (Exception ex) {
            throw new AppGenericRuntimeException(ex, ErrorCategory.INTERNAL_ERROR);
        }
    }

    @Lock(value = Lock.Type.READ)
    public JAXBContext getVersReqFascicoloCtx() {
        return versReqFascicoloCtx;
    }

    @Lock(value = Lock.Type.READ)
    public JAXBContext getVersRespFascicoloCtx() {
        return versRespFascicoloCtx;
    }

    @Lock(value = Lock.Type.READ)
    public JAXBContext getVersReqFascicoloCtxProfiloSpec() {
        return versReqFascicoloCtxProfiloSpec;
    }

    @Lock(value = Lock.Type.READ)
    public Schema getVersReqFascicoloSchema() {
        return versReqFascicoloSchema;
    }

}
