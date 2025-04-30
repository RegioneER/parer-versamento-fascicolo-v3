/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    public XmlSipUnmarshalException(String message, String errorCode, Throwable exception, String xmlSip) {
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
