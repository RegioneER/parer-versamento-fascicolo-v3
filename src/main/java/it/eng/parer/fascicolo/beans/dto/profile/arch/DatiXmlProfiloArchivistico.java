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
package it.eng.parer.fascicolo.beans.dto.profile.arch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;

/**
 *
 * @author fioravanti_f, sinatti_s
 */
public class DatiXmlProfiloArchivistico implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6378542948864642292L;

    private String indiceClassificazione;
    private String descIndiceClassificazione;
    private String infoPianoCoservazione;
    private BigDecimal tempoConservazione = BigDecimal.valueOf(9999); // default (mandatory)
    private CSChiaveFasc chiaveFascicoloDiAppartenenza;
    private List<DXPAFascicoloCollegato> fascCollegati;
    private List<DXPAVoceClassificazione> vociClassificazione;

    public String getIndiceClassificazione() {
	return indiceClassificazione;
    }

    public void setIndiceClassificazione(String indiceClassificazione) {
	this.indiceClassificazione = indiceClassificazione;
    }

    public String getDescIndiceClassificazione() {
	return descIndiceClassificazione;
    }

    public void setDescIndiceClassificazione(String descIndiceClassificazione) {
	this.descIndiceClassificazione = descIndiceClassificazione;
    }

    public CSChiaveFasc getChiaveFascicoloDiAppartenenza() {
	return chiaveFascicoloDiAppartenenza;
    }

    public void setChiaveFascicoloDiAppartenenza(CSChiaveFasc chiaveFascicoloDiAppartenenza) {
	this.chiaveFascicoloDiAppartenenza = chiaveFascicoloDiAppartenenza;
    }

    public BigDecimal getTempoConservazione() {
	return tempoConservazione;
    }

    public void setTempoConservazione(BigDecimal tempoConservazione) {
	Assert.assertNotNull(tempoConservazione);
	this.tempoConservazione = tempoConservazione;
    }

    public String getInfoPianoCoservazione() {
	return infoPianoCoservazione;
    }

    public void setInfoPianoCoservazione(String infoPianoCoservazione) {
	this.infoPianoCoservazione = infoPianoCoservazione;
    }

    public List<DXPAFascicoloCollegato> getFascCollegati() {
	if (fascCollegati == null) {
	    fascCollegati = new ArrayList<>(0);
	}
	// clean from null objects
	fascCollegati.removeIf(Objects::isNull);
	return fascCollegati;
    }

    public void setFascCollegati(List<DXPAFascicoloCollegato> fascCollegati) {
	this.fascCollegati = fascCollegati;
    }

    public DXPAFascicoloCollegato addFascCollegato(DXPAFascicoloCollegato fascCollegato) {
	getFascCollegati().add(fascCollegato);
	return fascCollegato;
    }

    public DXPAFascicoloCollegato removeFascCollegato(DXPAFascicoloCollegato fascCollegato) {
	getFascCollegati().remove(fascCollegato);
	return fascCollegato;
    }

    public List<DXPAVoceClassificazione> getVociClassificazione() {
	if (vociClassificazione == null) {
	    vociClassificazione = new ArrayList<>(0);
	}
	// clean from null objects
	vociClassificazione.removeIf(Objects::isNull);
	return vociClassificazione;
    }

    public void setVociClassificazione(List<DXPAVoceClassificazione> vociClassificazione) {
	this.vociClassificazione = vociClassificazione;
    }

    public DXPAVoceClassificazione addVoceClassificazione(
	    DXPAVoceClassificazione voceClassificazione) {
	getVociClassificazione().add(voceClassificazione);
	return voceClassificazione;
    }

    public DXPAVoceClassificazione removeVoceClassificazione(
	    DXPAVoceClassificazione voceClassificazione) {
	getVociClassificazione().remove(voceClassificazione);
	return voceClassificazione;
    }
}
