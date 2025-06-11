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
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.profile.norm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Del profilo normativo utilizzati solo soggetti ed eventi
 */
public class DatiXmlProfiloNormativo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6199328772196765242L;

    private List<DXPNSoggetto> soggetti;
    private String tipoAggreg;

    public List<DXPNSoggetto> getSoggetti() {
	if (soggetti == null) {
	    soggetti = new ArrayList<>(0);
	}
	// clean from null objects
	soggetti.removeIf(Objects::isNull);
	return soggetti;
    }

    public void setSoggetti(List<DXPNSoggetto> soggetti) {
	this.soggetti = soggetti;
    }

    public DXPNSoggetto addSoggetto(DXPNSoggetto soggetto) {
	getSoggetti().add(soggetto);
	return soggetto;
    }

    public DXPNSoggetto removeSoggetto(DXPNSoggetto soggetto) {
	getSoggetti().remove(soggetto);
	return soggetto;
    }

    public String getTipoAggreg() {
	return tipoAggreg;
    }

    public void setTipoAggreg(String tipoAggreg) {
	this.tipoAggreg = tipoAggreg;
    }

}
