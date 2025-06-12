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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.profile.ISoggetto;
import it.eng.parer.fascicolo.beans.utils.Costanti;

/*
 * DTO che raccoglie le informazioni del soggetto su profilo generale. Gli attributi del soggetto
 * sono definiti secondo il modello xsd del profilo. Alcune informazioni come nome, cognome,
 * tipoRapporto, etc. non sono in nessun modo ripetibili, lo stesso vale in generale per tutti gli
 * attributi (non liste) per alcuni si è deciso di utilizzare delle tecniche di "aggregazione" del
 * dato (vedi denominazione) seppur non dovrebbe mai capitare che un soggetto ha molteplici
 * aggregazioni ma per evitare una logica troppo "blindata" in fase di "estrazione" delle
 * informazioni dall'xml, questo dovrebbe permettere di creare sempre e comunque un soggetto che
 * "colleziona" tutte le informazioni al suo interno.
 */
public class DXPGSoggetto implements ISoggetto<DXPGIdentSoggetto, DXPGEvento>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8724820225857602930L;

    // non ripetibile
    String nome;
    String cognome;
    /*
     * utilizzo di "aggregato" multi string : tecnica adottata per "accumulare" più informazioni
     * qualora un soggetto presenti più tipologie (questo da modello NON dovrebbe mai accadere)
     * Esempio : un soggotto sia PG che PAE
     */
    String tipoRapporto;
    //
    String sesso;
    String dataNascita;
    String luogoNascita;
    String cittadinanza;
    //
    transient StringJoiner denominazione = new StringJoiner(" - ");
    transient List<String> indirizzoDigitRifs;
    transient List<DXPGIdentSoggetto> identificativi;
    transient List<DXPGEvento> eventi;

    @Override
    public String getDataNascita() {
	return dataNascita;
    }

    @Override
    public void setDataNascita(String dataNascita) {
	this.dataNascita = dataNascita;
    }

    @Override
    public String getLuogoNascita() {
	return luogoNascita;
    }

    @Override
    public void setLuogoNascita(String luogoNascita) {
	this.luogoNascita = luogoNascita;
    }

    @Override
    public String getCittadinanza() {
	return cittadinanza;
    }

    @Override
    public void setCittadinanza(String cittadinanza) {
	this.cittadinanza = cittadinanza;
    }

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

    public String getSesso() {
	return sesso;
    }

    public void setSesso(String sesso) {
	this.sesso = sesso;
    }

    @Override
    public List<DXPGEvento> getEventi() {
	if (eventi == null) {
	    eventi = new ArrayList<>(0);
	}
	// clean from null objects
	eventi.removeIf(Objects::isNull);
	return eventi;
    }

    @Override
    public void setEventi(List<DXPGEvento> eventi) {
	this.eventi = eventi;
    }

    @Override
    public DXPGEvento addEvento(DXPGEvento evento) {
	getEventi().add(evento);
	return evento;
    }

    @Override
    public DXPGEvento removeEvento(DXPGEvento evento) {
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
    public void setIdentificativi(List<DXPGIdentSoggetto> identificativi) {
	this.identificativi = identificativi;

    }

    @Override
    public List<DXPGIdentSoggetto> getIdentificativi() {
	if (identificativi == null) {
	    identificativi = new ArrayList<>(0);
	}
	// clean from null objects
	identificativi.removeIf(Objects::isNull);
	return identificativi;
    }

    @Override
    public DXPGIdentSoggetto addIdentificativo(DXPGIdentSoggetto identificativo) {
	getIdentificativi().add(identificativo);
	return identificativo;
    }

    @Override
    public DXPGIdentSoggetto removeIdentificativi(DXPGIdentSoggetto identificativo) {
	getIdentificativi().remove(identificativo);
	return identificativo;
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
