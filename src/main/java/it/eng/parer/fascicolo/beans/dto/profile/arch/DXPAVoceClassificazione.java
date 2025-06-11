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

public class DXPAVoceClassificazione implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1283152099737149057L;

    String codiceVoce;
    String descrizioneVoce;

    public String getCodiceVoce() {
	return codiceVoce;
    }

    public void setCodiceVoce(String codiceVoce) {
	Assert.assertNotNull(codiceVoce);
	this.codiceVoce = codiceVoce;
    }

    public String getDescrizioneVoce() {
	return descrizioneVoce;
    }

    public void setDescrizioneVoce(String descrizioneVoce) {
	Assert.assertNotNull(descrizioneVoce);
	this.descrizioneVoce = descrizioneVoce;
    }

    @Override
    public String toString() {
	return codiceVoce + " - " + descrizioneVoce;
    }

}
