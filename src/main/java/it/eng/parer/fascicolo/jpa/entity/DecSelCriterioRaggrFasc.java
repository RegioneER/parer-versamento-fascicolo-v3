package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_SEL_CRITERIO_RAGGR_FASC database table.
 *
 */
@Entity
@Cacheable(true)
@Table(name = "DEC_SEL_CRITERIO_RAGGR_FASC")
@NamedQuery(name = "DecSelCriterioRaggrFasc.findAll", query = "SELECT d FROM DecSelCriterioRaggrFasc d")
public class DecSelCriterioRaggrFasc implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idSelCriterioRaggrFasc;
    private DecCriterioRaggrFasc decCriterioRaggrFasc;
    private String tiSel;
    private AplSistemaMigraz aplSistemaMigraz;
    private DecTipoFascicolo decTipoFascicolo;
    private DecVoceTitol decVoceTitol;

    public DecSelCriterioRaggrFasc() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_SEL_CRITERIO_RAGGR_FASC_IDSELCRITERIORAGGRFASC_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_SEL_CRITERIO_RAGGR_FASC"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_SEL_CRITERIO_RAGGR_FASC_IDSELCRITERIORAGGRFASC_GENERATOR")
    @Column(name = "ID_SEL_CRITERIO_RAGGR_FASC")
    public Long getIdSelCriterioRaggrFasc() {
        return this.idSelCriterioRaggrFasc;
    }

    public void setIdSelCriterioRaggrFasc(Long idSelCriterioRaggrFasc) {
        this.idSelCriterioRaggrFasc = idSelCriterioRaggrFasc;
    }

    // bi-directional many-to-one association to DecCriterioRaggrFasc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CRITERIO_RAGGR_FASC")
    public DecCriterioRaggrFasc getDecCriterioRaggrFasc() {
        return this.decCriterioRaggrFasc;
    }

    public void setDecCriterioRaggrFasc(DecCriterioRaggrFasc decCriterioRaggrFasc) {
        this.decCriterioRaggrFasc = decCriterioRaggrFasc;
    }

    @Column(name = "TI_SEL")
    public String getTiSel() {
        return this.tiSel;
    }

    public void setTiSel(String tiSel) {
        this.tiSel = tiSel;
    }

    // bi-directional many-to-one association to AplSistemaMigraz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SISTEMA_MIGRAZ")
    public AplSistemaMigraz getAplSistemaMigraz() {
        return this.aplSistemaMigraz;
    }

    public void setAplSistemaMigraz(AplSistemaMigraz aplSistemaMigraz) {
        this.aplSistemaMigraz = aplSistemaMigraz;
    }

    // bi-directional many-to-one association to DecTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_FASCICOLO")
    public DecTipoFascicolo getDecTipoFascicolo() {
        return this.decTipoFascicolo;
    }

    public void setDecTipoFascicolo(DecTipoFascicolo decTipoFascicolo) {
        this.decTipoFascicolo = decTipoFascicolo;
    }

    // bi-directional many-to-one association to DecVoceTitol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOCE_TITOL")

    public DecVoceTitol getDecVoceTitol() {
        return this.decVoceTitol;
    }

    public void setDecVoceTitol(DecVoceTitol decVoceTitol) {
        this.decVoceTitol = decVoceTitol;
    }

}