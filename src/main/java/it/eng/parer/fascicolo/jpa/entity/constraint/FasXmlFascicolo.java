package it.eng.parer.fascicolo.jpa.entity.constraint;

/**
 * VRS_UPD_UNITA_DOC_KO's constraint
 *
 * @author sinatti_s
 */
public final class FasXmlFascicolo {

    private FasXmlFascicolo() {
    }

    /**
     * Tipi modelli
     *
     * ti_modello_xsd IN ('PROFILO_GENERALE_FASCICOLO', 'PROFILO_ARCHIVISTICO_FASCICOLO',
     * 'PROFILO_SPECIFICO_FASCICOLO','PROFILO_NORMATIVO_FASCICOLO')
     */
    public enum TiModXsdFasXmlFascicolo {
        PROFILO_GENERALE_FASCICOLO, PROFILO_ARCHIVISTICO_FASCICOLO, PROFILO_SPECIFICO_FASCICOLO,
        PROFILO_NORMATIVO_FASCICOLO
    }

}