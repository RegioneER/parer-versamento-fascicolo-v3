/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.exceptions;

import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import jakarta.ws.rs.WebApplicationException;

public class AppGenericRuntimeException extends WebApplicationException {

    /**
     *
     */
    private static final long serialVersionUID = 5015771412184277789L;

    private final ErrorCategory category;

    public AppGenericRuntimeException() {
        super();
        this.category = ErrorCategory.INTERNAL_ERROR; // default
    }

    public AppGenericRuntimeException(ErrorCategory category) {
        super();
        this.category = category;
    }

    public AppGenericRuntimeException(String message, Throwable throwable, ErrorCategory category) {
        super(message, throwable);
        this.category = category;
    }

    public AppGenericRuntimeException(Throwable throwable, ErrorCategory category) {
        super(throwable);
        this.category = category;
    }

    public AppGenericRuntimeException(String message, ErrorCategory category) {
        super(message);
        this.category = category;
    }

    public ErrorCategory getCategory() {
        return category;
    }

    @Override
    public String getMessage() {
        return "[" + getCategory().toString() + "]" + "  " + super.getMessage();
    }

}
