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

package it.eng.parer.fascicolo.beans.dto.profile.norm;

import java.io.Serializable;
import java.util.Date;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.profile.IEvento;

public class DXPNEvento implements IEvento, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5916948054589854073L;
    String descrizione;
    Date dataInizio;
    Date dataFine;
    Date oraInizio;
    Date oraFine;

    @Override
    public String getDescrizione() {
	return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
	Assert.assertNotNull(descrizione);
	this.descrizione = descrizione;
    }

    @Override
    public Date getDataInizio() {
	return dataInizio;
    }

    @Override
    public void setDataInizio(Date dataInizio) {
	this.dataInizio = dataInizio;
    }

    @Override
    public Date getDataFine() {
	return dataFine;
    }

    @Override
    public void setDataFine(Date dataFine) {
	this.dataFine = dataFine;
    }

    public Date getOraInizio() {
	return oraInizio;
    }

    public void setOraInizio(Date oraInizio) {
	this.oraInizio = oraInizio;
    }

    public Date getOraFine() {
	return oraFine;
    }

    public void setOraFine(Date oraFine) {
	this.oraFine = oraFine;
    }

    @Override
    public String toString() {
	return "DXPNEvento [" + (descrizione != null ? "descrizione - " + descrizione + ", " : "")
		+ (dataInizio != null ? "dataInizio - " + dataInizio + ", " : "")
		+ (dataFine != null ? "dataFine - " + dataFine + ", " : "") + "oraInizio - "
		+ oraInizio + ", oraFine - " + oraFine + "]";
    }

}
