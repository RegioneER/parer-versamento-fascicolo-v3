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
