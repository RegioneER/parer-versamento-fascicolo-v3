/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.util.Map;

import io.quarkus.jaxb.runtime.JaxbContextCustomizer;
import jakarta.inject.Singleton;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import jakarta.xml.bind.Unmarshaller;

@Singleton
public class JaxbCustomModuleCustomizer implements JaxbContextCustomizer {

    // For JAXB context configuration
    @Override
    public void customizeContextProperties(Map<String, Object> properties) {
        // ...
    }

    // For Marshaller configuration
    @Override
    public void customizeMarshaller(Marshaller marshaller) throws PropertyException {
        marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
    }

    // For Unmarshaller configuration
    @Override
    public void customizeUnmarshaller(Unmarshaller unmarshaller) throws PropertyException {
        // ...
    }
}