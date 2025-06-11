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
