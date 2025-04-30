package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * FasStatoFascicoloElenco constraint
 *
 * @author sinatti_s
 *
 */
public final class FasStatoFascicoloElenco {

    private FasStatoFascicoloElenco() {
    }

    public enum TiStatoFascElenco {
        IN_ATTESA_SCHED, IN_ELENCO_APERTO, IN_ELENCO_CHIUSO, IN_ELENCO_COMPLETATO, IN_ELENCO_CON_AIP_CREATO,
        IN_ELENCO_CON_ELENCO_INDICI_AIP_CREATO, IN_ELENCO_DA_CHIUDERE, IN_ELENCO_FIRMATO,
        IN_ELENCO_IN_CODA_CREAZIONE_AIP, NON_SELEZ_SCHED
    }
}
