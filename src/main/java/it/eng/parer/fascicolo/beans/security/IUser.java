package it.eng.parer.fascicolo.beans.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import it.eng.parer.fascicolo.beans.security.profile.Profile;

public interface IUser<T extends Profile> extends Serializable {

    public String getNome();

    public void setNome(String nome);

    public String getCognome();

    public void setCognome(String cognome);

    public long getIdUtente();

    public void setIdUtente(long idUtente);

    public String getUsername();

    public void setUsername(String username);

    public boolean isAttivo();

    public void setAttivo(boolean attivo);

    public Date getScadenzaPwd();

    public void setScadenzaPwd(Date scadenzaPwd);

    public T getProfile();

    public void setProfile(T profile);

    public List<String> getApplicazioni();

    public void setApplicazioni(List<String> applicazioni);

    // ***** Aggiunti per la gestione dei diversi tipi di IDP
    public enum UserType {
        SPID_FEDERA
    }

    public UserType getUserType();

    public void setUserType(UserType userType);

    public String getExternalId();

    public void setExternalId(String externalId);
    // *****
}
