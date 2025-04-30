package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * ElvStatoElencoVersFasc constraint
 *
 * @author DiLorenzo_F
 */
public final class ElvStatoElencoVersFasc {

    private ElvStatoElencoVersFasc() {
    }

    /**
     * Tipo stato da elaborare ti_stato_elenco_fasc IN ('AIP_CREATI', 'APERTO', 'CHIUSO', 'COMPLETATO', 'DA_CHIUDERE',
     * 'ELENCO_INDICI_AIP_CREATO', 'ELENCO_INDICI_AIP_FIRMA_IN_CORSO', 'FIRMATO', 'FIRMA_IN_CORSO',
     * 'IN_CODA_CREAZIONE_AIP')
     */
    public enum TiStatoElencoFasc {
        AIP_CREATI, APERTO, CHIUSO, COMPLETATO, DA_CHIUDERE, ELENCO_INDICI_AIP_CREATO, ELENCO_INDICI_AIP_FIRMA_IN_CORSO,
        FIRMATO, FIRMA_IN_CORSO, IN_CODA_CREAZIONE_AIP
    }
}
