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

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans;

import java.io.InputStream;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

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

    // cache degli Schema compilati da XSD del DB, indicizzati per hash del contenuto XSD
    private final ConcurrentHashMap<Integer, Schema> xsdSchemaCache = new ConcurrentHashMap<>();

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

    /**
     * Restituisce uno Schema compilato dall'XSD fornito, con caching basato sul contenuto. Il
     * compilazione avviene solo la prima volta per ogni XSD distinto.
     *
     * @param xsd      contenuto dell'XSD come stringa
     * @param resolver resource resolver per XSD multi-namespace (può essere null)
     * @return Schema compilato
     * @throws SAXException se l'XSD non è valido
     */
    @Lock(value = Lock.Type.READ)
    public Schema getOrCompileSchema(String xsd, LSResourceResolver resolver) throws SAXException {
        int key = xsd.hashCode();
        Schema cached = xsdSchemaCache.get(key);
        if (cached != null) {
            return cached;
        }
        // compilazione (può avvenire in concorrenza per lo stesso xsd la prima volta — accettabile)
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (org.xml.sax.SAXNotRecognizedException
                | org.xml.sax.SAXNotSupportedException ignored) {
            // non tutti gli schemaFactory supportano questa feature
        }
        if (resolver != null) {
            factory.setResourceResolver(resolver);
        }
        Schema compiled = factory.newSchema(new StreamSource(new StringReader(xsd)));
        xsdSchemaCache.putIfAbsent(key, compiled);
        return xsdSchemaCache.get(key);
    }

}
