package it.eng.parer.fascicolo.beans.dto;

import it.eng.parer.fascicolo.beans.dto.profile.arch.DXPAFascicoloCollegato;

public class FascicoloLink extends DXPAFascicoloCollegato {

    /**
     *
     */
    private static final long serialVersionUID = -1875679385549579566L;
    //
    Long idLinkFasc;

    public Long getIdLinkFasc() {
        return idLinkFasc;
    }

    public void setIdLinkFasc(Long idLinkFasc) {
        this.idLinkFasc = idLinkFasc;
    }

    @Override
    public String toString() {
        return idLinkFasc + " - " + descCollegamento + " - " + csChiaveFasc;
    }

}
