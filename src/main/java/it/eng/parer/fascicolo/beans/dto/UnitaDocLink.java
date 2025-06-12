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

package it.eng.parer.fascicolo.beans.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import it.eng.parer.fascicolo.beans.dto.base.CSChiave;

public class UnitaDocLink implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3153568788306139252L;

    //
    Long idLinkUnitaDoc;
    CSChiave csChiave;
    Date dataInserimentoFas;
    BigInteger posizione;

    public Long getIdLinkUnitaDoc() {
	return idLinkUnitaDoc;
    }

    public void setIdLinkUnitaDoc(Long idLinkUnitaDoc) {
	this.idLinkUnitaDoc = idLinkUnitaDoc;
    }

    public CSChiave getCsChiave() {
	return csChiave;
    }

    public void setCsChiave(CSChiave csChiave) {
	this.csChiave = csChiave;
    }

    public Date getDataInserimentoFas() {
	return dataInserimentoFas;
    }

    public void setDataInserimentoFas(Date dataInserimentoFas) {
	this.dataInserimentoFas = dataInserimentoFas;
    }

    public BigInteger getPosizione() {
	return posizione;
    }

    public void setPosizione(BigInteger posizione) {
	this.posizione = posizione;
    }

    @Override
    public String toString() {
	return "FasUnitaDocColl [idLinkUnitaDoc - " + idLinkUnitaDoc + " , csChiave - " + csChiave
		+ " , dataInserimentoFas - " + dataInserimentoFas + " , posizione - " + posizione
		+ "]";
    }

}
