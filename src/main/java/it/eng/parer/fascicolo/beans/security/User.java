package it.eng.parer.fascicolo.beans.security;

import java.math.BigDecimal;

import it.eng.parer.fascicolo.beans.security.profile.Profile;

public class User extends BaseUser<Profile> {

    private static final long serialVersionUID = 1L;
    private BigDecimal idOrganizzazioneFoglia;
    private String nomeStruttura;
    private String sistemaVersante;
    private Long idApplicazione;
    private boolean utenteDaAssociare;
    private String codiceFiscale;

    public User() {
        super();
    }

    public BigDecimal getIdOrganizzazioneFoglia() {
        return idOrganizzazioneFoglia;
    }

    public void setIdOrganizzazioneFoglia(BigDecimal idOrganizzazioneFoglia) {
        this.idOrganizzazioneFoglia = idOrganizzazioneFoglia;
    }

    public String getNomeStruttura() {
        return nomeStruttura;
    }

    public void setNomeStruttura(String nomeStruttura) {
        this.nomeStruttura = nomeStruttura;
    }

    public Long getIdApplicazione() {
        return idApplicazione;
    }

    public void setIdApplicazione(Long idApplicazione) {
        this.idApplicazione = idApplicazione;
    }

    public boolean isUtenteDaAssociare() {
        return utenteDaAssociare;
    }

    public void setUtenteDaAssociare(boolean utenteDaAssociare) {
        this.utenteDaAssociare = utenteDaAssociare;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getSistemaVersante() {
        return sistemaVersante;
    }

    public void setSistemaVersante(String sistemaVersante) {
        this.sistemaVersante = sistemaVersante;
    }

}
