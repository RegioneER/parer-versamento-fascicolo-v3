/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.profile.norm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Del profilo normativo utilizzati solo soggetti ed eventi
 */
public class DatiXmlProfiloNormativo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6199328772196765242L;

    private List<DXPNSoggetto> soggetti;
    private String tipoAggreg;

    public List<DXPNSoggetto> getSoggetti() {
        if (soggetti == null) {
            soggetti = new ArrayList<>(0);
        }
        // clean from null objects
        soggetti.removeIf(Objects::isNull);
        return soggetti;
    }

    public void setSoggetti(List<DXPNSoggetto> soggetti) {
        this.soggetti = soggetti;
    }

    public DXPNSoggetto addSoggetto(DXPNSoggetto soggetto) {
        getSoggetti().add(soggetto);
        return soggetto;
    }

    public DXPNSoggetto removeSoggetto(DXPNSoggetto soggetto) {
        getSoggetti().remove(soggetto);
        return soggetto;
    }

    public String getTipoAggreg() {
        return tipoAggreg;
    }

    public void setTipoAggreg(String tipoAggreg) {
        this.tipoAggreg = tipoAggreg;
    }

}
