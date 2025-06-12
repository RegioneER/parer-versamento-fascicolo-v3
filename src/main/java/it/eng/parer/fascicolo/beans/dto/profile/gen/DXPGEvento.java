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
import java.util.Date;

import org.wildfly.common.Assert;

import it.eng.parer.fascicolo.beans.dto.profile.IEvento;

public class DXPGEvento implements IEvento, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2599984362306903110L;

    String descrizione;
    Date dataInizio;
    Date dataFine;

    public String getDescrizione() {
	return descrizione;
    }

    public void setDescrizione(String descrizione) {
	Assert.assertNotNull(descrizione);
	this.descrizione = descrizione;
    }

    public Date getDataInizio() {
	return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
	this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
	return dataFine;
    }

    public void setDataFine(Date dataFine) {
	this.dataFine = dataFine;
    }

    @Override
    public String toString() {
	return "DXPGEvento [" + (descrizione != null ? "descrizione - " + descrizione + ", " : "")
		+ (dataInizio != null ? "dataInizio - " + dataInizio + ", " : "")
		+ (dataFine != null ? "dataFine - " + dataFine : "") + "]";
    }

}
