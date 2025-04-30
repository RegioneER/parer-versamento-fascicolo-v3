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
