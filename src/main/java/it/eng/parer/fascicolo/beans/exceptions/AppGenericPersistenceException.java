/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.exceptions;

import org.apache.commons.lang3.StringUtils;

import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;

public class AppGenericPersistenceException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -7917206270508553978L;

    private static final ErrorCategory category = ErrorCategory.PERSISTENCE;
    private final String codErr;
    private final String dsErr;

    public AppGenericPersistenceException() {
        super();
        this.codErr = MessaggiWSBundle.ERR_666P;
        this.dsErr = StringUtils.EMPTY;
    }

    public AppGenericPersistenceException(String message, Throwable throwable, String codErr, String dsErr) {
        super(message, throwable);
        this.codErr = codErr;
        this.dsErr = dsErr;
    }

    public AppGenericPersistenceException(String message, Throwable throwable, String dsErr) {
        this(message, throwable, MessaggiWSBundle.ERR_666P, dsErr);
    }

    public AppGenericPersistenceException(Throwable throwable, String codErr, String dsErr) {
        super(throwable);
        this.codErr = codErr;
        this.dsErr = dsErr;
    }

    public AppGenericPersistenceException(Throwable throwable, String dsErr) {
        this(throwable, MessaggiWSBundle.ERR_666P, dsErr);
    }

    public AppGenericPersistenceException(String message, String codErr, String dsErr) {
        super(message);
        this.codErr = codErr;
        this.dsErr = dsErr;
    }

    public AppGenericPersistenceException(String message, String dsErr) {
        this(message, MessaggiWSBundle.ERR_666P, dsErr);

    }

    public ErrorCategory getCategory() {
        return category;
    }

    public String getCodErr() {
        return codErr;
    }

    public String getDsErr() {
        return dsErr;
    }

    @Override
    public String getMessage() {
        return "[" + getCategory().toString() + "]" + "[" + getCodErr() + "]" + "[" + getDsErr() + "]" + "  "
                + super.getMessage();
    }

}
