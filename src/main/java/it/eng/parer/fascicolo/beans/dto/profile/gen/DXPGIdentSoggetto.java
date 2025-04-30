package it.eng.parer.fascicolo.beans.dto.profile.gen;

import java.io.Serializable;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.profile.IIdentSoggetto;

public class DXPGIdentSoggetto implements IIdentSoggetto, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7974248667981738442L;

    String codice;
    String tipo;
    boolean isPredefined;

    public DXPGIdentSoggetto() {
        super();
        this.isPredefined = false;
    }

    public DXPGIdentSoggetto(boolean isPredefined) {
        super();
        this.isPredefined = isPredefined;
    }

    @Override
    public String getCodice() {
        return codice;
    }

    @Override
    public void setCodice(String codice) {
        Assert.assertNotNull(codice);
        this.codice = codice;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        Assert.assertNotNull(tipo);
        this.tipo = tipo;
    }

    @Override
    public boolean isPredefined() {
        return isPredefined;
    }

    public void setPredefined(boolean isPredefined) {
        this.isPredefined = isPredefined;
    }

    @Override
    public String toString() {
        return "DXPGIdentSoggetto [" + (codice != null ? "codice - " + codice + ", " : "")
                + (tipo != null ? "tipo - " + tipo : "") + "]";
    }

}
