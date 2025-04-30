package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.entity.constraint.ElvElencoVersFascDaElab.TiStatoElencoFascDaElab;
import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the ELV_ELENCO_VERS_FASC_DA_ELAB database table.
 *
 */
@Entity
@Table(name = "ELV_ELENCO_VERS_FASC_DA_ELAB")
@NamedQuery(name = "ElvElencoVersFascDaElab.findAll", query = "SELECT e FROM ElvElencoVersFascDaElab e")
public class ElvElencoVersFascDaElab implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idElencoVersDaElab;
    private BigDecimal aaFascicolo;
    private BigDecimal idCriterioRaggrFasc;
    private BigDecimal idStrut;
    private TiStatoElencoFascDaElab tiStato;
    private ElvElencoVersFasc elvElencoVersFasc;

    public ElvElencoVersFascDaElab() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "ELV_ELENCO_VERS_DA_ELAB_IDELENCOVERSDAELAB_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SELV_ELENCO_VERS_DA_ELAB"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ELV_ELENCO_VERS_DA_ELAB_IDELENCOVERSDAELAB_GENERATOR")
    @Column(name = "ID_ELENCO_VERS_DA_ELAB")
    public Long getIdElencoVersDaElab() {
        return this.idElencoVersDaElab;
    }

    public void setIdElencoVersDaElab(Long idElencoVersDaElab) {
        this.idElencoVersDaElab = idElencoVersDaElab;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
        return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
        this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "ID_CRITERIO_RAGGR_FASC")
    public BigDecimal getIdCriterioRaggrFasc() {
        return this.idCriterioRaggrFasc;
    }

    public void setIdCriterioRaggrFasc(BigDecimal idCriterioRaggrFasc) {
        this.idCriterioRaggrFasc = idCriterioRaggrFasc;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
        return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
        this.idStrut = idStrut;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_STATO")
    public TiStatoElencoFascDaElab getTiStato() {
        return this.tiStato;
    }

    public void setTiStato(TiStatoElencoFascDaElab tiStato) {
        this.tiStato = tiStato;
    }

    // bi-directional many-to-one association to ElvElencoVersFasc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ELENCO_VERS_FASC")
    public ElvElencoVersFasc getElvElencoVersFasc() {
        return this.elvElencoVersFasc;
    }

    public void setElvElencoVersFasc(ElvElencoVersFasc elvElencoVersFasc) {
        this.elvElencoVersFasc = elvElencoVersFasc;
    }

}
