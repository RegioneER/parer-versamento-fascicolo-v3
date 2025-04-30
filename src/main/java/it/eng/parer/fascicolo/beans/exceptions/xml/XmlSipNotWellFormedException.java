/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
