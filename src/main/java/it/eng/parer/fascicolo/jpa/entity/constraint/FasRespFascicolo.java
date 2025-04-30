package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * FasRespFascicolo's constraint
 *
 * @author Moretti_Lu
 */
public final class FasRespFascicolo {

    private FasRespFascicolo() {
    }

    /**
     * Tipo oggetto responsabile
     */
    public enum TiOggResp {
        FASCICOLO, PROC_AMMIN
    }
}