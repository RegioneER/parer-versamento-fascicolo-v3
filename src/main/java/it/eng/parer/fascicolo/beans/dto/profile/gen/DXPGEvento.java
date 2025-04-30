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
