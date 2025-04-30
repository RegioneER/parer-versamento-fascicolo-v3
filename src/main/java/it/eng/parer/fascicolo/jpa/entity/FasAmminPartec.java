package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

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
 * The persistent class for the FAS_AMMIN_PARTEC database table.
 *
 */
@Entity
@Table(name = "FAS_AMMIN_PARTEC")
@NamedQuery(name = "FasAmminPartec.find", query = "SELECT f FROM FasAmminPartec f WHERE f.fasFascicolo.idFascicolo = :idFascicolo")
public class FasAmminPartec implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idAmminPartec;
    private String cdAmminPartec;
    private String dsAmminPartec;
    private String tiCodiceAmminPartec;
    private FasFascicolo fasFascicolo;

    public FasAmminPartec() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_AMMIN_PARTEC_IDAMMINPARTEC_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_AMMIN_PARTEC"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_AMMIN_PARTEC_IDAMMINPARTEC_GENERATOR")
    @Column(name = "ID_AMMIN_PARTEC")
    public Long getIdAmminPartec() {
        return this.idAmminPartec;
    }

    public void setIdAmminPartec(Long idAmminPartec) {
        this.idAmminPartec = idAmminPartec;
    }

    @Column(name = "CD_AMMIN_PARTEC")
    public String getCdAmminPartec() {
        return this.cdAmminPartec;
    }

    public void setCdAmminPartec(String cdAmminPartec) {
        this.cdAmminPartec = cdAmminPartec;
    }

    @Column(name = "DS_AMMIN_PARTEC")
    public String getDsAmminPartec() {
        return this.dsAmminPartec;
    }

    public void setDsAmminPartec(String dsAmminPartec) {
        this.dsAmminPartec = dsAmminPartec;
    }

    @Column(name = "TI_CODICE_AMMIN_PARTEC")
    public String getTiCodiceAmminPartec() {
        return this.tiCodiceAmminPartec;
    }

    public void setTiCodiceAmminPartec(String tiCodiceAmminPartec) {
        this.tiCodiceAmminPartec = tiCodiceAmminPartec;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FASCICOLO")
    public FasFascicolo getFasFascicolo() {
        return this.fasFascicolo;
    }

    public void setFasFascicolo(FasFascicolo fasFascicolo) {
        this.fasFascicolo = fasFascicolo;
    }
}