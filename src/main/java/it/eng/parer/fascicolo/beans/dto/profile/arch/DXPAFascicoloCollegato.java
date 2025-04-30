package it.eng.parer.fascicolo.beans.dto.profile.arch;

import java.io.Serializable;

import io.smallrye.common.constraint.Assert;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;

public class DXPAFascicoloCollegato implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6310873681354716730L;

    protected String descCollegamento;
    protected CSChiaveFasc csChiaveFasc;

    public String getDescCollegamento() {
        return descCollegamento;
    }

    public void setDescCollegamento(String descCollegamento) {
        this.descCollegamento = descCollegamento;
    }

    public CSChiaveFasc getCsChiaveFasc() {
        return csChiaveFasc;
    }

    public void setCsChiaveFasc(CSChiaveFasc csChiaveFasc) {
        Assert.assertNotNull(csChiaveFasc);
        this.csChiaveFasc = csChiaveFasc;
    }

    @Override
    public String toString() {
        return descCollegamento + " - " + csChiaveFasc;
    }

}
