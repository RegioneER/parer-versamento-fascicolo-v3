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
package it.eng.parer.fascicolo.beans.dto.profile.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author fioravanti_f
 */
public class DatiXmlProfiloGenerale implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1396491059853032245L;

    private String oggettoFascicolo;
    private Date dataApertura;
    private Date dataChiusura;
    private String noteFascicolo;
    private String lvlRiservatezza;
    private List<DXPGSoggetto> soggetti;
    private DXPGProcAmmininistrativo procAmm;
    private List<DXPGEvento> eventi;

    public String getOggettoFascicolo() {
	return oggettoFascicolo;
    }

    public void setOggettoFascicolo(String oggettoFascicolo) {
	this.oggettoFascicolo = oggettoFascicolo;
    }

    public Date getDataApertura() {
	return dataApertura;
    }

    public void setDataApertura(Date dataApertura) {
	this.dataApertura = dataApertura;
    }

    public Date getDataChiusura() {
	return dataChiusura;
    }

    public void setDataChiusura(Date dataChiusura) {
	this.dataChiusura = dataChiusura;
    }

    public String getNoteFascicolo() {
	return noteFascicolo;
    }

    public void setNoteFascicolo(String noteFascicolo) {
	this.noteFascicolo = noteFascicolo;
    }

    public List<DXPGSoggetto> getSoggetti() {
	if (soggetti == null) {
	    soggetti = new ArrayList<>(0);
	}
	// clean from null objects
	soggetti.removeIf(Objects::isNull);
	return soggetti;
    }

    public void setSoggetti(List<DXPGSoggetto> soggetti) {
	this.soggetti = soggetti;
    }

    public DXPGSoggetto addSoggetto(DXPGSoggetto soggetto) {
	getSoggetti().add(soggetto);
	return soggetto;
    }

    public DXPGSoggetto removeSoggetto(DXPGSoggetto soggetto) {
	getSoggetti().remove(soggetto);
	return soggetto;
    }

    public String getLvlRiservatezza() {
	return lvlRiservatezza;
    }

    public void setLvlRiservatezza(String lvlRiservatezza) {
	this.lvlRiservatezza = lvlRiservatezza;
    }

    public DXPGProcAmmininistrativo getProcAmm() {
	return procAmm;
    }

    public void setProcAmm(DXPGProcAmmininistrativo procAmm) {
	this.procAmm = procAmm;
    }

    public List<DXPGEvento> getEventi() {
	if (eventi == null) {
	    eventi = new ArrayList<>(0);
	}
	// clean from null objects
	eventi.removeIf(Objects::isNull);
	return eventi;
    }

    public void setEventi(List<DXPGEvento> eventi) {
	this.eventi = eventi;
    }

    public DXPGEvento addEvento(DXPGEvento evento) {
	getEventi().add(evento);
	return evento;
    }

    public DXPGEvento removeEvento(DXPGEvento evento) {
	getEventi().remove(evento);
	return evento;
    }

}
