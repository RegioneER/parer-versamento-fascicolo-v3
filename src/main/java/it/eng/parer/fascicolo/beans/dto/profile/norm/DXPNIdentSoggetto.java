package it.eng.parer.fascicolo.beans.dto.profile.norm;

import java.io.Serializable;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.profile.IIdentSoggetto;

public class DXPNIdentSoggetto implements IIdentSoggetto, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1124864128722561737L;
    String codice;
    String tipo;

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
        return true;
    }

    @Override
    public String toString() {
        return "DXPNIdentSoggetto [" + (codice != null ? "codice - " + codice + ", " : "")
                + (tipo != null ? "tipo - " + tipo : "") + "]";
    }

}
