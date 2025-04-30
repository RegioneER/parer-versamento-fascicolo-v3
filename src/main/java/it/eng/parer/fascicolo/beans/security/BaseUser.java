package it.eng.parer.fascicolo.beans.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.eng.parer.fascicolo.beans.security.profile.Profile;

public class BaseUser<T extends Profile> implements IUser<T> {
    private static final long serialVersionUID = 1L;

    private long idUtente = 0;
    private String username = null;
    private String cognome = "";
    private String nome = "";
    private transient T profile;
    private List<String> applicazioni = null;
    private boolean attivo;
    private Date scadenzaPwd;

    // ***** Aggiunti per la gestione dei diversi tipi di IDP
    private UserType userType = null;
    // ID di un eventiale IDP esterno (Es.: SPID (SpidID)
    private String externalId = null;

    public BaseUser() {
        this.profile = null;
        this.applicazioni = new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public T getProfile() {
        return profile;
    }

    @Override
    public void setProfile(T profile) {
        this.profile = profile;
    }

    @Override
    public long getIdUtente() {
        return idUtente;
    }

    @Override
    public void setIdUtente(long idUtente) {
        this.idUtente = idUtente;
    }

    @Override
    public boolean isAttivo() {

        return attivo;
    }

    @Override
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    @Override
    public Date getScadenzaPwd() {
        return scadenzaPwd;
    }

    @Override
    public void setScadenzaPwd(Date scadenzaPwd) {
        this.scadenzaPwd = scadenzaPwd;
    }

    @Override
    public List<String> getApplicazioni() {
        return applicazioni;
    }

    @Override
    public void setApplicazioni(List<String> applicazioni) {
        this.applicazioni = applicazioni;
    }

    // ***** Aggiunti per la gestione dei diversi tipi di IDP
    @Override
    public UserType getUserType() {
        return userType;
    }

    @Override
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
