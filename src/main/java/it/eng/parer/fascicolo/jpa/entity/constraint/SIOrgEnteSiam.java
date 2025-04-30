package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 *
 * @author Gilioli_P
 */
public class SIOrgEnteSiam {

    private SIOrgEnteSiam() {
    }

    public static String[] tiCdEnteConvenz = { "Altro", "IPA Ente/AOO", "IPA Ente", "ISTAT" };

    /**
     * Tipo ente siam ti_ente_siam IN ('CONVENZIONATO', 'NON_CONVENZIONATO')
     */
    public enum TiEnteSiam {
        CONVENZIONATO, NON_CONVENZIONATO
    }

    /**
     * Tipo ente convenzionato ti_ente_convenz IN ('AMMINISTRATORE', 'CONSERVATORE', 'GESTORE', 'PRODUTTORE')
     */
    public enum TiEnteConvenz {
        AMMINISTRATORE, CONSERVATORE, GESTORE, PRODUTTORE
    }

    /**
     * Tipo ente non convenzionato ti_ente_non_convenz IN ('FORNITORE_ESTERNO', 'ORGANO_VIGILANZA', 'VERSATORE_ESTERNO')
     */
    public enum TiEnteNonConvenz {
        FORNITORE_ESTERNO, ORGANO_VIGILANZA, VERSATORE_ESTERNO
    }
}
