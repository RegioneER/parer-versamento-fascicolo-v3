package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_UD_AIP_FASCICOLO_DA_ELAB database table.
 *
 */
@Entity
@Table(name = "FAS_UD_AIP_FASCICOLO_DA_ELAB")
public class FasUdAipFascicoloDaElab implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idUdAipFascicoloDaElab;
    private BigDecimal aroVerIndiceAipUd;
    private FasAipFascicoloDaElab fasAipFascicoloDaElab;

    public FasUdAipFascicoloDaElab() {
        // hibernate constructor
    }

    @Id
    @SequenceGenerator(name = "FAS_UD_AIP_FASCICOLO_DA_ELAB_IDUDAIPFASCICOLODAELAB_GENERATOR", allocationSize = 1, sequenceName = "SFAS_UD_AIP_FASCICOLO_DA_ELAB")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_UD_AIP_FASCICOLO_DA_ELAB_IDUDAIPFASCICOLODAELAB_GENERATOR")
    @Column(name = "ID_UD_AIP_FASCICOLO_DA_ELAB")
    public Long getIdUdAipFascicoloDaElab() {
        return this.idUdAipFascicoloDaElab;
    }

    public void setIdUdAipFascicoloDaElab(Long idUdAipFascicoloDaElab) {
        this.idUdAipFascicoloDaElab = idUdAipFascicoloDaElab;
    }

    // bi-directional many-to-one association to AroVerIndiceAipUd
    // @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "ID_VER_INDICE_AIP")
    public BigDecimal getAroVerIndiceAipUd() {
        return this.aroVerIndiceAipUd;
    }

    public void setAroVerIndiceAipUd(BigDecimal aroVerIndiceAipUd) {
        this.aroVerIndiceAipUd = aroVerIndiceAipUd;
    }

    // bi-directional many-to-one association to FasAipFascicoloDaElab
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AIP_FASCICOLO_DA_ELAB")
    public FasAipFascicoloDaElab getFasAipFascicoloDaElab() {
        return this.fasAipFascicoloDaElab;
    }

    public void setFasAipFascicoloDaElab(FasAipFascicoloDaElab fasAipFascicoloDaElab) {
        this.fasAipFascicoloDaElab = fasAipFascicoloDaElab;
    }

}