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

package it.eng.parer.fascicolo.beans.dto.profile.gen;

import java.io.Serializable;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.profile.IIdentSoggetto;

public class DXPGIdentSoggetto implements IIdentSoggetto, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7974248667981738442L;

    String codice;
    String tipo;
    boolean isPredefined;

    public DXPGIdentSoggetto() {
	super();
	this.isPredefined = false;
    }

    public DXPGIdentSoggetto(boolean isPredefined) {
	super();
	this.isPredefined = isPredefined;
    }

    @Override
    public String getCodice() {
	return codice;
    }

    @Override
    public void setCodice(String codice) {
	Assert.assertNotNull(codice);
	this.codice = codice;
    }

    @Override
    public String getTipo() {
	return tipo;
    }

    @Override
    public void setTipo(String tipo) {
	Assert.assertNotNull(tipo);
	this.tipo = tipo;
    }

    @Override
    public boolean isPredefined() {
	return isPredefined;
    }

    public void setPredefined(boolean isPredefined) {
	this.isPredefined = isPredefined;
    }

    @Override
    public String toString() {
	return "DXPGIdentSoggetto [" + (codice != null ? "codice - " + codice + ", " : "")
		+ (tipo != null ? "tipo - " + tipo : "") + "]";
    }

}
