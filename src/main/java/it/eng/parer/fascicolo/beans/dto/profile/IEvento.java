package it.eng.parer.fascicolo.beans.dto.profile;

import java.util.Date;

public interface IEvento {

    public String getDescrizione();

    public void setDescrizione(String descrizione);

    public Date getDataInizio();

    public void setDataInizio(Date dataInizio);

    public Date getDataFine();

    public void setDataFine(Date dataFine);

}
