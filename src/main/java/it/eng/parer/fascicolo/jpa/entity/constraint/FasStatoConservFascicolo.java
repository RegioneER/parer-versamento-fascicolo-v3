package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * FasStatoConservFascicolo constraint
 *
 * @author sinatti_s
 *
 */
public final class FasStatoConservFascicolo {

    private FasStatoConservFascicolo() {
    }

    public enum TiStatoConservazione {
        AIP_GENERATO, AIP_IN_AGGIORNAMENTO, ANNULLATO, IN_ARCHIVIO, PRESA_IN_CARICO, VERSAMENTO_IN_ARCHIVIO
    }
}