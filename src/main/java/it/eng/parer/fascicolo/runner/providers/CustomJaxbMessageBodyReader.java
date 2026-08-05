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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.validation.Schema;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.reactive.server.spi.ServerRequestContext;

import io.quarkus.resteasy.reactive.jaxb.runtime.serialisers.ServerJaxbMessageBodyReader;
import it.eng.parer.fascicolo.beans.XmlFascCache;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.exceptions.xml.XmlSipUnmarshalException;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import it.eng.parer.fascicolo.beans.utils.xml.WsXmlValidationEventHandler;
import it.eng.parer.fascicolo.beans.utils.xml.XmlUtils;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/*
 * Provider che estende JaxbMessageBodyReader per ampliarne la logica, aggiungendo: validazione con
 * xsd e gestione dell'encoding dichiarato nell'XML.
 *
 * Strategia di lettura: 1. Si leggono solo i primi 300 bytes per rilevare l'encoding dalla
 * dichiarazione XML (la dichiarazione è sempre ASCII-compatibile, 300 bytes sono sempre
 * sufficienti). 2. Lo stream originale viene ricostruito con SequenceInputStream (header + resto).
 * 3. JAXB consuma lo stream a chunk interni: il payload completo non viene mai materializzato in
 * heap come byte[] o String.
 */
@Provider
public class CustomJaxbMessageBodyReader extends ServerJaxbMessageBodyReader {

    @Inject
    XmlFascCache xmlFascCache;

    private static final int XML_HEADER_READ_BYTES = 300;

    @Override
    public IndiceSIPFascicolo readFrom(Class<Object> type, Type genericType, MediaType mediaType,
            ServerRequestContext context) throws WebApplicationException, IOException {
        try {
            InputStream requestStream = context.getInputStream();
            // Leggo solo l'header per rilevare l'encoding dichiarato
            byte[] header = requestStream.readNBytes(XML_HEADER_READ_BYTES);
            if (header.length == 0) {
                return null;
            }
            // La dichiarazione XML è ASCII-compatibile: lettura US_ASCII è sempre sicura
            String xmlDeclaration = new String(header, StandardCharsets.US_ASCII);
            XmlUtils.getXmlEcondingDeclaration(xmlDeclaration); // valida la dichiarazione

            // Ricostruisco lo stream completo: header già letto + resto non ancora letto.
            // JAXB leggerà a chunk — l'XML non viene mai caricato interamente in heap.
            InputStream fullStream = new SequenceInputStream(new ByteArrayInputStream(header),
                    requestStream);

            return doReadFromWithValidation(fullStream, header);
        } catch (XMLStreamException | FactoryConfigurationError | XmlSipUnmarshalException e) {
            throw new AppGenericRuntimeException(e, ErrorCategory.VALIDATION_ERROR);
        }
    }

    private IndiceSIPFascicolo doReadFromWithValidation(InputStream entityStream, byte[] xmlHeader)
            throws XmlSipUnmarshalException {
        WsXmlValidationEventHandler ve = new WsXmlValidationEventHandler();
        try {
            JAXBContext jaxbContext = xmlFascCache.getVersReqFascicoloCtx();
            Schema schema = xmlFascCache.getVersReqFascicoloSchema();
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            unmarshaller.setEventHandler(ve);
            return (IndiceSIPFascicolo) unmarshaller.unmarshal(entityStream);
        } catch (JAXBException e) {
            // In caso di errore forniamo i primi byte dell'XML per diagnostica
            String partialXml = xmlHeader != null ? new String(xmlHeader, StandardCharsets.UTF_8)
                    : StringUtils.EMPTY;
            throw new XmlSipUnmarshalException(ve.getMessaggio(), e, partialXml);
        }
    }
}
