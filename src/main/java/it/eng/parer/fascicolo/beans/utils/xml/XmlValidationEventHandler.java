package it.eng.parer.fascicolo.beans.utils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;

/**
 *
 *
 * @author Moretti_Lu
 */
public class XmlValidationEventHandler implements ValidationEventHandler {

    private final Logger log = LoggerFactory.getLogger(XmlValidationEventHandler.class);

    /**
     * Holds the first error validation event.
     */
    private ValidationEvent firstErrorValidationEvent;

    /**
     * @return Always returns <code>true</code>, because we want to continue the current unmarshal, validate, or marshal
     *         to log all events.
     */
    @Override
    public boolean handleEvent(ValidationEvent event) {
        if (firstErrorValidationEvent == null && event.getSeverity() > ValidationEvent.WARNING) {
            firstErrorValidationEvent = event;
        }

        switch (event.getSeverity()) {
        case ValidationEvent.WARNING:
            log.atWarn().log("Validation warning: {}", event);
            break;
        case ValidationEvent.ERROR, ValidationEvent.FATAL_ERROR:
            log.atError().log("Validation error: {}", event);
            break;
        default:
            log.atInfo().log("Validation event: {}", event);
        }

        return false;
    }

    public ValidationEvent getFirstErrorValidationEvent() {
        return firstErrorValidationEvent;
    }
}
