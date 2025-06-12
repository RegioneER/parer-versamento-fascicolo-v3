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
package it.eng.parer.fascicolo.beans.dto;

import java.util.EnumSet;
import java.util.Set;

import it.eng.parer.fascicolo.beans.dto.base.IWSDesc;
import it.eng.parer.fascicolo.beans.utils.Costanti;

/**
 *
 * @author fioravanti_f
 */
public class VersFascicoloExt extends AbsVersFascicoloExt {

    private static final long serialVersionUID = 5261426459498072293L;
    private String datiXml;
    private boolean simulaScrittura;

    private transient IWSDesc descrizione;
    private Set<Costanti.ModificatoriWS> modificatoriWS = EnumSet
	    .noneOf(Costanti.ModificatoriWS.class);

    @Override
    public void setDatiXml(String datiXml) {
	this.datiXml = datiXml;
    }

    @Override
    public String getDatiXml() {
	return datiXml;
    }

    @Override
    public boolean isSimulaScrittura() {
	return simulaScrittura;
    }

    @Override
    public void setSimulaScrittura(boolean simulaScrittura) {
	this.simulaScrittura = simulaScrittura;
    }

    @Override
    public IWSDesc getDescrizione() {
	return descrizione;
    }

    @Override
    public void setDescrizione(IWSDesc descrizione) {
	this.descrizione = descrizione;
    }

    @Override
    public Set<Costanti.ModificatoriWS> getModificatoriWSCalc() {
	return this.modificatoriWS;
    }

    public Set<Costanti.ModificatoriWS> getModificatoriWS() {
	return modificatoriWS;
    }

    public void setModificatoriWS(Set<Costanti.ModificatoriWS> modificatoriWS) {
	this.modificatoriWS = modificatoriWS;
    }
}
