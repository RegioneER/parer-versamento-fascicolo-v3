/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.utils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;

/**
 *
 * classe che implementa un event handler per l'unmarshaller di JAXB. Questo handler provoca l'interruzione della
 * verifica e dell'unmarshall al primo errore, che viene scritto nella variabile 'messaggio'
 *
 * @author fioravanti_f
 */
public class WsXmlValidationEventHandler implements ValidationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(WsXmlValidationEventHandler.class);

    String messaggio = null;

    public String getMessaggio() {
        return messaggio;
    }

    @Override
    public boolean handleEvent(ValidationEvent event) {

        log.atDebug().log("\nErrore di validazione JAXB");
        log.atDebug().log("SEVERITY:  {}", event.getSeverity());
        log.atDebug().log("MESSAGE:  {}", event.getMessage());
        log.atDebug().log("LINKED EXCEPTION: {0}", event.getLinkedException());
        log.atDebug().log("LOCATOR");
        log.atDebug().log("    LINE NUMBER:  {}", event.getLocator().getLineNumber());
        log.atDebug().log("    COLUMN NUMBER:  {}", event.getLocator().getColumnNumber());
        log.atDebug().log("    OFFSET:  {}", event.getLocator().getOffset());
        log.atDebug().log("    OBJECT:  {}", event.getLocator().getObject());
        log.atDebug().log("    NODE:  {}", event.getLocator().getNode());
        log.atDebug().log("    URL:  {}", event.getLocator().getURL());

        messaggio = event.getMessage();

        return false;
    }

}
