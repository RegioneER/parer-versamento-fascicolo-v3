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

public class XmlSipNotWellFormedException extends Exception implements IXmlSipValidationException {

    /**
     *
     */
    private static final long serialVersionUID = 4057708703115725849L;

    private final String xmlSip;

    public XmlSipNotWellFormedException() {
	super();
	this.xmlSip = StringUtils.EMPTY;
    }

    public XmlSipNotWellFormedException(Throwable cause, String xmlSip) {
	super(cause);
	this.xmlSip = xmlSip;
    }

    public XmlSipNotWellFormedException(String message, Throwable cause, String xmlSip) {
	super(message, cause);
	this.xmlSip = xmlSip;
    }

    public String getXmlSip() {
	return xmlSip;
    }

}
