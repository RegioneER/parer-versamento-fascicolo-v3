package it.eng.parer.fascicolo.beans.dto.profile.norm;

import java.io.Serializable;
import java.util.Date;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.profile.IEvento;

public class DXPNEvento implements IEvento, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5916948054589854073L;
    String descrizione;
    Date dataInizio;
    Date dataFine;
    Date oraInizio;
    Date oraFine;

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
        Assert.assertNotNull(descrizione);
        this.descrizione = descrizione;
    }

    @Override
    public Date getDataInizio() {
        return dataInizio;
    }

    @Override
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    @Override
    public Date getDataFine() {
        return dataFine;
    }

    @Override
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public Date getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Date oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Date getOraFine() {
        return oraFine;
    }

    public void setOraFine(Date oraFine) {
        this.oraFine = oraFine;
    }

    @Override
    public String toString() {
        return "DXPNEvento [" + (descrizione != null ? "descrizione - " + descrizione + ", " : "")
                + (dataInizio != null ? "dataInizio - " + dataInizio + ", " : "")
                + (dataFine != null ? "dataFine - " + dataFine + ", " : "") + "oraInizio - " + oraInizio
                + ", oraFine - " + oraFine + "]";
    }

}
