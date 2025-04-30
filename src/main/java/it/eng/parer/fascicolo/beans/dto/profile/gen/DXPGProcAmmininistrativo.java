package it.eng.parer.fascicolo.beans.dto.profile.gen;

import java.io.Serializable;

public class DXPGProcAmmininistrativo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7770603240791983206L;

    String codice;
    String denominazione;
    String materiaArgStrut;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getMateriaArgStrut() {
        return materiaArgStrut;
    }

    public void setMateriaArgStrut(String materiaArgStrut) {
        this.materiaArgStrut = materiaArgStrut;
    }

    @Override
    public String toString() {
        return codice + " - " + denominazione + " - " + materiaArgStrut;
    }

}
