package it.eng.parer.fascicolo.jpa.viewEntity.constants;

/**
 * FasVLisUdInFasc's constraint
 *
 * @author Moretti_Lu
 */
public final class FasVLisUdInFasc {

    private FasVLisUdInFasc() {
    }

    /**
     * Tipo dello stato in conservazione
     */
    public enum TiStatoConservazione {
        AIP_DA_GENERARE, AIP_FIRMATO, AIP_GENERATO, AIP_IN_AGGIORNAMENTO, ANNULLATA, IN_ARCHIVIO, IN_CUSTODIA,
        IN_VOLUME_DI_CONSERVAZIONE, PRESA_IN_CARICO, VERSAMENTO_IN_ARCHIVIO
    }
}