package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * ElvElencoVersFascDaElab constraint
 *
 * @author DiLorenzo_F
 */
public final class ElvElencoVersFascDaElab {

    private ElvElencoVersFascDaElab() {
    }

    /**
     * Tipo stato da elaborare ti_stato_elenco_fasc_daelab IN ('AIP_CREATI', 'APERTO', 'CHIUSO', 'DA_CHIUDERE',
     * 'ELENCO_INDICI_AIP_CREATO', 'ELENCO_INDICI_AIP_FIRMA_IN_CORSO', 'FIRMATO', 'FIRMA_IN_CORSO',
     * 'IN_CODA_CREAZIONE_AIP')
     */
    public enum TiStatoElencoFascDaElab {
        AIP_CREATI, APERTO, CHIUSO, DA_CHIUDERE, ELENCO_INDICI_AIP_CREATO, ELENCO_INDICI_AIP_FIRMA_IN_CORSO, FIRMATO,
        FIRMA_IN_CORSO, IN_CODA_CREAZIONE_AIP
    }
}
