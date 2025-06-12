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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.profile.ISoggetto;
import it.eng.parer.fascicolo.beans.utils.Costanti;

public class DXPNSoggetto implements ISoggetto<DXPNIdentSoggetto, DXPNEvento>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1408894230612086351L;

    String nome;
    String cognome;
    String tipoRapporto;
    /*
     * utilizzo di "aggregato" multi string : tecnica adottata per "accumulare" più informazioni
     * qualora un soggetto presenti più tipologie (questo da modello NON dovrebbe mai accadere)
     * Esempio : un soggotto sia PG che PAE
     */
    transient StringJoiner denominazione = new StringJoiner(" - ");
    transient List<String> indirizzoDigitRifs;
    transient List<DXPNIdentSoggetto> identificativi;
    transient List<DXPNEvento> eventi;

    @Override
    public String getNome() {
	return nome;
    }

    @Override
    public void setNome(String nome) {
	this.nome = nome;
    }

    @Override
    public String getCognome() {
	return cognome;
    }

    @Override
    public void setCognome(String cognome) {
	this.cognome = cognome;
    }

    @Override
    public String getDenominazione() {
	return denominazione.toString();
    }

    @Override
    public void setDenominazioneDefault(String denominazione) {
	this.denominazione.setEmptyValue(denominazione);
    }

    @Override
    public void addDenominazione(String denominazione) {
	this.denominazione.add(denominazione);
    }

    @Override
    public String getTipoRapporto() {
	return tipoRapporto;
    }

    @Override
    public void setTipoRapporto(String tipoRapporto) {
	Assert.assertNotNull(tipoRapporto);
	this.tipoRapporto = tipoRapporto;
    }

    @Override
    public List<DXPNEvento> getEventi() {
	if (eventi == null) {
	    eventi = new ArrayList<>(0);
	}
	// clean from null objects
	eventi.removeIf(Objects::isNull);
	return eventi;
    }

    @Override
    public void setEventi(List<DXPNEvento> eventi) {
	this.eventi = eventi;
    }

    @Override
    public DXPNEvento addEvento(DXPNEvento evento) {
	getEventi().add(evento);
	return evento;
    }

    @Override
    public DXPNEvento removeEvento(DXPNEvento evento) {
	getEventi().remove(evento);
	return evento;
    }

    @Override
    public List<String> getIndirizzoDigitRifs() {
	if (indirizzoDigitRifs == null) {
	    indirizzoDigitRifs = new ArrayList<>(0);
	}
	// clean from null objects
	indirizzoDigitRifs.removeIf(Objects::isNull);
	return indirizzoDigitRifs;
    }

    @Override
    public void setIndirizzoDigitRifs(List<String> indirizzoDigitRifs) {
	this.indirizzoDigitRifs = indirizzoDigitRifs;
    }

    @Override
    public String addIndirizzoDigitRif(String indirizzoDigitRif) {
	getIndirizzoDigitRifs().add(indirizzoDigitRif);
	return indirizzoDigitRif;
    }

    @Override
    public String removeIndirizzoDigitRif(String indirizzoDigitRif) {
	getIndirizzoDigitRifs().remove(indirizzoDigitRif);
	return indirizzoDigitRif;
    }

    @Override
    public void setIdentificativi(List<DXPNIdentSoggetto> identificativi) {
	this.identificativi = identificativi;

    }

    @Override
    public List<DXPNIdentSoggetto> getIdentificativi() {
	if (identificativi == null) {
	    identificativi = new ArrayList<>(0);
	}
	// clean from null objects
	identificativi.removeIf(Objects::isNull);
	return identificativi;
    }

    @Override
    public DXPNIdentSoggetto addIdentificativo(DXPNIdentSoggetto identificativo) {
	getIdentificativi().add(identificativo);
	return identificativo;
    }

    @Override
    public DXPNIdentSoggetto removeIdentificativi(DXPNIdentSoggetto identificativo) {
	getIdentificativi().remove(identificativo);
	return identificativo;
    }

    @Override
    public void setCittadinanza(String cittadinanza) {
	throw new UnsupportedOperationException("setCittadinanza");
    }

    @Override
    public String getCittadinanza() {
	return null;
    }

    @Override
    public void setLuogoNascita(String luogoNascita) {
	throw new UnsupportedOperationException("setLuogoNascita");

    }

    @Override
    public String getLuogoNascita() {
	return null;
    }

    @Override
    public String getDataNascita() {
	return null;
    }

    @Override
    public void setDataNascita(String dataNascita) {
	throw new UnsupportedOperationException("setDataNascita");
    }

    @Override
    public String toString() {
	return (nome != null ? "nome: " + nome + Costanti.TO_STRING_FIELD_SEPARATOR : "")
		+ (cognome != null ? "cognome: " + cognome + Costanti.TO_STRING_FIELD_SEPARATOR
			: "")
		+ (tipoRapporto != null
			? "tipo ruolo: " + tipoRapporto + Costanti.TO_STRING_FIELD_SEPARATOR
			: "")
		+ (denominazione.length() != 0 ? "denominazione: " + denominazione : "");
    }

}
