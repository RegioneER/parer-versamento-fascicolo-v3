package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
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
 * The persistent class for the DEC_ERR_AA_TIPO_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "DEC_ERR_AA_TIPO_FASCICOLO")
@NamedQuery(name = "DecErrAaTipoFascicolo.findAll", query = "SELECT d FROM DecErrAaTipoFascicolo d")
public class DecErrAaTipoFascicolo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idErrAaTipoFascicolo;
    private BigDecimal aaFascicolo;
    private String dsErrFmtNumero;
    private BigDecimal idFascicoloErrFmtNumero;
    private DecAaTipoFascicolo decAaTipoFascicolo;

    public DecErrAaTipoFascicolo() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_ERR_AA_TIPO_FASCICOLO_IDERRAATIPOFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_ERR_AA_TIPO_FASCICOLO"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_ERR_AA_TIPO_FASCICOLO_IDERRAATIPOFASCICOLO_GENERATOR")
    @Column(name = "ID_ERR_AA_TIPO_FASCICOLO")
    public Long getIdErrAaTipoFascicolo() {
        return this.idErrAaTipoFascicolo;
    }

    public void setIdErrAaTipoFascicolo(Long idErrAaTipoFascicolo) {
        this.idErrAaTipoFascicolo = idErrAaTipoFascicolo;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
        return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
        this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "DS_ERR_FMT_NUMERO")
    public String getDsErrFmtNumero() {
        return this.dsErrFmtNumero;
    }

    public void setDsErrFmtNumero(String dsErrFmtNumero) {
        this.dsErrFmtNumero = dsErrFmtNumero;
    }

    @Column(name = "ID_FASCICOLO_ERR_FMT_NUMERO")
    public BigDecimal getIdFascicoloErrFmtNumero() {
        return this.idFascicoloErrFmtNumero;
    }

    public void setIdFascicoloErrFmtNumero(BigDecimal idFascicoloErrFmtNumero) {
        this.idFascicoloErrFmtNumero = idFascicoloErrFmtNumero;
    }

    // bi-directional many-to-one association to DecAaTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AA_TIPO_FASCICOLO")
    public DecAaTipoFascicolo getDecAaTipoFascicolo() {
        return this.decAaTipoFascicolo;
    }

    public void setDecAaTipoFascicolo(DecAaTipoFascicolo decAaTipoFascicolo) {
        this.decAaTipoFascicolo = decAaTipoFascicolo;
    }

}