package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * ElvFascDaElabElenco constraint
 *
 * @author sinatti_s
 */
public final class ElvFascDaElabElenco {

    private ElvFascDaElabElenco() {
    }

    /**
     * Tipo stato da elaborare ti_stato_fasc_da_elab IN ('IN_ATTESA_SCHED', 'NON_SELEZ_SCHED')
     */
    public enum TiStatoFascDaElab {
        IN_ATTESA_SCHED, NON_SELEZ_SCHED
    }
}