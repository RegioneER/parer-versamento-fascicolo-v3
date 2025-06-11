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

package it.eng.parer.fascicolo.beans.dto.profile.arch;

import java.io.Serializable;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;

public class DXPAFascicoloCollegato implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6310873681354716730L;

    protected String descCollegamento;
    protected CSChiaveFasc csChiaveFasc;

    public String getDescCollegamento() {
	return descCollegamento;
    }

    public void setDescCollegamento(String descCollegamento) {
	this.descCollegamento = descCollegamento;
    }

    public CSChiaveFasc getCsChiaveFasc() {
	return csChiaveFasc;
    }

    public void setCsChiaveFasc(CSChiaveFasc csChiaveFasc) {
	Assert.assertNotNull(csChiaveFasc);
	this.csChiaveFasc = csChiaveFasc;
    }

    @Override
    public String toString() {
	return descCollegamento + " - " + csChiaveFasc;
    }

}
