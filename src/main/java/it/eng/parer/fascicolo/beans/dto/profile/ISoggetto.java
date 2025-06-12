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

package it.eng.parer.fascicolo.beans.dto.profile;

import java.util.List;

public interface ISoggetto<I extends IIdentSoggetto, E extends IEvento> {

    public String getNome();

    public void setNome(String nome);

    public String getCognome();

    public void setCognome(String cognome);

    public String getDenominazione();

    void setDenominazioneDefault(String denominazione);

    public void addDenominazione(String denominazione);

    public String getTipoRapporto();

    public void setTipoRapporto(String tipoRapporto);

    public List<E> getEventi();

    public void setEventi(List<E> eventi);

    public E addEvento(E evento);

    public E removeEvento(E evento);

    public List<String> getIndirizzoDigitRifs();

    public void setIndirizzoDigitRifs(List<String> indirizzoDigitRifs);

    public String addIndirizzoDigitRif(String indirizzoDigitRif);

    public String removeIndirizzoDigitRif(String indirizzoDigitRif);

    public List<I> getIdentificativi();

    public void setIdentificativi(List<I> identificativi);

    public I addIdentificativo(I identificativo);

    public I removeIdentificativi(I identificativo);

    public void setCittadinanza(String cittadinanza);

    public String getCittadinanza();

    public void setLuogoNascita(String luogoNascita);

    public String getLuogoNascita();

    public String getDataNascita();

    public void setDataNascita(String dataNascita);

}
