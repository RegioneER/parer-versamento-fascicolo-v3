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

public class DXPGProcAmmininistrativo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7770603240791983206L;

    String codice;
    String denominazione;
    String materiaArgStrut;

    public String getCodice() {
	return codice;
    }

    public void setCodice(String codice) {
	this.codice = codice;
    }

    public String getDenominazione() {
	return denominazione;
    }

    public void setDenominazione(String denominazione) {
	this.denominazione = denominazione;
    }

    public String getMateriaArgStrut() {
	return materiaArgStrut;
    }

    public void setMateriaArgStrut(String materiaArgStrut) {
	this.materiaArgStrut = materiaArgStrut;
    }

    @Override
    public String toString() {
	return codice + " - " + denominazione + " - " + materiaArgStrut;
    }

}
