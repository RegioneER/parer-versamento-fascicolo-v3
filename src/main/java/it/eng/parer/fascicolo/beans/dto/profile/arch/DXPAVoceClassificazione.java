package it.eng.parer.fascicolo.beans.dto.profile.arch;

import java.io.Serializable;

import io.smallrye.common.constraint.Assert;

public class DXPAVoceClassificazione implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1283152099737149057L;

    String codiceVoce;
    String descrizioneVoce;

    public String getCodiceVoce() {
        return codiceVoce;
    }

    public void setCodiceVoce(String codiceVoce) {
        Assert.assertNotNull(codiceVoce);
        this.codiceVoce = codiceVoce;
    }

    public String getDescrizioneVoce() {
        return descrizioneVoce;
    }

    public void setDescrizioneVoce(String descrizioneVoce) {
        Assert.assertNotNull(descrizioneVoce);
        this.descrizioneVoce = descrizioneVoce;
    }

    @Override
    public String toString() {
        return codiceVoce + " - " + descrizioneVoce;
    }

}
