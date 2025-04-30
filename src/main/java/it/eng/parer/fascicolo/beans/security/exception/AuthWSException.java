package it.eng.parer.fascicolo.beans.security.exception;

public class AuthWSException extends Exception {

    public enum CodiceErrore {
        LOGIN_FALLITO, UTENTE_NON_ATTIVO, UTENTE_SCADUTO, UTENTE_NON_AUTORIZZATO, PROBLEMA_ESTRAZIONE_APPLICAZIONE
    }

    private static final long serialVersionUID = 1L;

    private final CodiceErrore codiceErrore;
    private final String descrizioneErrore;

    public AuthWSException(CodiceErrore code, String msg) {
        super();
        this.codiceErrore = code;
        this.descrizioneErrore = msg;
    }

    public CodiceErrore getCodiceErrore() {
        return codiceErrore;
    }

    public String getDescrizioneErrore() {
        return descrizioneErrore;
    }

}
