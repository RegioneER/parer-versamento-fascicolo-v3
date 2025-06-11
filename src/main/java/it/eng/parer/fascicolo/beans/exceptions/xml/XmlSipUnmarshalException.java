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
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.exceptions.xml;

import org.apache.commons.lang3.StringUtils;

import jakarta.xml.bind.JAXBException;

public class XmlSipUnmarshalException extends JAXBException implements IXmlSipValidationException {

    private static final long serialVersionUID = 4261618786078845479L;
    private final String xmlSip;

    public XmlSipUnmarshalException(String message) {
	super(message);
	this.xmlSip = StringUtils.EMPTY;
    }

    public XmlSipUnmarshalException(String message, String errorCode, Throwable exception,
	    String xmlSip) {
	super(message, errorCode, exception);
	this.xmlSip = xmlSip;
    }

    public XmlSipUnmarshalException(String message, String errorCode, String xmlSip) {
	super(message, errorCode);
	this.xmlSip = xmlSip;
    }

    public XmlSipUnmarshalException(String message, Throwable exception, String xmlSip) {
	super(message, exception);
	this.xmlSip = xmlSip;
    }

    public XmlSipUnmarshalException(Throwable exception, String xmlSip) {
	super(exception);
	this.xmlSip = xmlSip;
    }

    public String getXmlSip() {
	return xmlSip;
    }

}
