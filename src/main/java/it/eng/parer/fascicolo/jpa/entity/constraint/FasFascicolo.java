package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * FasFascicolo's constraint
 *
 * @author Moretti_Lu
 */
public final class FasFascicolo {

    private FasFascicolo() {
    }

    /**
     * Tipo di conservazione
     */
    public enum TiConservazione {
        IN_ARCHIVIO, MIGRAZIONE, VERSAMENTO_ANTICIPATO
    }

    /**
     * Stato di conservazione
     */
    public enum TiStatoConservazione {
        PRESA_IN_CARICO, AIP_IN_AGGIORNAMENTO, AIP_GENERATO, ANNULLATO, IN_ARCHIVIO, VERSAMENTO_IN_ARCHIVIO
    }

    /**
     * Stato di conservazione con stato &lt;&gt; ANNULLATO
     */
    public enum TiStatoConservazioneNonAnnullato {
        PRESA_IN_CARICO, AIP_IN_AGGIORNAMENTO, AIP_GENERATO, IN_ARCHIVIO, VERSAMENTO_IN_ARCHIVIO
    }

    /**
     * Stato dei fascicoli in conservazione
     */
    public enum TiStatoFascElencoVers {
        IN_ATTESA_SCHED, IN_ELENCO_APERTO, IN_ELENCO_CHIUSO, IN_ELENCO_COMPLETATO, IN_ELENCO_CON_AIP_CREATO,
        IN_ELENCO_CON_ELENCO_INDICI_AIP_CREATO, IN_ELENCO_DA_CHIUDERE, IN_ELENCO_FIRMATO,
        IN_ELENCO_IN_CODA_CREAZIONE_AIP, NON_SELEZ_SCHED
    }

}
